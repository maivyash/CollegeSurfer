package com.example.cpp.girlhostelladmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.example.cpp.hostelladmin.Boys_Hostelladmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class girlhostelladminmessbill extends AppCompatActivity {
    Button selectbill,uploadbill;
    Uri photouri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girlhostelladminmessbill);
        selectbill=findViewById(R.id.girlselectbill);
        uploadbill=findViewById(R.id.girluploadbill);
        selectbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE BILL"),1);
            }
        });
        uploadbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photouri==null){
                    Toast.makeText(girlhostelladminmessbill.this, "NO photo selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                StorageReference sf= FirebaseStorage.getInstance().getReference();
                StorageReference file=sf.child("girl_Hostellstud/MESSBILL");
                file.putFile(photouri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(girlhostelladminmessbill.this, "SUCCESFULLY UPLOADED MESS BILL", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Boys_Hostelladmin.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(girlhostelladminmessbill.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photouri = data.getData();
            ImageView i1=findViewById(R.id.messbillupload);
            Glide.with(this).load(photouri).into(i1);
        }
    }
}