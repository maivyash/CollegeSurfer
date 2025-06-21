package com.example.cpp.lostfoundstud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class userscurrentlostreports extends AppCompatActivity {
    LinearLayout l1;
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userscurrentlostreports);
        t1=findViewById(R.id.textonyourreport);
        l1=findViewById(R.id.usersreports);
        b1=findViewById(R.id.deletereportbtn);
       currentreport();

    }

    private void currentreport() {
       TextView tname,tdate,tdesc,tmobobile,taddress;
       tname = findViewById(R.id.textName);
       taddress = findViewById(R.id.textaddress);
       tdate = findViewById(R.id.textDate);
       tmobobile = findViewById(R.id.textmo);
       tdesc= findViewById(R.id.textDesc);
        DocumentReference df = FirebaseFirestore.getInstance().collection("lostreport").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    l1.setVisibility(View.GONE);
                }
                if (documentSnapshot.exists()){
                    t1.setVisibility(View.GONE);
                    // this  will show the user's own report in page
                    // this will delete the report
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showreports();

                        }
                    });
                }else{
                    return;
                }

                tname.setText("Name - "+documentSnapshot.getString("Name"));
                taddress.setText("Address - "+documentSnapshot.getString("address"));
                tdate.setText("Lost Date - "+documentSnapshot.getString("date"));
                tdesc.setText("Item Description - "+documentSnapshot.getString("description"));
                tmobobile.setText("Contact Number - "+documentSnapshot.getString("Mobileno"));


            }
        });
    }

    private void showreports() {

       // this will delete  the  main report data
        FirebaseFirestore.getInstance().collection("lostreport").document(FirebaseAuth.getInstance().getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(userscurrentlostreports.this, "Deleted report successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),lostandfoundstud.class));
                FirebaseStorage.getInstance().getReference().child("lostandfound/"+FirebaseAuth.getInstance().getUid()+"/photoloastobject.jpeg").delete();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(userscurrentlostreports.this, "something went wording", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),lostandfoundstud.class));
                finish();
            }
        });
    }
}
