package com.example.cpp.facultyadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.R;
import com.example.cpp.homepagestud.studresultview;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class Facultyadminaddtestresult extends AppCompatActivity {
    TextInputEditText examname,subjectcode,yearandmonth,branchcode;
    Button select,upload;
    PDFView imageView;
    Uri uri;
    LinearLayout linearLayout;
    TextView textView;
    ProgressDialog progressDialog;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyadminaddtestresult);
        subjectcode=findViewById(R.id.subjectcode);
        examname=findViewById(R.id.examname);
        yearandmonth=findViewById(R.id.yearmonth);
        select=findViewById(R.id.selectresultimagetoupload);
        upload=findViewById(R.id.uploadtestresult);
        imageView=findViewById(R.id.showimageresulttemp);
        linearLayout=findViewById(R.id.showtestresult);
        textView=findViewById(R.id.noresulttoshow1);
        branchcode=findViewById(R.id.branchcode);




        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"CHOOSE RESULT"),1);

            }

        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (yearandmonth.getText().toString().isEmpty()){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER YEAR AND MONTH", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subjectcode.getText().toString().isEmpty()){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER SUBJECT CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subjectcode.getText().toString().length()!=6){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER VALID SUBJECT CODE ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (examname.getText().toString().isEmpty()){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER SUBJECT NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branchcode.getText().toString().isEmpty()){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER BRANCH CODE NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branchcode.getText().toString().length()!=4){
                    Toast.makeText(Facultyadminaddtestresult.this, "ENTER VALID BRANCH CODE NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri==null){
                    Toast.makeText(Facultyadminaddtestresult.this, "Select Photo of Result to Upload", Toast.LENGTH_SHORT).show();
                    return;
                }


                DocumentReference df=FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        StorageReference sf= FirebaseStorage.getInstance().getReference().child("Testresult/"+FirebaseAuth.getInstance().getUid()+"/");

                        sf.listAll().addOnSuccessListener(listResult -> {
                            if (listResult.getItems().size()>4){
                                Toast.makeText(Facultyadminaddtestresult.this, "MAX LIMIT IS REACHED: 5", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Facultyadminaddtestresult.this, "DELETE THE ALREADY PRESENT RESULT FIRST", Toast.LENGTH_SHORT).show();


                            }else {
                                StorageReference sf1 =FirebaseStorage.getInstance().getReference().child("Testresult/"+FirebaseAuth.getInstance().getUid()).child(UUID.randomUUID().toString()+".pdf");
                                StorageMetadata metadata= new StorageMetadata.Builder()
                                        .setCustomMetadata("branchcode",branchcode.getText().toString())
                                        .setCustomMetadata("yearmonth",yearandmonth.getText().toString())
                                        .setCustomMetadata("subjectcode",subjectcode.getText().toString())
                                        .setCustomMetadata("examname",examname.getText().toString())
                                        .setCustomMetadata("facultyname",documentSnapshot.getString("Fullname"))
                                        .build();
                                sf1.putFile(uri,metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            Toast.makeText(Facultyadminaddtestresult.this, "Successful uploaded", Toast.LENGTH_SHORT).show();
                                        FCMSend.Builder builder=new FCMSend.Builder("result"+branchcode.getText().toString().substring(0,2),true)
                                                .setTitle("NEW RESULT")
                                                .setBody("RESULT OF SUBJECT CODE "+subjectcode.getText().toString()+"HAS ARRIVED \nclick to know more");
                                        builder.send();
                                            imageView.setVisibility(View.GONE);
                                            examname.setText("");
                                            subjectcode.setText("");
                                            yearandmonth.setText("");
                                            branchcode.setText("");
                                            uri = null;
                                        }
                                });
                            }
                        });

                    }
                });

            }

        });

        StorageReference sf =FirebaseStorage.getInstance().getReference().child("Testresult/"+FirebaseAuth.getInstance().getUid());

        sf.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().size()>0) {
                for (StorageReference reference:listResult.getItems()){
                    textView.setVisibility(View.GONE);
                    Log.d("TEST",String.valueOf(i));
                    View view1 = getLayoutInflater().inflate(R.layout.activity_studresultview,null);
                    TextView imageView=view1.findViewById(R.id.resultimage);
                    TextView teachername=view1.findViewById(R.id.teacherid);
                    TextView subjectcode=view1.findViewById(R.id.resultsubjectcode);
                    TextView yearmonth=view1.findViewById(R.id.resultyear);
                    TextView testname=view1.findViewById(R.id.resultname);
                    TextView branchcode=view1.findViewById(R.id.branchcode);
                    i++;
                    MaterialButton delete=view1.findViewById(R.id.deleteresult);
                    DocumentReference df = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Facultyadminaddtestresult.this, "DELETED", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    testname.setText("Test Name : "+storageMetadata.getCustomMetadata("examname"));
                                    teachername.setText("Teacher Name : "+storageMetadata.getCustomMetadata("facultyname"));
                                    subjectcode.setText("Test Name : "+storageMetadata.getCustomMetadata("subjectcode"));
                                    yearmonth.setText("Test Name : "+storageMetadata.getCustomMetadata("yearmonth"));
                                    branchcode.setText("BRANCH CODE : "+storageMetadata.getCustomMetadata("branchcode"));
                                }
                            });

                           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                        }
                    });
                    linearLayout.addView(view1);


                }

            }else {
                if(!(listResult.getItems().size()>0)){
                    textView.setVisibility(View.VISIBLE);
                    Log.d("HEHEHE","RIKNKIYA KE PAPA");
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.toString());
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
                imageView.setVisibility(View.VISIBLE);
            }
            imageView.fromUri(uri)
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