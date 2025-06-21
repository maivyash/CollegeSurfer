package com.example.cpp.principleadmin;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class principaladminevent extends AppCompatActivity {
    LinearLayout linearLayout;
    Button upload,select;
    EditText name,desc,link;
    Uri uri;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principaladminevent);
        linearLayout=findViewById(R.id.eventlinear);
        upload=findViewById(R.id.uploadevent);
        name=findViewById(R.id.eventnameinput);
        desc=findViewById(R.id.eventdescriptioninput);
        link=findViewById(R.id.eventlinkinput);
        select=findViewById(R.id.selectevent);
        textView=findViewById(R.id.noeventactive);

        load();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE EVENT BANNER"),1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(principaladminevent.this, "ENTER NAME", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (desc.getText().toString().isEmpty()){
                    Toast.makeText(principaladminevent.this, "ENTER DESCRIPTION", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (link.getText().toString().isEmpty()){
                    Toast.makeText(principaladminevent.this, "ENTER LINK", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri==null){
                    Toast.makeText(principaladminevent.this, "SELECT BANNER OF EVENT", Toast.LENGTH_SHORT).show();
                    return;
                }
                StorageMetadata metadata=new StorageMetadata.Builder()
                        .setCustomMetadata("name",name.getText().toString())
                        .setCustomMetadata("desc",desc.getText().toString())
                        .setCustomMetadata("link",link.getText().toString())
                        .build();
                FirebaseStorage.getInstance().getReference().child("event/").child(UUID.randomUUID().toString()).putFile(uri,metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(principaladminevent.this, "UPLOADED!!", Toast.LENGTH_SHORT).show();
                        FCMSend.Builder builder=new FCMSend.Builder("event",true)
                                .setTitle("NEW EVENT ")
                                .setBody("NEW EVENT HAS ARRIVED 😍😍"+"\nclick here to view");
                        builder.send();
                        name.setText("");
                        desc.setText("");
                        link.setText("");
                        load();
                        uri=null;
                    }
                });



            }
        });
    }

    private void load() {
        FirebaseStorage.getInstance().getReference().child("event/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size()>0){
                    textView.setText("ACTIVE EVENTS ");
                    textView.setGravity(Gravity.TOP);
                    textView.setTextColor(getResources().getColor(R.color.green));
                    for (StorageReference reference:listResult.getItems()){
                        View view=getLayoutInflater().inflate(R.layout.activity_eventview,null);
                        TextView name=view.findViewById(R.id.eventnameview);
                        TextView desc=view.findViewById(R.id.eventdescview);
                        TextView link=view.findViewById(R.id.eventlinkview);
                        TextView pdfView=view.findViewById(R.id.pdfevent);
                        Button delete=view.findViewById(R.id.deleteevent);
                        reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                name.setText("EVENT NAME: "+storageMetadata.getCustomMetadata("name"));
                                desc.setText("EVENT DESCRIPTION: "+storageMetadata.getCustomMetadata("desc"));
                                link.setText("EVENT LINK: "+storageMetadata.getCustomMetadata("link"));
                                link.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+storageMetadata.getCustomMetadata("link"))));
                                    }
                                });

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
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(principaladminevent.this, "DELETED!!", Toast.LENGTH_SHORT).show();
                                        linearLayout.removeAllViews();
                                        load();
                                    }
                                });
                            }
                        });
                        linearLayout.addView(view);
                    }

                }else {

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
            Toast.makeText(this, "SELECTED", Toast.LENGTH_SHORT).show();

        }
    }


}