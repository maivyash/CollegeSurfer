package com.example.cpp.libraryadmin;

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

public class Library extends AppCompatActivity {
Switch aSwitch;
LinearLayout linearLayout;
FirebaseFirestore Fstore=FirebaseFirestore.getInstance();
DocumentReference df;
Button addbook,refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        aSwitch=findViewById(R.id.Libraryswitch);
        addbook=findViewById(R.id.adminaddbookbtn);
        linearLayout=findViewById(R.id.adminlibrarylinear);
        refresh=findViewById(R.id.refresh);
        chk();
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()){
                    df=Fstore.collection("yesno").document("Library");
                    Map<String,Object> Library=new HashMap<>();
                    Library.put("library","1");
                    df.set(Library);
                    Toast.makeText(getApplicationContext(), "Library Is Open", Toast.LENGTH_SHORT).show();
                    FCMSend.Builder builder=new FCMSend.Builder("library",true)
                            .setTitle("LIBRARY")
                            .setBody("LIBRARY is Opened");
                    builder.send();
                    chk();
                }else {
                    df=Fstore.collection("yesno").document("Library");
                    Map<String,Object> Library=new HashMap<>();
                    Library.put("library","0");
                    df.set(Library);
                    Toast.makeText(getApplicationContext(), "Library is closed", Toast.LENGTH_SHORT).show();
                    FCMSend.Builder builder=new FCMSend.Builder("library",true)
                            .setTitle("LIBRARY")
                            .setBody("LIBRARY is Closed");
                    builder.send();
                    chk();
                }
            }
        });

    }

    private void chk() {
        TextView t1=findViewById(R.id.librarystatus);
        DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("Library");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    return;
                }
                if (documentSnapshot.getString("library").equals("0")){
                    t1.setText("Library IS CLOSED");
                    t1.setTextColor(ContextCompat.getColor(Library.this, R.color.red));
                    aSwitch.setChecked(false);

                } else if (documentSnapshot.getString("library").equals("1")) {
                    t1.setTextColor(ContextCompat.getColor(Library.this, R.color.dark_green));
                    t1.setText("Library IS OPENED");
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
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), libraryadminaddbook.class));
                return;
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
                FirebaseFirestore.getInstance().collection("librarybooks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                number.setText("NUMBER: "+documentSnapshot.getString("bookno"));
                                edition.setText("EDITION: "+documentSnapshot.getString("edition"));
                                genrea.setText("GENREA: "+documentSnapshot.getString("genrea"));
                                writer.setText("WRITER: "+documentSnapshot.getString("writer"));

                                if (documentSnapshot.getString("inStock").equals("1")){
                                    compactswitch.setChecked(true);
                                }else {
                                    compactswitch.setChecked(false);
                                }
                                compactswitch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (compactswitch.isChecked()){
                                            FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).update("inStock","1");
                                        }else {
                                            FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).update("inStock","0");

                                        }
                                    }
                                });

                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Library.this, "deleted", Toast.LENGTH_SHORT).show();
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
        FirebaseFirestore.getInstance().collection("librarybooks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        number.setText("NUMBER: "+documentSnapshot.getString("bookno"));
                        edition.setText("EDITION: "+documentSnapshot.getString("edition"));
                        genrea.setText("GENREA: "+documentSnapshot.getString("genrea"));
                        writer.setText("WRITER: "+documentSnapshot.getString("writer"));

                        if (documentSnapshot.getString("inStock").equals("1")){
                            compactswitch.setChecked(true);
                        }else {
                            compactswitch.setChecked(false);
                        }
                        compactswitch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (compactswitch.isChecked()){
                                    FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).update("inStock","1");
                                }else {
                                    FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).update("inStock","0");

                                }
                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseFirestore.getInstance().collection("librarybooks").document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Library.this, "deleted", Toast.LENGTH_SHORT).show();
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
}