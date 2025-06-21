package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class studLibrary extends AppCompatActivity {
    LinearLayout linearLayout;
    Button button;
    SharedPreferences libraryprefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_library);
        libraryprefences=getSharedPreferences("lib", Context.MODE_PRIVATE);
        linearLayout=findViewById(R.id.studlibraryliner);
        chk();

        button=findViewById(R.id.studnotifylibrary);
        button.setVisibility(View.GONE);
        chked();

    }

    private void chked() {
        try {
            if (libraryprefences.getBoolean("subscribed", true)) {
                button.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setText("UnSubscribe");
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("library").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(studLibrary.this, "UnSubscribed", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = libraryprefences.edit();
                                editor.putBoolean("subscribed", false);
                                editor.commit();
                                chked();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(studLibrary.this, "Something went wrong for notification service", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                button.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setText("NOTIFY");
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("library").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(studLibrary.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = libraryprefences.edit();
                                editor.putBoolean("subscribed", true);
                                editor.commit();
                                chked();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(studLibrary.this, "Something went wrong for notification service", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
            Log.d("EXCEPTION", e.toString());
        }
    }

    private void chk() {
        TextView t1=findViewById(R.id.studlibrarystatus);
        DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("Library");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    return;
                }
                if (documentSnapshot.getString("library").equals("0")){
                    t1.setText("Library is CLOSED");
                    t1.setTextColor(Color.RED);


                } else if (documentSnapshot.getString("library").equals("1")) {
                    t1.setText("Library is OPENED");
                    t1.setTextColor(ContextCompat.getColor(studLibrary.this, R.color.dark_green));


                }else
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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


                        //set below 10 char. bold properly


                        name.setText("NAME: "+documentSnapshot.getString("name"));
                        number.setText("NUMBER: "+documentSnapshot.getString("bookno"));
                        edition.setText("EDITION: "+documentSnapshot.getString("edition"));
                        genrea.setText("GENREA: "+documentSnapshot.getString("genrea"));
                        writer.setText("WRITER: "+documentSnapshot.getString("writer"));



                        if (documentSnapshot.getString("inStock").equals("0")){
                            view.setBackgroundColor(getColor(R.color.red));
                        }
                        compactswitch.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                        linearLayout.addView(view);
                    }

                }
            }
        });

    }
}