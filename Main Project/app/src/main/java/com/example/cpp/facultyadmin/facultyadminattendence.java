package com.example.cpp.facultyadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.CollationElementIterator;
import java.util.UUID;

public class facultyadminattendence extends AppCompatActivity {
    TextInputEditText subject,branch,month,name;
    Button select,upload;
    LinearLayout linearLayout;
    PDFView pdfView;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyadminattendence);
        subject=findViewById(R.id.adminattendencesubjectcode);
        branch=findViewById(R.id.adminattendencebranchcode);
        month=findViewById(R.id.adminattendencemonth);
        select=findViewById(R.id.attendenceselect);
        upload=findViewById(R.id.uploadassignment);
        linearLayout=findViewById(R.id.adminattendencelinear);
        pdfView=findViewById(R.id.pdfattendence);
        pdfView.setVisibility(View.GONE);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE BILL"),1);

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminattendence.this, "ENTER SUBJECT CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branch.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminattendence.this, "ENTER BRANCH CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (month.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminattendence.this, "ENTER MONTH", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(subject.getText().toString().length()==6)){
                    Toast.makeText(facultyadminattendence.this, "ENTER VALID SUBJECT CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(branch.getText().toString().length()<=6)){
                    Toast.makeText(facultyadminattendence.this, "ENTER VALID BRANCH CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri==null){
                    Toast.makeText(facultyadminattendence.this, "SELECT ATTENDANCE", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        StorageMetadata metadata=new StorageMetadata.Builder()
                                .setCustomMetadata("code",subject.getText().toString())
                                .setCustomMetadata("branch",branch.getText().toString())
                                .setCustomMetadata("month",month.getText().toString())
                                .setCustomMetadata("name",documentSnapshot.getString("Fullname"))
                                .build();

                        FirebaseStorage.getInstance().getReference().child("attendance/"+ FirebaseAuth.getInstance().getUid()).child(UUID.randomUUID().toString()).putFile(uri,metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(facultyadminattendence.this, "UPLOADED", Toast.LENGTH_SHORT).show();
                                FCMSend.Builder builder=new FCMSend.Builder("attendance"+branch.getText().toString().substring(0,2),true)
                                        .setTitle("ATTENDENCE")
                                        .setBody("ATTENDENCE RECORD OF CURRENT MONTH HAS BEEN PUBLISHED OF"+subject.getText().toString()+"⚠️⚠️");
                                builder.send();
                                subject.setText("");
                                branch.setText("");
                                month.setText("");
                                name.setText("");
                                pdfView.setVisibility(View.GONE);

                            }
                        });
                    }
                });

            }
        });
        addview();


    }
    public  void addview(){
        FirebaseStorage.getInstance().getReference().child("attendance/"+ FirebaseAuth.getInstance().getUid()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size()>0){
                    for (StorageReference reference:listResult.getItems()){
                        View view=getLayoutInflater().inflate(R.layout.activity_attendanceview,null);
                        TextView code=view.findViewById(R.id.attendenceviewcode);
                        TextView branch=view.findViewById(R.id.attendenceviewbranch);
                        TextView month=view.findViewById(R.id.attendenceviewmonth);
                        TextView name=view.findViewById(R.id.attendenceviewteachname);
                        TextView pdfView1=view.findViewById(R.id.pdfattendenceview);
                        name.setVisibility(View.GONE);
                        Button delete=view.findViewById(R.id.deleteattendence);
                        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                name.setText("NAME: "+storageMetadata.getCustomMetadata("name"));
                                code.setText("SUBJECT CODE: "+storageMetadata.getCustomMetadata("code"));
                                branch.setText("BRANCH CODE: "+storageMetadata.getCustomMetadata("branch"));
                                month.setText("MONTH: "+storageMetadata.getCustomMetadata("month"));


                            }
                        });
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
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(facultyadminattendence.this, "DELETED", Toast.LENGTH_SHORT).show();
                                        linearLayout.removeAllViews();
                                        addview();
                                    }
                                });

                            }
                        });

                        linearLayout.addView(view);

                    }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();

            if (!(uri==null)){
                pdfView.setVisibility(View.VISIBLE);
            }
            pdfView.fromUri(uri)
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
    }
}