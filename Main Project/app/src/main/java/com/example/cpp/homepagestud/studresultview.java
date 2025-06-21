package com.example.cpp.homepagestud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cpp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class studresultview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studresultview);

        TextView pdfView=findViewById(R.id.resultimage);

        Button delete=findViewById(R.id.deleteresult);

        StorageReference sf= FirebaseStorage.getInstance().getReference().child("Testresult/"+getIntent().getStringExtra("DOCUMENT_ID")+"/"+getIntent().getStringExtra("DOCUMENT_DESC"));
        try {
            if (getIntent().getStringExtra("DELETE").equals("1")){
                delete.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }


        sf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                sf.delete();
                finish();
            }
        });
    }
}