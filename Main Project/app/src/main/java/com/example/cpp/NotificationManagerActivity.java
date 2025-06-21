package com.example.cpp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationManagerActivity extends AppCompatActivity {
    SharedPreferences notify;
Switch lostandfouond,examshedule,assingment,attendence,library,bookbuyandsell,cooperative,event,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notify=getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_notification_manager);
        lostandfouond=findViewById(R.id.lafnotify);
        examshedule=findViewById(R.id.examnotify);
        assingment=findViewById(R.id.assinotify);
        attendence=findViewById(R.id.attnotify);
        event=findViewById(R.id.eventnotify);
        library=findViewById(R.id.libnotify);
        bookbuyandsell=findViewById(R.id.bbsnotify);
        cooperative=findViewById(R.id.copnotify);
        result=findViewById(R.id.resnotify);
notified();
}

    private void notified() {
        try {
            if (notify.getBoolean("lostandfound",true)){
                lostandfouond.setChecked(true);
                lostandfouond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("lostandfound");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("lostandfound",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                lostandfouond.setChecked(false);
                lostandfouond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("lostandfound");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("lostandfound",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }



            if (notify.getBoolean("cooperative",true)){
                cooperative.setChecked(true);
                cooperative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("cooperative");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("cooperative",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                cooperative.setChecked(false);
                cooperative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("cooperative");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("cooperative",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }



            if (notify.getBoolean("library",true)){
                library.setChecked(true);
                library.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("library");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("library",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                library.setChecked(false);
                library.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("library");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("library",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }



            if (notify.getBoolean("book",true)){
                bookbuyandsell.setChecked(true);
                bookbuyandsell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("book");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("book",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                bookbuyandsell.setChecked(false);
                bookbuyandsell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("book");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("book",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }


            if (notify.getBoolean("exam",true)){
                examshedule.setChecked(true);
                examshedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("exam");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("exam",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                examshedule.setChecked(false);
                examshedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("exam");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("exam",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }


            if (notify.getBoolean("event",true)){
                event.setChecked(true);
                event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("event");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("event",false);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }else {
                event.setChecked(false);
                event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseMessaging.getInstance().subscribeToTopic("event");
                        SharedPreferences.Editor editor=notify.edit();
                        editor.putBoolean("event",true);
                        editor.commit();
                        Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                        notified();
                    }
                });
            }


            FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String assingmentS,attendenceS,resultS;
                    assingmentS="assignment"+documentSnapshot.getString("Branch");
                    attendenceS="attendance"+documentSnapshot.getString("Branch");
                    resultS="result"+documentSnapshot.getString("Branch");



                    if (notify.getBoolean("assignment",true)){
                        assingment.setChecked(true);
                        assingment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(assingmentS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("assignment",false);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }else {
                        assingment.setChecked(false);
                        assingment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().subscribeToTopic(assingmentS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("assignment",true);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }



                    if (notify.getBoolean("attendance",true)){
                        attendence.setChecked(true);
                        attendence.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(attendenceS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("attendance",false);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }else {
                        attendence.setChecked(false);
                        attendence.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().subscribeToTopic(attendenceS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("attendance",true);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }
                    if (notify.getBoolean("result",true)){
                        result.setChecked(true);
                        result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(resultS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("result",false);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "UN-NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }else {
                        result.setChecked(false);
                        result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseMessaging.getInstance().subscribeToTopic(resultS);
                                SharedPreferences.Editor editor=notify.edit();
                                editor.putBoolean("result",true);
                                editor.commit();
                                Toast.makeText(NotificationManagerActivity.this, "NOTIFIED", Toast.LENGTH_SHORT).show();
                                notified();
                            }
                        });
                    }




                }
            });

        }catch (Exception e){

        }





    }
    }
