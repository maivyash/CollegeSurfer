package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.principleadmin.adminexamsheduleupload;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class studtimetable extends AppCompatActivity {
PDFView pdfView;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studtimetable);
        textView=findViewById(R.id.notimeoftable);
        pdfView=findViewById(R.id.timetablepdf);
        load();

    }
    public  void load(){
        StorageReference reference=FirebaseStorage.getInstance().getReference().child("timetable.pdf");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (!(uri==null)){
                    textView.setText("ACTIVE TIME TABLE ");
                    textView.setTextColor(ContextCompat.getColor(studtimetable.this, R.color.dark_green));


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
}