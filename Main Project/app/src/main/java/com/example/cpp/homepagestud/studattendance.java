package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.facultyadmin.facultyadminattendence;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class studattendance extends AppCompatActivity {
    TextView textView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studattendance);
        linearLayout=findViewById(R.id.studattendlinear);
        textView=findViewById(R.id.noattendencefound);
        textView.setVisibility(View.GONE);
        FirebaseStorage.getInstance().getReference().child("attendance/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                         if (listResult.getPrefixes().size()>0){
                             for (StorageReference prefix:listResult.getPrefixes()){
                                 prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                     @Override
                                     public void onSuccess(ListResult listResult) {
                                        for (StorageReference reference:listResult.getItems()){

                                            reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                                @Override
                                                public void onSuccess(StorageMetadata storageMetadata) {
                                                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                            if (!storageMetadata.getCustomMetadata("branch").substring(0,2).equals(documentSnapshot.getString("Branch"))){
                                                                return;
                                                            }
                                                            View view=getLayoutInflater().inflate(R.layout.activity_attendanceview,null);
                                                            TextView code=view.findViewById(R.id.attendenceviewcode);
                                                            TextView branch=view.findViewById(R.id.attendenceviewbranch);
                                                            TextView month=view.findViewById(R.id.attendenceviewmonth);
                                                            TextView name=view.findViewById(R.id.attendenceviewteachname);
                                                            TextView pdfView1=view.findViewById(R.id.pdfattendenceview);
                                                            Button delete=view.findViewById(R.id.deleteattendence);
                                                            delete.setVisibility(View.GONE);
                                                            String subjectCode = "SUBJECT CODE : " + storageMetadata.getCustomMetadata("code");
                                                            String branchCode = "BRANCH CODE : " + storageMetadata.getCustomMetadata("branch");
                                                            String monthValue = "MONTH : " + storageMetadata.getCustomMetadata("month");
                                                            String nameValue = "NAME : " + storageMetadata.getCustomMetadata("name");


                                                            SpannableString spannableSubjectCode = new SpannableString(subjectCode);
                                                            spannableSubjectCode.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(14, subjectCode.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                            SpannableString spannableBranchCode = new SpannableString(branchCode);
                                                            spannableBranchCode.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(14, branchCode.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                            SpannableString spannableMonthValue = new SpannableString(monthValue);
                                                            spannableMonthValue.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(8, monthValue.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                            SpannableString spannableNameValue = new SpannableString(nameValue);
                                                            spannableNameValue.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(7, nameValue.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                                            code.setText(spannableSubjectCode);
                                                            branch.setText(spannableBranchCode);
                                                            month.setText(spannableMonthValue);
                                                            name.setText(spannableNameValue);

                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    pdfView1.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            Intent intent=new Intent(Intent.ACTION_VIEW);
                                                                            intent.setDataAndType(uri, "application/pdf");
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            try {
                                                                                startActivity(intent);
                                                                            }catch (Exception e){
                                                                                Log.d("Exception",e.toString());
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            });


                                                            linearLayout.addView(view);


                                                        }
                                                    });

                                                }
                                            });


                                        }
                                     }
                                 });
                             }
                         }else {
                             textView.setVisibility(View.VISIBLE);
                         }
            }
        });
    }
    private void downloadPdfAndLoad(Uri pdfUri, PDFView pdfView) {
        // Create a temporary file to store the downloaded PDF
        File pdfFile = new File(getApplicationContext().getCacheDir(), "temp.pdf");

        // Download the PDF file
        FirebaseStorage.getInstance().getReferenceFromUrl(pdfUri.toString())
                .getFile(pdfFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Load the downloaded PDF into the PDFView
                        pdfView.fromFile(pdfFile)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableDoubletap(true)
                                .defaultPage(0)
                                .enableAnnotationRendering(false)
                                .password(null)
                                .pageFitPolicy(FitPolicy.WIDTH)
                                .load();
                        pdfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(pdfUri, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    startActivity(intent);
                                }catch (Exception e){
                                    Log.d("Exception",e.toString());
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle the failure to download the PDF
                        Log.e("Download Failure", exception.toString());
                    }
                });
    }
}