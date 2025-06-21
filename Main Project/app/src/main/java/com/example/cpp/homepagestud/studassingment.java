package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cpp.R;
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

public class studassingment extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studassingment);
        textView=findViewById(R.id.noassingment);
        textView.setVisibility(View.GONE);
        linearLayout=findViewById(R.id.studassingmentlinear);
        FirebaseStorage.getInstance().getReference().child("assignment/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if ((listResult.getPrefixes().size()>0)) {


                    for (StorageReference prefix : listResult.getPrefixes()) {
                        String uid = prefix.getName();
                        prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                for (StorageReference reference : listResult.getItems()) {

                                    reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(StorageMetadata storageMetadata) {
                                            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (!storageMetadata.getCustomMetadata("scheme").substring(0,2).equals(documentSnapshot.getString("Branch"))){
                                                       return;
                                                    }
                                                    View view = getLayoutInflater().inflate(R.layout.activity_assingmentview, null);
                                                    TextView pdfView = view.findViewById(R.id.assingmentviewpdf);
                                                    TextView name = view.findViewById(R.id.assingmentviewname);
                                                    TextView number = view.findViewById(R.id.assingmentviewnumber);
                                                    TextView code = view.findViewById(R.id.assingmentviewcode);
                                                    TextView scheme = view.findViewById(R.id.assingmentviewscheme);
                                                    TextView submitdate = view.findViewById(R.id.assingmentviewdate);
                                                    TextView teachername = view.findViewById(R.id.assingmentviewteachname);
                                                    Button delete = view.findViewById(R.id.deleteassingment);
                                                    delete.setVisibility(View.GONE);
                                                    name.setText("NAME : " + storageMetadata.getCustomMetadata("name"));
                                                    number.setText("ASSINGMENT NUMBER : " + storageMetadata.getCustomMetadata("number"));
                                                    code.setText("SUBJECT CODE : " + storageMetadata.getCustomMetadata("code"));
                                                    scheme.setText("SCHEME : " + storageMetadata.getCustomMetadata("scheme"));
                                                    submitdate.setText("SUBMIT DATE : " + storageMetadata.getCustomMetadata("submitdate"));

                                                    FirebaseFirestore.getInstance().collection("User").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            teachername.setText("Teacher Name: " + documentSnapshot.getString("Fullname"));
                                                        }
                                                    });
                                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            pdfView.setOnClickListener(new View.OnClickListener() {
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
                    return;
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