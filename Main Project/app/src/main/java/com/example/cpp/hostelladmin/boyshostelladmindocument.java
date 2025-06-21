package com.example.cpp.hostelladmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class boyshostelladmindocument extends AppCompatActivity {

    PDFView pdfARC, pdfmarksheet, pdfadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boyshostelladmindocument);

        pdfARC = findViewById(R.id.viewARC);
        pdfmarksheet = findViewById(R.id.viewmarksheet);
        pdfadhar = findViewById(R.id.viewadharcard);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");

        // Load ARC PDF
        loadPdfFromFirebase("hostelladdmisiondoc/" + documentId + "/ARC.pdf", pdfARC);

        // Load marksheet PDF
        loadPdfFromFirebase("hostelladdmisiondoc/" + documentId + "/marksheet.pdf", pdfmarksheet);

        // Load aadhar PDF
        loadPdfFromFirebase("hostelladdmisiondoc/" + documentId + "/Adhar.pdf", pdfadhar);
    }

    private void loadPdfFromFirebase(String path, PDFView pdfView) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(path);

        File localFile = new File(getExternalCacheDir(), path.substring(path.lastIndexOf("/") + 1));

        reference.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if (task.isComplete()) {
                    // File downloaded successfully, now load the PDF
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(localFile), "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            try {
                                startActivity(intent);
                            }catch (Exception e){
                                Log.d("Exception",e.toString());
                            }
                            pdfView.fromFile(localFile)
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
                    });
                } else {
                    // Handle the failure to download the file
                    Toast.makeText(boyshostelladmindocument.this, "Failed to download PDF: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}