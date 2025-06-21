package com.example.cpp.homepagestud;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ResultFragment extends Fragment {
    LinearLayout linearLayout;
    TextView textView;
    Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_result, container, false);
        linearLayout=view.findViewById(R.id.linearresult);
        textView=view.findViewById(R.id.noresult);
        textView.setVisibility(View.GONE);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Testresult");
        storageReference.listAll().addOnSuccessListener(listResult -> {
            if (!(listResult.getPrefixes().size()>0)){
                textView.setVisibility(View.VISIBLE);
                return;
            }
            for (StorageReference prefix : listResult.getPrefixes()){

                String teacherUid = prefix.getName();
                prefix.listAll().addOnSuccessListener(imageList -> {
                    for (StorageReference item : imageList.getItems()){
                        String imagedesc=item.getName();
                        FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                View view1 = inflater.inflate(R.layout.activity_studresultview,null);
                                TextView imageView=view1.findViewById(R.id.resultimage);
                                Button delete =view1.findViewById(R.id.deleteresult);
                                delete.setVisibility(View.GONE);
                                TextView subjectcode=view1.findViewById(R.id.resultsubjectcode);
                                TextView yearmonth=view1.findViewById(R.id.resultyear);
                                TextView testname=view1.findViewById(R.id.resultname);
                                TextView teachername=view1.findViewById(R.id.teacherid);
                                TextView branchcode=view1.findViewById(R.id.branchcode);
                                DocumentReference df = FirebaseFirestore.getInstance().collection("User").document(teacherUid);
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                                        item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                            @Override
                                            public void onSuccess(StorageMetadata storageMetadata) {


                                                    String facultyName = "Faculty Name : " + storageMetadata.getCustomMetadata("facultyname");
                                                String testnameS="Test Name : "+ storageMetadata.getCustomMetadata("examname");
                                                String yearmonthS="Test Year and Month : "+ storageMetadata.getCustomMetadata("yearmonth");
                                                String subjectcodeS="Test Subject Code : "+ storageMetadata.getCustomMetadata("subjectcode");
                                                String branchcodeS="Branch Code : "+ storageMetadata.getCustomMetadata("branchcode");
                                                if(storageMetadata.getCustomMetadata("branchcode")!=null) {

                                                    if (!storageMetadata.getCustomMetadata("branchcode").substring(0, 2).equals(documentSnapshot.getString("Branch"))) {
//
                                                        return;
                                                    }
                                                }
                                                if(storageMetadata.getCustomMetadata("branchcode")==null) {
                                                    return;
                                                }
                                                SpannableString spannableFacultyName = new SpannableString(facultyName);
                                                SpannableString spannableTestId = new SpannableString(testnameS);
                                                SpannableString spanablemonth = new SpannableString(yearmonthS);
                                                SpannableString spanablecode = new SpannableString(subjectcodeS);
                                                SpannableString spannablebranchcode = new SpannableString(branchcodeS);

// Make the first 10 characters bold
                                                spannableFacultyName.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(15, facultyName.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                spannableTestId.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(12, testnameS.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                spanablemonth.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(21, yearmonthS.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                spanablecode.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(19, subjectcodeS.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                spannablebranchcode.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(13, branchcodeS.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                teachername.setText(spannableFacultyName);
                                                testname.setText(spannableTestId);
                                                yearmonth.setText(spanablemonth);
                                                subjectcode.setText(spanablecode);
                                                branchcode.setText(spannablebranchcode);
                                                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageView.setOnClickListener(new View.OnClickListener() {
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

                                                linearLayout.addView(view1);

                                            }
                                        });







                                        // Load the downloaded bytes into the ImageView using Glide



                                    }
                                });

                            }
                        });

                    }
                });
            }
        });

        return view;

    }
    private void downloadPdfAndLoad(Uri pdfUri, PDFView pdfView) {
        // Create a temporary file to store the downloaded PDF
        File pdfFile = new File(requireContext().getCacheDir(), "temp.pdf");

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