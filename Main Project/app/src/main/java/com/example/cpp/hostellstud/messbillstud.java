package com.example.cpp.hostellstud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class messbillstud extends AppCompatActivity {
ImageView messbill;
TextView messtext;
String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messbillstud);
        messbill=findViewById(R.id.messbill);
        messtext=findViewById(R.id.textmess);
        DocumentReference df = FirebaseFirestore.getInstance().collection("Hostellstud").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gender=documentSnapshot.getString("gender");
                if (gender.equals("MALE")){
                    StorageReference sf= FirebaseStorage.getInstance().getReference().child("Hostellstud/MESSBILL");
                    sf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri==null){
                                Toast.makeText(messbillstud.this, "NO MESS BILL ", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Picasso.get().load(uri).into(messbill);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            messtext.setText("NO MESS BILL FOUND");
                        }
                    });
                }
                if (gender.equals("FEMALE")){
                    StorageReference sf= FirebaseStorage.getInstance().getReference().child("girl_Hostellstud/MESSBILL");
                    sf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri==null){
                                Toast.makeText(messbillstud.this, "NO MESS BILL ", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Picasso.get().load(uri).into(messbill);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            messtext.setText("NO MESS BILL FOUND");
                        }
                    });
                }

            }
        });

    }
}