package com.example.cpp.hostelladmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Boys_Hostelladmin extends AppCompatActivity {
    Switch switchCompat;
    Boolean onof;
    FirebaseFirestore Fstore;
    DocumentReference df;
    CardView messbill;

    CircleImageView penalty1,Admissionreq,messsbill;

    LinearLayout adminhostelpenaltyclick,admissionclick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostell);

        //card view images
        penalty1=findViewById(R.id.Penalty1);
        Admissionreq=findViewById(R.id.addmissionreq);
        messsbill=findViewById(R.id.Messbill);


        Glide.with(this).load(R.drawable.penalty1).into(penalty1);
        Glide.with(this).load(R.drawable.addmissionreq).into(Admissionreq);
        Glide.with(this).load(R.drawable.messbill).into(messsbill);




        switchCompat =  findViewById(R.id.HostellonofXml);
        messbill=findViewById(R.id.uploadmessbill);
        Fstore=FirebaseFirestore.getInstance();
        chk();
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompat.isChecked()){
                    df=Fstore.collection("yesno").document("Hostell");
                    Map<String,Object> Hostellyesno=new HashMap<>();
                    Hostellyesno.put("hostell","1");
                    df.set(Hostellyesno);
                    Toast.makeText(getApplicationContext(), "Hostel Admission Is Started", Toast.LENGTH_SHORT).show();
                    chk();
                }else {
                    df=Fstore.collection("yesno").document("Hostell");
                    Map<String,Object> Hostellyesno=new HashMap<>();
                    Hostellyesno.put("hostell","0");
                    df.set(Hostellyesno);
                    Toast.makeText(getApplicationContext(), "Hostel Admission Is Closed", Toast.LENGTH_SHORT).show();
                    chk();
                }
            }
        });
        adminhostelpenaltyclick=findViewById(R.id.adminhostellpenalty);
        adminhostelpenaltyclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), adminhostellpenalty.class));

            }
        });
        admissionclick= findViewById(R.id.reqhosaddlist);
        admissionclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Pendingreqaddmision.class));

            }
        });
        messbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),uploadmessbilladmin.class));
            }
        });


    }

    private void chk() {
        TextView t1=findViewById(R.id.t1);
        DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("Hostell");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    return;
                }
                if (documentSnapshot.getString("hostell").equals("0")){
                    t1.setText("ADMISSION IS CLOSED");
                    t1.setTextColor(ContextCompat.getColor(Boys_Hostelladmin.this, R.color.red));
                    switchCompat.setChecked(false);

                } else if (documentSnapshot.getString("hostell").equals("1")) {
                t1.setText("ADMISSION IS OPENED");
                    t1.setTextColor(ContextCompat.getColor(Boys_Hostelladmin.this, R.color.dark_green));
                    switchCompat.setChecked(true);

                }else
                    Toast.makeText(Boys_Hostelladmin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Boys_Hostelladmin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}