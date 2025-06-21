package com.example.cpp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.cooperativeadmin.coperative;
import com.example.cpp.facultyadmin.Faculty;
import com.example.cpp.girlhostelladmin.Girls_Hostelladmin;
import com.example.cpp.homepagestud.Stud_HomePage;
import com.example.cpp.hostelladmin.Boys_Hostelladmin;
import com.example.cpp.libraryadmin.Library;
import com.example.cpp.principleadmin.Principal_HomePage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class openscreen extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "loginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    FirebaseAuth mAuth;
    TextView welcome;
    String Role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openscreen);
        FCMSend.SetServerKey("AAAAcQMei34:APA91bG620MfUBuxrjD3t4IZKQSDFFLjqEAcrsKQlSI0FMeCnH1Db7RR9pbTsBNC40iHjS23CLDnTn10ehyp5f1rlH6ERjA-YsoirsCyf6Wsg1rOWjFQn2fgOwtp8ySRh5vAX-LOzxp3");

        LottieAnimationView animationView = findViewById(R.id.imageView);
        animationView.setSpeed(5.0f);
        animationView.playAnimation();


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mAuth= FirebaseAuth.getInstance();

            FSelectUser(); // Directly navigate to the appropriate activity
        }else {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
    private void FSelectUser() {

        DocumentReference dr= FirebaseFirestore.getInstance().collection("User").document(mAuth.getUid());

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        DocumentReference df =FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
                        df.update("token",s);
                    }
                });
                // Set the isLoggedIn flag in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();

                Role=documentSnapshot.getString("Role");
                if (Role.equals("0")) {
                    Intent intent = new Intent(getApplicationContext(), Stud_HomePage.class);
                    startActivity(intent);
                }
                if (Role.equals("1")) {
                    Intent intent = new Intent(getApplicationContext(), Principal_HomePage.class);
                    startActivity(intent);
                }
                if (Role.equals("2")) {
                    Intent intent = new Intent(getApplicationContext(), Boys_Hostelladmin.class);
                    startActivity(intent);
                }
                if (Role.equals("3")) {
                    Intent intent = new Intent(getApplicationContext(), coperative.class);
                    startActivity(intent);
                }
                if (Role.equals("4")) {
                    Intent intent = new Intent(getApplicationContext(), Faculty.class);
                    startActivity(intent);
                }
                if (Role.equals("5")) {
                    Intent intent = new Intent(getApplicationContext(), Library.class);
                    startActivity(intent);
                }
                if (Role.equals("6")) {
                    Intent intent = new Intent(openscreen.this, Girls_Hostelladmin.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }

}