package com.example.cpp.principleadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class adminexamsheduleupload extends AppCompatActivity {
    Button select,upload,delete;
    TextView textView;
    PDFView pdfView;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminexamsheduleupload);
        select=findViewById(R.id.selecttimetable);
        upload=findViewById(R.id.uploadexamtimetable);
        textView=findViewById(R.id.notimetable);
        pdfView=findViewById(R.id.pdftimetable);
        delete=findViewById(R.id.deletetimetable);
        load();
        delete.setVisibility(View.GONE);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE TIME TABLE"),1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri==null){
                    Toast.makeText(adminexamsheduleupload.this, "SELECT TIME TABLE!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadandrenew(uri);
            }
        });

    }

    private void uploadandrenew(Uri thisuri) {
        FirebaseStorage.getInstance().getReference().child("timetable.pdf").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(adminexamsheduleupload.this, "UPLOADED", Toast.LENGTH_SHORT).show();
                FCMSend.Builder builder=new FCMSend.Builder("exam",true)
                        .setTitle("EXAM SHEDULE")
                        .setBody("NEW EXAM SHEDULE IS ARRIVED 📝📝");
                builder.send();
                load();
            }
        });
    }
    public  void load(){
        StorageReference reference=FirebaseStorage.getInstance().getReference().child("timetable.pdf");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri==null)){
                    textView.setText("ACTIVE TIME TABLE");
                    textView.setTextColor(ContextCompat.getColor(adminexamsheduleupload.this, R.color.dark_green));
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(adminexamsheduleupload.this, "DELETED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                    downloadPdfAndLoad(uri);
                }
            }
        });
    }
    private void downloadPdfAndLoad(Uri pdfUri) {
        // Create a temporary file to store the downloaded PDF
        File pdfFile = new File(getCacheDir(), "temp.pdf");

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
                                .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                                .pageFitPolicy(FitPolicy.WIDTH)
                                .load();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Toast.makeText(this, "SELECTED", Toast.LENGTH_SHORT).show();

        }
    }
}