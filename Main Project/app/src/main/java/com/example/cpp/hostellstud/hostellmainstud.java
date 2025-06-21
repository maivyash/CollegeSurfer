package com.example.cpp.hostellstud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class hostellmainstud extends AppCompatActivity {

LinearLayout hostell,studlistmigrate,penaltyclick,mess;
CircleImageView admission,list,penalty,bill2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostellmainstud);


        //card layout images!
        admission=findViewById(R.id.Admission);
        list=findViewById(R.id.List);
        penalty=findViewById(R.id.Panelty);
        bill2=findViewById(R.id.Bill2);

        Glide.with(this).load(R.drawable.admission).into(admission);
        Glide.with(this).load(R.drawable.list).into(list);
        Glide.with(this).load(R.drawable.panelty).into(penalty);
        Glide.with(this).load(R.drawable.bill2).into(bill2);






        hostell=findViewById(R.id.hostelanddmission);
        studlistmigrate=findViewById(R.id.tostudentlistofhostell);
        mess=findViewById(R.id.mess);
        hostell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Hostellstud.class));
            }
        });
        studlistmigrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startActivity(new Intent(getApplicationContext(), listhostellroomview.class));
            }
        });
        penaltyclick=findViewById(R.id.penaltystudid);
        penaltyclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference df= FirebaseFirestore.getInstance().collection("Hostellstud").document(FirebaseAuth.getInstance().getUid());
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        {
                            try{
                                if (!(documentSnapshot.getString("confirmation").equals("1"))) {
                                    Toast.makeText(hostellmainstud.this, "You are not student of Hostel", Toast.LENGTH_SHORT).show();

                                }else{
                                    startActivity(new Intent(getApplicationContext(),penaltystudhostell.class));
                                }
                            }catch (Exception e){
                                Log.d("ERROR",e.toString());
                                Toast.makeText(hostellmainstud.this, "You are not student of Hostel", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hostellmainstud.this, "You are not Hostel Student", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference df= FirebaseFirestore.getInstance().collection("Hostellstud").document(FirebaseAuth.getInstance().getUid());
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        {
                            try{
                                if (!(documentSnapshot.getString("confirmation").equals("1"))) {
                                    Toast.makeText(hostellmainstud.this, "You are not student of Hostel", Toast.LENGTH_SHORT).show();

                                }else{
                                    startActivity(new Intent(getApplicationContext(), messbillstud.class));
                                }
                            }catch (Exception e){
                                Log.d("ERROR",e.toString());
                                Toast.makeText(hostellmainstud.this, "You are not student of Hostel", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hostellmainstud.this, "You are not student of Hostel", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}