package com.example.cpp.girlhostelladmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Girls_Hostelladmin extends AppCompatActivity {
            Switch aSwitch;
    CircleImageView penalty1,Admissionreq,messsbill;
            DocumentReference df;
            LinearLayout penalty,request,messbill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girls_hostelladmin);
        aSwitch=findViewById(R.id.girlHostellonofXml);
        messbill=findViewById(R.id.girluploadmessbill);
        request=findViewById(R.id.girlreqhosaddlist);
        penalty=findViewById(R.id.girladminhostellpenalty);

        penalty1=findViewById(R.id.girlPenalty1);
        Admissionreq=findViewById(R.id.girladdmissionreq);
        messsbill=findViewById(R.id.girlMessbill);
        chk();
        Glide.with(this).load(R.drawable.penalty1).into(penalty1);
        Glide.with(this).load(R.drawable.addmissionreq).into(Admissionreq);
        Glide.with(this).load(R.drawable.messbill).into(messsbill);

        messbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            messbill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), girlhostelladminmessbill.class));
                }
            });

            }
        });


        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),girlhostelladminrequestpendinglist.class));


            }
        });

        penalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), girlhostelladminpenalty.class));
            }
        });
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()){
                    df=FirebaseFirestore.getInstance().collection("yesno").document("girlHostell");
                    Map<String,Object> Hostellyesno=new HashMap<>();
                    Hostellyesno.put("hostell","1");
                    df.set(Hostellyesno);
                    Toast.makeText(getApplicationContext(), "Hostel Admission Is Started", Toast.LENGTH_SHORT).show();
                    chk();
                }else {
                    df=FirebaseFirestore.getInstance().collection("yesno").document("girlHostell");
                    Map<String,Object> Hostellyesno=new HashMap<>();
                    Hostellyesno.put("hostell","0");
                    df.set(Hostellyesno);
                    Toast.makeText(getApplicationContext(), "Hostel Admission Is Closed", Toast.LENGTH_SHORT).show();
                    chk();
                }
            }
        });
    }


    private void chk() {
        try {
            TextView t1 = findViewById(R.id.girlt1);
            DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("girlHostell");
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists()){
                        return;
                    }
                    if (documentSnapshot.getString("hostell").equals("0")) {
                        t1.setText("ADMISSION IS CLOSED");
                        t1.setTextColor(ContextCompat.getColor(Girls_Hostelladmin.this, R.color.red));
                        aSwitch.setChecked(false);

                    } else if (documentSnapshot.getString("hostell").equals("1")) {
                        t1.setText("ADMISSION IS OPENED");
                        t1.setTextColor(ContextCompat.getColor(Girls_Hostelladmin.this, R.color.dark_green));
                        aSwitch.setChecked(true);

                    } else
                        Toast.makeText(Girls_Hostelladmin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Girls_Hostelladmin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.d("Girls_Hostelladmin",e.toString());
        }
    }
}