package com.example.cpp.cooperativeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class coperative extends AppCompatActivity {
Switch aSwitch;
DocumentReference df;
LinearLayout linearLayout;
Button addstock;
Button refresh;

TextView t1;

FirebaseFirestore Fstore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coperative);
        addstock=findViewById(R.id.adminaddstock);
        linearLayout=findViewById(R.id.admincooperativeadmin);
        refresh=findViewById(R.id.refreshstock);
        t1=findViewById(R.id.storestatus);
        chk();
        aSwitch=findViewById(R.id.coperativeswitch);
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()){
                    df=Fstore.collection("yesno").document("Cooperative");
                    Map<String,Object> Cooperative=new HashMap<>();
                    Cooperative.put("cooperative","1");
                    df.set(Cooperative);
                    Toast.makeText(getApplicationContext(), "Store Is Open", Toast.LENGTH_SHORT).show();
                    FCMSend.Builder builder=new FCMSend.Builder("cooperative",true)
                            .setTitle("COOPERATIVE")
                                    .setBody("Cooperative Store is Opened");
                    builder.send();
                    chk();
                }else {
                    df=Fstore.collection("yesno").document("Cooperative");
                    Map<String,Object> Cooperative=new HashMap<>();
                    Cooperative.put("cooperative","0");
                    df.set(Cooperative);
                    Toast.makeText(getApplicationContext(), "Store is closed", Toast.LENGTH_SHORT).show();
                    FCMSend.Builder builder=new FCMSend.Builder("cooperative",true)
                            .setTitle("COOPERATIVE")
                            .setBody("Cooperative Store is Closed");
                    builder.send();
                    chk();
                }
            }
        });
        addstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), addstock_cooperative.class));
                return;
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
                FirebaseFirestore.getInstance().collection("cooperativestock").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                View view=getLayoutInflater().inflate(R.layout.activity_librarybooksview,null);
                                TextView name=view.findViewById(R.id.nameoflib);
                                TextView number=view.findViewById(R.id.numberoflib);
                                TextView edition=view.findViewById(R.id.editionoflib);
                                TextView genrea=view.findViewById(R.id.genreaoflib);
                                TextView writer=view.findViewById(R.id.writeroflib);
                                Switch compactswitch=view.findViewById(R.id.stockoflib);
                                Button delete=view.findViewById(R.id.deleteoflib);
                                name.setText("NAME: "+documentSnapshot.getString("name"));
                                number.setText("PRICE: "+documentSnapshot.getString("price"));
                                edition.setVisibility(View.GONE);
                                genrea.setVisibility(View.GONE);
                                writer.setVisibility(View.GONE);


                                if (documentSnapshot.getString("inStock").equals("1")){
                                    compactswitch.setChecked(true);
                                }else {
                                    compactswitch.setChecked(false);
                                }
                                compactswitch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (compactswitch.isChecked()){
                                            FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).update("inStock","1");
                                        }else {
                                            FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).update("inStock","0");

                                        }
                                    }
                                });

                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                linearLayout.addView(view);
                            }

                        }
                    }
                });

            }
        });
        linearLayout.removeAllViews();
        FirebaseFirestore.getInstance().collection("cooperativestock").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        View view=getLayoutInflater().inflate(R.layout.activity_librarybooksview,null);
                        TextView name=view.findViewById(R.id.nameoflib);
                        TextView number=view.findViewById(R.id.numberoflib);
                        TextView edition=view.findViewById(R.id.editionoflib);
                        TextView genrea=view.findViewById(R.id.genreaoflib);
                        TextView writer=view.findViewById(R.id.writeroflib);
                        Switch compactswitch=view.findViewById(R.id.stockoflib);
                        Button delete=view.findViewById(R.id.deleteoflib);
                        number.setText("NAME: "+documentSnapshot.getString("name"));
                        name.setText("PRICE: "+documentSnapshot.getString("price"));
                        edition.setVisibility(View.GONE);
                        genrea.setVisibility(View.GONE);
                        writer.setVisibility(View.GONE);


                        if (documentSnapshot.getString("inStock").equals("1")){
                            compactswitch.setChecked(true);
                        }else {
                            compactswitch.setChecked(false);
                        }
                        compactswitch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (compactswitch.isChecked()){
                                    FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).update("inStock","1");
                                }else {
                                    FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).update("inStock","0");

                                }
                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseFirestore.getInstance().collection("cooperativestock").document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        linearLayout.addView(view);
                    }

                }
            }
        });

    }
    private void chk() {

        DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("Cooperative");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    return;
                }
                if (documentSnapshot.getString("cooperative").equals("0")){
                    t1.setText("STORE IS CLOSED");
                    t1.setTextColor(ContextCompat.getColor(coperative.this, R.color.red));
                    aSwitch.setChecked(false);

                } else if (documentSnapshot.getString("cooperative").equals("1")) {
                    t1.setText("STORE IS OPENED");
                    t1.setTextColor(ContextCompat.getColor(coperative.this, R.color.dark_green));
                    aSwitch.setChecked(true);

                }else
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}