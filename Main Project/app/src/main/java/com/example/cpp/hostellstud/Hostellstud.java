package com.example.cpp.hostellstud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.homepagestud.Stud_HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.FieldIndex;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Hostellstud extends AppCompatActivity {
DocumentReference df;
FirebaseFirestore FStore;
FirebaseAuth mAuth;
TextView name,eno,year,branch;
Button submitapplicationhostell,photo,marksheet,ARC,Adhar;
String uid,gender;
Uri photouri,marksheeturi,ARCuri,Adharuri;
StorageReference storageReference;
EditText getDENid,getadharnumber,getpermanentaddress,getcurrentaddress,getmobilenumber;
int ERROR=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostellstud);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getUid();
        submitapplicationhostell=findViewById(R.id.Hostellenroll);
        name=findViewById(R.id.Hostellstudname);
        eno=findViewById(R.id.Hostellstudeno);
        year=findViewById(R.id.Hostellstudaddmisionyear);
        branch=findViewById(R.id.Hostellstudcourse);
        photo=findViewById(R.id.getphoto);
        marksheet= findViewById(R.id.getMarksheet);
        Adhar=findViewById(R.id.getadhar);
        ARC = findViewById(R.id.getARC);
        getDENid=findViewById(R.id.DENid);
        getadharnumber=findViewById(R.id.adharnumber);
        getpermanentaddress=findViewById(R.id.parmanentaddress);
        getcurrentaddress=findViewById(R.id.currentaddress);
        getmobilenumber=findViewById(R.id.mobileno);
        storageReference= FirebaseStorage.getInstance().getReference();
        FStore=FirebaseFirestore.getInstance();

        DocumentReference df4=FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
        df4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gender=documentSnapshot.getString("gender");
                DocumentReference df3=FirebaseFirestore.getInstance().collection("yesno").document("Hostell");
                df3.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (gender.equals("MALE")){
                            if (!documentSnapshot.getString("hostell").equals("1")){
                                Toast.makeText(Hostellstud.this, "Hostel admissions are classed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
                                return;
                            }
                        } else if (gender.equals("FEMALE")) {
                            FirebaseFirestore.getInstance().collection("yesno").document("girlHostell").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (!documentSnapshot.getString("hostell").equals("1")){
                                        Toast.makeText(Hostellstud.this, "Hostel admissions are classed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
                                        return;
                                    }
                                }
                            });
                        }

                    }
                });
            }
        });


        if (uid==null){
            //checking if system is able to get User's UID
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), Stud_HomePage.class));
            finish();
            return;
        }
        CollectionReference cf= FirebaseFirestore.getInstance().collection("Hostellstud");
        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        if (documentSnapshot.getId().equals(FirebaseAuth.getInstance().getUid())){
                            Toast.makeText(getApplicationContext(),"You already registered to Hostel Admission",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
                            finish();
                            return;
                        }
                    }
                }
            }
        });


        //getting primary data of user  like name branch admission year and E.no
        df=FStore.collection("User").document(mAuth.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (Objects.equals(documentSnapshot.getString("HostellUser"), "1")){
//                    Toast.makeText(getApplicationContext(),"You already registered to Hostel Admission",Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
//                    finish();
//                    return;
//                }
//                if (documentSnapshot.getString("HostellUser")==null){
//                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
//                    finish();
//                    return;
//                }
                //displaying the primary information in XML
                name.setText("Name - "+documentSnapshot.getString("Fullname"));
                eno.setText("Enrolment Number - "+documentSnapshot.getString("Enrolmentnumber"));
                year.setText("Addmision Year - "+documentSnapshot.getString("AddmisionYear"));
                branch.setText("Branch - "+documentSnapshot.getString("Branch"));
                submitapplicationhostell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //following is checking fields for is  Empty
                        if (getDENid.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter DEN id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (getpermanentaddress.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Permanent Address", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (getcurrentaddress
                                .getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Enter Current Address", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (getadharnumber.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Addhar Number", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (getmobilenumber.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (!(getmobilenumber.getText().toString().length()==10)){
                            Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (!(getadharnumber.getText().toString().length()==12)){
                            Toast.makeText(getApplicationContext(), "Enter Valid Addhar Number", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if ((getpermanentaddress.getText().toString().length()>50)){
                            Toast.makeText(getApplicationContext(), "Permanent Address should not be greater than 50", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if ((getcurrentaddress.getText().toString().length()>50)){
                            Toast.makeText(getApplicationContext(), "Current Address should not be greater than 50", Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        if (photouri==null){
                            //testing if photo is selected properly
                            Toast.makeText(getApplicationContext(), "Something went wrong during selecting PHOTO", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (marksheeturi==null){
                            //testing if marksheet is selected properly
                            Toast.makeText(getApplicationContext(), "Something went wrong during Marksheet Selecting", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (ARCuri==null){
                            //testing if ARC is selected properly
                            Toast.makeText(getApplicationContext(), "Something went wrong during ARC Selecting", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        if (Adharuri==null){
                            //testing if Adhar is selected properly
                            Toast.makeText(getApplicationContext(), "Something went wrong during Adhar Card Selecting", Toast.LENGTH_SHORT).show();
                            return ;
                        }





                        //uploadiing photo
                        StorageReference fileReferencephoto=storageReference.child("hostelladdmisiondoc/"+FirebaseAuth.getInstance().getUid()+"/photo.jpeg");
                        fileReferencephoto.putFile(photouri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Hostellstud.this, "Something went wrong when uploading files to database", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });

                        //uploading marksheet
                        StorageReference fileReferencemarksheet=storageReference.child("hostelladdmisiondoc/"+FirebaseAuth.getInstance().getUid()+"/marksheet.pdf");
                        fileReferencemarksheet.putFile(marksheeturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Hostellstud.this, "Something went wrong when uploading files to database", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        //uploading ARC

                        StorageReference fileReferenceARC=storageReference.child("hostelladdmisiondoc/"+FirebaseAuth.getInstance().getUid()+"/ARC.pdf");
                        fileReferenceARC.putFile(ARCuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Hostellstud.this, "Something went wrong when uploading files to database", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });

                        //uploading adhar

                        StorageReference fileReferenceadhar=storageReference.child("hostelladdmisiondoc/"+FirebaseAuth.getInstance().getUid()+"/Adhar.pdf");
                        fileReferenceadhar.putFile(Adharuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Hostellstud.this, "Something went wrong when uploading files to database", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                        //submitting info. to Hostellstud Database
                        Map<String,Object> userinfo=new HashMap<>();
                        userinfo.put("HostellUser","1");
                        df.update(userinfo);
                        userinfo.clear();
                        userinfo.put("Fullname",documentSnapshot.getString("Fullname"));
                        userinfo.put("AddmisionYear",documentSnapshot.getString("AddmisionYear"));
                        userinfo.put("Enrolmentnumber",documentSnapshot.getString("Enrolmentnumber"));
                        userinfo.put("Branch",documentSnapshot.getString("Branch"));
                        userinfo.put("DENid",getDENid.getText().toString());
                        userinfo.put("permanentaddress",getpermanentaddress.getText().toString());
                        userinfo.put("currentaddress",getcurrentaddress.getText().toString());
                        userinfo.put("Adharnumber",getadharnumber.getText().toString());
                        userinfo.put("mobilenumber",getmobilenumber.getText().toString());
                        userinfo.put("gender",documentSnapshot.getString("gender"));
                        userinfo.put("confirmation","0");
                        FirebaseFirestore.getInstance().collection("Hostellstud").document(FirebaseAuth.getInstance().getUid()).set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Application submitted successfully ",Toast.LENGTH_LONG).show();
                            }
                        });


                        startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
                        finish();
                    }

                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Stud_HomePage.class));
                finish();
            }
        });



        //SELECTING OF FILES  HERE BELOW BY LISTENERS



        //this is selecting the photo from files manager of phone
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photointent= new Intent();
                photointent.setType("image/jpeg");
                photointent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photointent,"CHOOSE PASSPORT SIZE PHOTO"),1);
                //
                //
                //


            }
        });
        //this is selecting the marksheet from files manager of phone
        marksheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent marksheetintent=new Intent();
                marksheetintent.setType("application/pdf");
                marksheetintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(marksheetintent,"CHOOSE MARKSHEET"),2);
                //
                //
                //


            }
        });
        //this is selecting the ARC from files manager of phone
        ARC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent arcintent=new Intent();
                arcintent.setType("application/pdf");
                arcintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(arcintent,"CHOOSE MARKSHEET"),3);
                //
                //
                //


            }
        });
        //this is selecting the Addhar Card from files manager of phone
        Adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adharintent=new Intent();
                adharintent.setType("application/pdf");
                adharintent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(adharintent,"CHOOSE AADHARCARD"),4 );//request code help to identfy file on onActivityResult method
            }
        });
    }

        //this onActivityResult mathod used to make uri..s of files using request codes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this is getting photo and making its URI
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photouri = data.getData();
        }
        //this is getting marksheet and making its URI
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            marksheeturi = data.getData();
        }
        //this is getting ARC and making its URI
        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ARCuri = data.getData();
        }
        //this is getting Adhar and making its URI
        if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Adharuri = data.getData();
        }



    }
}