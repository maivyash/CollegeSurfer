package com.example.cpp.principleadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.example.cpp.facultyadmin.Facultyadminaddtestresult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class principaladminviewflipper extends AppCompatActivity {
    TextInputEditText editText;
    Button select,upload;
    LinearLayout linearLayout;
    TextView textView;
    Uri photouri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principaladminviewflipper);
        StorageReference sf= FirebaseStorage.getInstance().getReference().child("Viewflipper");
        linearLayout=findViewById(R.id.viewflipperlinear);
        editText=findViewById(R.id.viewflipperlink);
        select=findViewById(R.id.viewflipperselect);
        upload=findViewById(R.id.viewflipperupload);
        textView=findViewById(R.id.noavlflipper);
        textView.setVisibility(View.GONE);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                startActivityForResult(Intent.createChooser(intent,"CHOOSE FLIPPER BANNER"),1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sf.listAll().addOnSuccessListener(listResult -> {
                    if (listResult.getItems().size()>=3){
                        Toast.makeText(principaladminviewflipper.this, "MAX LIMIT IS 3 REACHED", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (editText.getText().toString().isEmpty()){
                        Toast.makeText(principaladminviewflipper.this, "ENTER LINK", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (photouri==null){
                        Toast.makeText(principaladminviewflipper.this, "SELECT BANNER", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Patterns.WEB_URL.matcher(editText.getText().toString()).matches()){
                        Toast.makeText(getApplicationContext(), "Enter Valid URL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sf.child("/"+editText.getText().toString())
                    .putFile(photouri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(principaladminviewflipper.this, "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            }
        });
        sf.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().isEmpty()){
                textView.setVisibility(View.VISIBLE);
                return;
            }
            for (StorageReference reference:listResult.getItems()){
                View view=getLayoutInflater().inflate(R.layout.activity_principaladmin_viewflipperview,null);
                TextView link=view.findViewById(R.id.linkviewflipper);
                ImageView imageView=view.findViewById(R.id.imageviewflipper);
                Button del=view.findViewById(R.id.deleteviewflipper);

                link.setText(reference.getName().trim());
                reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                   Glide.with(getApplicationContext()).load(bytes).into(imageView);

                });
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(principaladminviewflipper.this, "DELETED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                linearLayout.addView(view);

            }





        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photouri = data.getData();
            ImageView imageView=findViewById(R.id.imageviewflippertemp);
            Glide.with(getApplicationContext()).load(photouri).into(imageView);

        }
    }

}