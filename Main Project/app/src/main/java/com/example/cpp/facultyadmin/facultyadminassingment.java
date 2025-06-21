package com.example.cpp.facultyadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.BranchInputFilter;
import com.example.cpp.CharacterInputFilter;
import com.example.cpp.NumericInputFilter;
import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

public class facultyadminassingment extends AppCompatActivity {
    TextInputEditText name,scheme,endate,code,number;

    Button submit,select;
    Uri uri;
    LinearLayout linearLayout;
    PDFView pdfViewtemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultyadminassingment);
        name=findViewById(R.id.assingmentname);
        name.setFilters(new InputFilter[]{new CharacterInputFilter()});
        number=findViewById(R.id.assingmentnumber);
        scheme=findViewById(R.id.assingmentscheme);
        scheme.setFilters(new InputFilter[]{new NumericInputFilter()});
        code=findViewById(R.id.assingmentsubjectcode);
        code.setFilters(new InputFilter[]{new NumericInputFilter()});
        endate=findViewById(R.id.assingmentdate);
        scheme.setFilters(new InputFilter[]{new CharacterInputFilter()});
        submit=findViewById(R.id.submitassingment);
        select=findViewById(R.id.selectassingment);
        linearLayout=findViewById(R.id.adminassingmentlinear);
        pdfViewtemp=findViewById(R.id.assingmentpdftemp);

        endate.setKeyListener(null);// disable keyboard input
        endate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(facultyadminassingment.this, endate);
            }

            private void showDatePickerDialog(facultyadminassingment facultyadminassingment, TextInputEditText endate) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        facultyadminassingment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                endate.setText(selectedDate);
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });




        pdfViewtemp.setVisibility(View.GONE);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE ASSIGNMENT"),1);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminassingment.this, "ENTER NAME PROPERLY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminassingment.this, "ENTER ASSIGNMENT NUMBER PROPERLY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminassingment.this, "ENTER SUBJECT CODE PROPERLY", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (endate.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminassingment.this, "ENTER SUBMIT DATE PROPERLY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scheme.getText().toString().isEmpty()){
                    Toast.makeText(facultyadminassingment.this, "ENTER SCHEME PROPERLY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((code.getText().toString().length())>5||(code.getText().toString().length())<5){
                    Toast.makeText(facultyadminassingment.this, "ENTER VALID SUBJECT CODE"+code.getText().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((number.getText().toString().length())>5||(number.getText().toString().length())<5){
                    Toast.makeText(facultyadminassingment.this, "ENTER VALID ASSIGNMENT NUMBER", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri==null){
                    Toast.makeText(facultyadminassingment.this, "SELECT ASSIGNMENT", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseStorage.getInstance().getReference().child("assignment/"+FirebaseAuth.getInstance().getUid()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        if (listResult.getItems().size()>5){
                            Toast.makeText(facultyadminassingment.this, "MAXIMUM LIMIT OF UPLOAD REACHED(5)", Toast.LENGTH_SHORT).show();
                            Toast.makeText(facultyadminassingment.this, "DELETE AVAILABLE ASSIGNMENTS FIRST", Toast.LENGTH_SHORT).show();
                        }else{
                            StorageMetadata metadata= new StorageMetadata.Builder()
                                    .setCustomMetadata("name",name.getText().toString())
                                    .setCustomMetadata("number",number.getText().toString())
                                    .setCustomMetadata("code",code.getText().toString())
                                    .setCustomMetadata("submitdate",endate.getText().toString())
                                    .setCustomMetadata("scheme",scheme.getText().toString())
                                    .build();
                            FirebaseStorage.getInstance().getReference().child("assignment").child(FirebaseAuth.getInstance().getUid()).child(number.getText().toString()).putFile(uri,metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Toast.makeText(facultyadminassingment.this, "SUBMITTED", Toast.LENGTH_SHORT).show();
                                    FCMSend.Builder builder=new FCMSend.Builder("assignment"+scheme.getText().toString().substring(0,2),true)
                                            .setTitle("NEW ASSINGMENT")
                                            .setBody("NEW ASSIGNMENT HAS ARRIVED OF 💀💀"+code.getText().toString()+"\nclick to know more");
                                    builder.send();
                                    uri=null;
                                    name.setText("");
                                    number.setText("");
                                    code.setText("");
                                    endate.setText("");
                                    scheme.setText("");
                                    pdfViewtemp.setVisibility(View.GONE);
                                    return;
                                }
                            });
                        }

                    }
                });

            }

        });
        FirebaseStorage.getInstance().getReference().child("assignment/"+FirebaseAuth.getInstance().getUid()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size()>0){
                    for (StorageReference reference:listResult.getItems()){
                        View view=getLayoutInflater().inflate(R.layout.activity_assingmentview,null);
                        TextView pdfView=view.findViewById(R.id.assingmentviewpdf);
                        TextView name=view.findViewById(R.id.assingmentviewname);
                        TextView number=view.findViewById(R.id.assingmentviewnumber);
                        TextView code=view.findViewById(R.id.assingmentviewcode);
                        TextView scheme=view.findViewById(R.id.assingmentviewscheme);
                        TextView submitdate=view.findViewById(R.id.assingmentviewdate);
                        TextView teachername=view.findViewById(R.id.assingmentviewteachname);
                        teachername.setVisibility(View.GONE);
                        Button delete=view.findViewById(R.id.deleteassingment);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reference.delete();
                                Toast.makeText(facultyadminassingment.this, "DELETED!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                name.setText("Name: "+storageMetadata.getCustomMetadata("name"));
                                number.setText("Assignment Number: "+storageMetadata.getCustomMetadata("number"));
                                code.setText("Subject Code: "+storageMetadata.getCustomMetadata("code"));
                                scheme.setText("Scheme: "+storageMetadata.getCustomMetadata("scheme"));
                                submitdate.setText("Submit Date: "+storageMetadata.getCustomMetadata("submitdate"));

                            }
                        });
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                            }
                        });
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                           pdfView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {


                                   Intent intent=new Intent(Intent.ACTION_VIEW);
                                   intent.setDataAndType(Uri.parse(uri.toString()), "application/pdf");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            pdfViewtemp.setVisibility(View.VISIBLE);
            pdfViewtemp.fromUri(uri).defaultPage(1).fitEachPage(true).load();

        }
    }

}