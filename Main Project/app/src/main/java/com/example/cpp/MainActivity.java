package com.example.cpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.cooperativeadmin.coperative;
import com.example.cpp.facultyadmin.Faculty;
import com.example.cpp.girlhostelladmin.Girls_Hostelladmin;
import com.example.cpp.homepagestud.Stud_HomePage;
import com.example.cpp.hostelladmin.Boys_Hostelladmin;
import com.example.cpp.libraryadmin.Library;
import com.example.cpp.principleadmin.Principal_HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
EditText mail,pass;
TextView registerpg,reset;
Button login;

    String smail,spass,uid,Role;
    FirebaseAuth mAuth;
    FirebaseFirestore fb;
    Stud_HomePage homePage;
    private static final String SHARED_PREF_NAME = "loginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mail = findViewById(R.id.editTextText);
        pass = findViewById(R.id.editTextpassword);
        //eno=findViewById(R.id.editTextEnrollment);
        registerpg = findViewById(R.id.textView);
        login = findViewById(R.id.button);
        //Seno=eno.getText().toString();
        homePage = new Stud_HomePage();
        reset = findViewById(R.id.textView2);


        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            FSelectUser(); // Directly navigate to the appropriate activity
        }else {
            registerpg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Regestration_Page.class);
                    startActivity(intent);

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loginuserAcc();
                }
            });
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetpass();

                }
            });
        }
        mAuth = FirebaseAuth.getInstance();

        }
    private void resetpass() {
        smail=mail.getText().toString();

        if (TextUtils.isEmpty(smail)) {
            mail.setError("Enter Mail..");
            return;
        }

        smail=smail.toLowerCase();
        smail=smail.replaceAll("\\s","");

        mAuth.sendPasswordResetEmail(smail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Reset instructions to sent to your email",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void loginuserAcc(){
        smail=mail.getText().toString();
        spass=pass.getText().toString();
        smail=smail.toLowerCase();
        smail=smail.replaceAll("\\s","");
        spass=spass.replaceAll("\\s","");
        // validations for input email and password
        if (TextUtils.isEmpty(smail)) {
            mail.setError("Enter E-Mail");
            return;
        }
        if (TextUtils.isEmpty(spass)) {
            Toast.makeText(getApplicationContext(),"Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(smail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   if ( mAuth.getCurrentUser().isEmailVerified()!=true){

                       Toast.makeText(getApplicationContext(),"Verify email",Toast.LENGTH_LONG).show();
                       return;
                   }

                    Toast.makeText(MainActivity.this,"LOGIN Succesed!!!!",Toast.LENGTH_LONG).show();

                    FSelectUser();

                }else{

                    String errorMessage = "LOGIN FAILED!!";

                    if (task.getException() != null) {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            errorMessage = "User not registered. Please sign up!";
                        } else if (errorCode.equals("ERROR_WRONG_PASSWORD")) {
                            errorMessage = "Incorrect password. Please try again!";
                        }
                    }

                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void FSelectUser() {
        mAuth=FirebaseAuth.getInstance();
        try {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    DocumentReference df = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
                    df.update("token", s);
                }
            });
        }catch (Exception e){
            Log.d("EXCEPTION",e.toString());
        }
        DocumentReference dr=FirebaseFirestore.getInstance().collection("User").document(mAuth.getUid());
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Set the isLoggedIn flag in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.apply();
                Role=documentSnapshot.getString("Role");
                if (Role.equals("0")) {
                    Intent intent = new Intent(MainActivity.this, Stud_HomePage.class);
                    startActivity(intent);
                }
                if (Role.equals("1")) {
                    Intent intent = new Intent(MainActivity.this, Principal_HomePage.class);
                    startActivity(intent);
                }
                if (Role.equals("2")) {
                    Intent intent = new Intent(MainActivity.this, Boys_Hostelladmin.class);
                    startActivity(intent);
                }
                if (Role.equals("3")) {
                    Intent intent = new Intent(MainActivity.this, coperative.class);
                    startActivity(intent);
                }
                if (Role.equals("4")) {
                    Intent intent = new Intent(MainActivity.this, Faculty.class);
                    startActivity(intent);
                }
                if (Role.equals("5")) {
                    Intent intent = new Intent(MainActivity.this, Library.class);
                    startActivity(intent);
                }
                if (Role.equals("6")) {
                    Intent intent = new Intent(MainActivity.this, Girls_Hostelladmin.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }
    }


