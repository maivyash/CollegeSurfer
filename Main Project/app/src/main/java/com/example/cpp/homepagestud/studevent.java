package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.principleadmin.principaladminevent;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class studevent extends AppCompatActivity {
TextView textView;
LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studevent);
        linearLayout=findViewById(R.id.eventlinearstud);
        textView=findViewById(R.id.noevent);

        load();

    }
    private void load() {
        FirebaseStorage.getInstance().getReference().child("event/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size()>0){
                    textView.setText("ACTIVE EVENTS ");
                    textView.setTextColor(ContextCompat.getColor(studevent.this, R.color.dark_green));

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
                                String eventName = "EVENT NAME : " + storageMetadata.getCustomMetadata("name");
                                String eventDesc = "EVENT DESCRIPTION : " + storageMetadata.getCustomMetadata("desc");
                                String eventLink = "EVENT LINK : " + storageMetadata.getCustomMetadata("link");

                                SpannableString spannableEventName = new SpannableString(eventName);
                                SpannableString spannableEventDesc = new SpannableString(eventDesc);
                                SpannableString spannableEventLink = new SpannableString(eventLink);


                                spannableEventName.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(12, eventName.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableEventDesc.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(19, eventDesc.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableEventLink.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(12, eventLink.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                int linkStart = eventLink.indexOf("www.");
                                if (linkStart != -1) {
                                    String linkSubstring = eventLink.substring(linkStart);

                                    spannableEventLink = new SpannableString(eventLink);
                                    spannableEventLink.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(12, eventLink.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableEventLink.setSpan(new ForegroundColorSpan(Color.BLUE), linkStart, linkStart + linkSubstring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    name.setText(spannableEventName);
                                    desc.setText(spannableEventDesc);
                                    link.setText(spannableEventLink);
                                } else {

                                    name.setText(eventName);
                                    desc.setText(eventDesc);
                                    link.setText(eventLink);
                                }

                                name.setText(spannableEventName);
                                desc.setText(spannableEventDesc);
                                link.setText(spannableEventLink);


                                // make the first 20 char of aboove set text name,desc,link bold

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
                        delete.setVisibility(View.GONE);
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
}