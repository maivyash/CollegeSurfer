package com.example.cpp.hostellstud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class penaltystudhostell extends AppCompatActivity {
TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penaltystudhostell);
        t1 = findViewById(R.id.penaltystudamt);
        t2 = findViewById(R.id.penaltystuddesc);
        DocumentReference df= FirebaseFirestore.getInstance().collection("Hostellstud").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    if (documentSnapshot.getString("Description").equals("null")||documentSnapshot.getString("penalty").equals("null")){
                        Toast.makeText(penaltystudhostell.this, "No Penalty", Toast.LENGTH_SHORT).show();
                        t1.setText("No Penalty on Your Name");
                        finish();
                        return;
                    }
                    t1.setText("Penalty Amount : " + documentSnapshot.getString("penalty"));
                    t2.setText("Description : " + documentSnapshot.getString("Description"));
                }catch (Exception e){
                    Toast.makeText(penaltystudhostell.this, "NO PENALTY FOUND ON YOUR NAME", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR",e.toString());

                }

            }
        });


    }
}