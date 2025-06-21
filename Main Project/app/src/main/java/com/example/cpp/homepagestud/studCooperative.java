package com.example.cpp.homepagestud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class studCooperative extends AppCompatActivity {
    LinearLayout linearLayout;
    Button button;
    SharedPreferences cooperativeprefences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_cooperative);
         cooperativeprefences=getSharedPreferences("cooperative", Context.MODE_PRIVATE);
         button=findViewById(R.id.studnotifycooperative);
         button.setVisibility(View.GONE);
        linearLayout=findViewById(R.id.studcooperativelinear);

        chk();

        chked();
    }
    private void chked() {
        try {
            if (cooperativeprefences.getBoolean("subscribed", true)) {
                button.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setText("UnSubscribe");
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("cooperative").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "UnSubscribed", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = cooperativeprefences.edit();
                                editor.putBoolean("subscribed", false);
                                editor.commit();
                                chked();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Something went wrong for notification service", Toast.LENGTH_SHORT).show();
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
                        FirebaseMessaging.getInstance().subscribeToTopic("cooperative").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "NOTIFIED", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = cooperativeprefences.edit();
                                editor.putBoolean("subscribed", true);
                                editor.commit();
                                chked();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Something went wrong for notification service", Toast.LENGTH_SHORT).show();
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
        TextView t1=findViewById(R.id.studcooperativestatus);
        DocumentReference df = FirebaseFirestore.getInstance().collection("yesno").document("Cooperative");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    return;
                }
                if (documentSnapshot.getString("cooperative").equals("0")){
                    t1.setText("STORE IS CLOSED");
                    t1.setTextColor(Color.RED);

                } else if (documentSnapshot.getString("cooperative").equals("1")) {
                    t1.setText("STORE IS OPENED");
                    t1.setTextColor(ContextCompat.getColor(studCooperative.this, R.color.dark_green));

                }else
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                        name.setText("NAME: "+documentSnapshot.getString("name"));
                        number.setText("PRICE: "+documentSnapshot.getString("price"));
                        edition.setVisibility(View.GONE);
                        genrea.setVisibility(View.GONE);
                        writer.setVisibility(View.GONE);
                        compactswitch.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);

                        Drawable backgroundDrawable = ContextCompat.getDrawable(studCooperative.this, R.drawable.cutout_loop_red);


                        if (documentSnapshot.getString("inStock").equals("0")){

                            view.setBackground(backgroundDrawable);
                        }
                        linearLayout.addView(view);
                    }

                }
            }
        });


    }
}