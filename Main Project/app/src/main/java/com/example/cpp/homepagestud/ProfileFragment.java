package com.example.cpp.homepagestud;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cpp.MainActivity;
import com.example.cpp.NotificationManagerActivity;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment  {
    private static final String SHARED_PREF_NAME = "loginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    MaterialTextView UID;
    Button logout,notification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        notification=view.findViewById(R.id.notificationbtn);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext()==null)
                    return;
                startActivity(new Intent(getContext(), NotificationManagerActivity.class));
            }
        });
        logout=view.findViewById(R.id.logoutButton);
        UID=view.findViewById(R.id.profilefragmentuid);
        DocumentReference df=FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TextView name=view.findViewById(R.id.nameprofile);
                TextView eno=view.findViewById(R.id.enoprofile);
                TextView branch=view.findViewById(R.id.branchprofile);
                TextView name1 = view.findViewById(R.id.username);
                name1.setText(documentSnapshot.getString("Fullname"));
                name.setText("NAME: "+documentSnapshot.getString("Fullname"));
                eno.setText("Enrolment Number: "+documentSnapshot.getString("Enrolmentnumber"));
                branch.setText("Branch: "+documentSnapshot.getString("Branch"));
            }
        });

        //Displaying UID
        UID.setText("User Id: " + FirebaseAuth.getInstance().getUid()+"@gpgondia.ac.in");
        UID.setTextIsSelectable(true);

        //Logout Button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext()==null)
                    return;
                //Alert  Dialog Box
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                builder.setView(dialogView);


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
                                if (isLoggedIn) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(KEY_IS_LOGGED_IN, false);
                                    editor.apply();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                }else {

                                    Toast.makeText(getContext(), "You are not Logged In Yet", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), "Logged In First!!!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_NEGATIVE);

                        //custom style to the buttons
                        if (positiveButton != null && negativeButton != null) {
                            positiveButton.setTextSize(16); // Change text size
                            positiveButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // Change text color
                            negativeButton.setTextSize(16); // Change text size
                            negativeButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // Change text color
                        }
                    }
                });
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.cutout04);

                dialog.show();
            }
        });
        return view;
    }

}