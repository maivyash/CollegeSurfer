package com.example.cpp.hostellstud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.Usermodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class listhostellroomview extends AppCompatActivity {
int i=0;
String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listhostellroomview);

        //underline text
        TextView textView = findViewById(R.id.liststud);
        String content = "Student List";
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        textView.setText(spannableString);


        DocumentReference df =FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gender=documentSnapshot.getString("gender");

                CollectionReference c1 = FirebaseFirestore.getInstance().collection("Hostellstud");

                LinearLayout l1 =findViewById(R.id.linearlayoutofhostellstudlist);
                c1.whereEqualTo("confirmation","1").whereEqualTo("gender",gender).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(listhostellroomview.this, "No Request Generated", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),hostellmainstud.class));
                            TextView t1=findViewById(R.id.liststud);
                            t1.setText("NO LIST FOUND");

                            return;
                        }
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            Usermodel model=documentSnapshot.toObject(Usermodel.class);
                            View view =  getLayoutInflater().inflate(R.layout.activity_documentlststudhostell,null);
                            TextView t1=view.findViewById(R.id.name);
                            TextView t2= view.findViewById(R.id.eno);
                            TextView t3=view.findViewById(R.id.branch);
                            TextView t4 =view.findViewById(R.id.year);
                            TextView t5=view.findViewById(R.id.roomnumber);
                            t1.setText("Full Name : "+model.getFullname());
                            t2.setText("Enrollment Number : "+model.getEnrolmentnumber());
                            t3.setText("Branch : "+model.getBranch());
                            t4.setText("Addmision Year : "+documentSnapshot.getString("AddmisionYear"));
                            t5.setText("Room Number : "+documentSnapshot.getString("roomnumber"));
                            l1.addView(view);
                            i++;



                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(listhostellroomview.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),hostellmainstud.class));
                        finish();
                    }
                });
            }
        });




    }
}