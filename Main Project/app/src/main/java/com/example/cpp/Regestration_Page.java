
package com.example.cpp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Regestration_Page extends AppCompatActivity  {
EditText rname,rpass,rcpass,rmail,reno,ryear;
Button register;
    String email, password;
    String SRname,SRreno,SRyear,SRmail,SRCpass,text,gender;
    FirebaseAuth mAuth;
    FirebaseFirestore fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration_page);
//spinner code
        //for Branch
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //for genders
        Spinner spinner1 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Genders,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

//spinner code ends
        rname=findViewById(R.id.xmlname);
        rpass=findViewById(R.id.rpass);
        reno=findViewById(R.id.xmlreno);
        rcpass=findViewById(R.id.rcpass);
        rmail=findViewById(R.id.rmail);
        ryear=findViewById(R.id.xmlryear);
        register=findViewById(R.id.rbtn);
        fb=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                text =adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView1, View view, int j, long k) {
                gender=adapterView1.getItemAtPosition(j).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registeruser();

            }
        });
            }
    public void registeruser(){
        // Take the value of two edit texts in Strings

        email=rmail.getText().toString();
        password = rpass.getText().toString();
        SRname=rname.getText().toString();
        SRreno=reno.getText().toString();
        SRyear=ryear.getText().toString();
        SRmail=rmail.getText().toString();
        SRCpass=rcpass.getText().toString();


        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(SRCpass)) {
            Toast.makeText(getApplicationContext(), "Please enter confirm password!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(SRname)) {
            Toast.makeText(getApplicationContext(), "Please enter Name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(SRreno)) {
            Toast.makeText(getApplicationContext(), "Please enter Enrolment number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(SRyear)) {
            Toast.makeText(getApplicationContext(), "Please enter Addmision Year!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(rmail.getText().toString().contains("@"))){
            Toast.makeText(this, "Enter Valid E-Mail ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rpass.getText().toString().length()<8){
            Toast.makeText(this, "Password must be 8 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        SRmail=SRmail.toLowerCase();
        SRname=SRname.toUpperCase();
        if(Integer.parseInt(SRyear)<2009||Integer.parseInt(SRyear)>2024){
            Toast.makeText(getApplicationContext(),"Enter valid year",Toast.LENGTH_LONG).show();
            return;
        }
        SRmail=SRmail.replaceAll("\\s","");
        SRCpass=SRCpass.replaceAll("\\s","");
        password=password.replaceAll("\\s","");
        if(SRCpass.equals(password)) {

            // create new user or register new user



           Cheakuser();
             }else {
            Toast.makeText(getApplicationContext(), "Password and confirm Password is not matched", Toast.LENGTH_LONG).show();

        }
    }
void Createuser(){
    mAuth.createUserWithEmailAndPassword(SRmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                FireStore();
                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Email Verification link is sent to email",Toast.LENGTH_LONG).show();
                    }
                });
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();


            } else {

                Toast.makeText(getApplicationContext(), "Registration failed!!"+ " Please try again later", Toast.LENGTH_LONG).show();


            }
        }
    });
}


    public  void FireStore(){

        DocumentReference db=fb.collection("User").document(mAuth.getUid());//getting the uid and storing it to firebase
        Map<String,Object> userinfo=new HashMap<>();
        userinfo.put("Fullname",SRname);
        userinfo.put("password",password);
        userinfo.put("Enrolmentnumber",SRreno);
        userinfo.put("AddmisionYear",SRyear);
        userinfo.put("Role","0");
        userinfo.put("Branch",text);
        userinfo.put("gender",gender);
        userinfo.put("HostellUser","0");
        userinfo.put("lostreport",0);
        db.set(userinfo);
    }


void Cheakuser () {
    CollectionReference cf = fb.collection("User");
    cf.whereEqualTo("Enrolmentnumber", SRreno).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enrolment number iis already present in database", Toast.LENGTH_LONG).show();


                } else {
                    Createuser();
                }
            } else {
                Toast.makeText(getApplicationContext(), "something went wrong ", Toast.LENGTH_LONG).show();

            }
        }
    });


}
}