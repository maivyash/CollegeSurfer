package com.example.cpp.girlhostelladmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.R;
import com.example.cpp.hostelladmin.boyshostelladmindocument;
import com.example.cpp.hostelladmin.hostelladminmodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class girlhostelladminrequestpendinglist extends AppCompatActivity {
    LinearLayout l1;
    CollectionReference cf;
    TextView t1;
    ImageView imageView;
    Button Accept,Reject,Document;
    TextView setDENid,setpermanentaddress,setcurrentaddress,setadharnumber,setmobilenumber,eno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girlhostelladminrequestpendinglist);
        l1=findViewById(R.id.girllinearforreqlisthostelladmin);
        t1=findViewById(R.id.girltext);
        t1.setVisibility(View.GONE);
        cf= FirebaseFirestore.getInstance().collection("Hostellstud");
        load();

    }
    void load(){
        cf.whereEqualTo("gender","FEMALE").whereEqualTo("confirmation","0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    t1.setVisibility(View.VISIBLE);
                    l1.clearAnimation();
                    return;
                }

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    View view =getLayoutInflater().inflate(R.layout.activity_document_listreqadd_admin,null);
                    TextView name=view.findViewById(R.id.name);
                    TextView branch=view.findViewById(R.id.branch);
                    TextView addmisonyear=view.findViewById(R.id.addmisonyear);
                    EditText roomnumber =view.findViewById(R.id.alotroomnumber);
                    setadharnumber=view.findViewById(R.id.adharnumber);
                    setcurrentaddress=view.findViewById(R.id.currentaddress);
                    setmobilenumber=view.findViewById(R.id.mobileno);
                    setpermanentaddress=view.findViewById(R.id.permanentaddress);
                    setDENid=view.findViewById(R.id.DENid);
                    eno=view.findViewById(R.id.eno);
                    imageView=view.findViewById(R.id.imagestud);

                    Accept=view.findViewById(R.id.accept);
                    Reject=view.findViewById(R.id.reject);
                    Document=view.findViewById(R.id.doc);
                    hostelladminmodel model=documentSnapshot.toObject(hostelladminmodel.class);

                    StorageReference sf= FirebaseStorage.getInstance().getReference();
                    StorageReference file=sf.child("hostelladdmisiondoc/"+documentSnapshot.getId()+"/photo.jpeg");
                    name.setText("NAME : "+model.getFullname());
                    addmisonyear.setText("ADMISSION YEAR : "+model.getAddmisionYear());
                    branch.setText("BRANCH : "+model.getBranch());
                    setpermanentaddress.setText("PERMANENT ADDRESS : "+documentSnapshot.getString("permanentaddress"));
                    setadharnumber.setText("ADDHAR NUMBER : "+documentSnapshot.getString("Adharnumber"));
                    setmobilenumber.setText("MOBILE NUMBER : "+documentSnapshot.getString("mobilenumber"));
                    setcurrentaddress.setText("CURRENT ADDRESS : "+documentSnapshot.getString("currentaddress"));
                    setDENid.setText("DEN ID : "+documentSnapshot.getString("DENid"));
                    eno.setText("ENROLLMENT NUMBER : "+documentSnapshot.getString("Enrolmentnumber"));
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(girlhostelladminrequestpendinglist.this, "not able to load "+documentSnapshot.getString("Fullname")+"'s image", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (roomnumber.getText().toString().isEmpty()){
                                Toast.makeText(girlhostelladminrequestpendinglist.this, "Enter Room number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Map<String,Object> m1=new HashMap<>();
                            m1.put("confirmation","1");
                            m1.put("roomnumber",roomnumber.getText().toString());
                            DocumentReference df=FirebaseFirestore.getInstance().collection("Hostellstud").document(documentSnapshot.getId());
                            df.update(m1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseFirestore.getInstance().collection("User").document(documentSnapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            FCMSend.Builder fcmSend=new FCMSend.Builder(documentSnapshot.getString("token")).setTitle("HOSTEL").setBody("Your Hostel Admission Request is Accepted");
                                            fcmSend.send();
                                        }
                                    });

                                    Toast.makeText(girlhostelladminrequestpendinglist.this, "SUCCESSFULLY ADMITTED", Toast.LENGTH_SHORT).show();


                                }
                            });

                        }
                    });
                    Reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DocumentReference df1 = FirebaseFirestore.getInstance().collection("Hostellstud").document(documentSnapshot.getId());
                            df1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseFirestore.getInstance().collection("User").document(documentSnapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            FCMSend.Builder fcmSend=new FCMSend.Builder(documentSnapshot.getString("token")).setTitle("HOSTEL").setBody("Your Hostel Admission Request is Rejected");
                                            fcmSend.send();
                                        }
                                    });

                                    Toast.makeText(girlhostelladminrequestpendinglist.this, "SUCCESSFULLY DELETED", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });

                    Document.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =new Intent(getApplicationContext(), boyshostelladmindocument.class);
                            intent.putExtra("DOCUMENT_ID",documentSnapshot.getId());
                            startActivity(intent);

                        }
                    });
                    l1.addView(view);
                }
            }
        });
    }
}