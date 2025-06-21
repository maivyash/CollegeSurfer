package com.example.cpp.homepagestud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cpp.MainActivity;
import com.example.cpp.R;
import com.example.cpp.hostellstud.hostellmainstud;
import com.example.cpp.lostfoundstud.lostandfoundstud;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

TextView hello;
LinearLayout lostandfound, hostelclick;

ImageView imageview3,imageview4,imageview5;
CircleImageView hostell, attend, Event,exam,assignment,lost,library,cooperative;
int i=0;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        //Circle_imageview and imageview images
        if (!isAdded()){
            return view;
        }
       imageview3=view.findViewById(R.id.imageView3);
       imageview4=view.findViewById(R.id.imageView4);
       imageview5=view.findViewById(R.id.imageView5);
        Glide.with(this).load(R.drawable.image2).into(imageview3);
        Glide.with(this).load(R.drawable.image3).into(imageview4);
        Glide.with(this).load(R.drawable.image4).into(imageview5);
       StorageReference sf= FirebaseStorage.getInstance().getReference().child("Viewflipper");
       sf.listAll().addOnSuccessListener(listResult -> {
           if (listResult.getItems().isEmpty()){
               return;
           }
           Log.d("HEHEHE", String.valueOf(listResult.getItems().size()));
           int count = 0;
           for (StorageReference reference : listResult.getItems()) {
               final int currentCount = ++count; // Create a final variable to capture the current count
               reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
/*but line no 92 indicatioong this line in logcat */
                   if (isAdded()){
                       if (currentCount == 1) {
                           Glide.with(requireContext().getApplicationContext()).load(bytes).into(imageview3);
                           imageview3.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+reference.getName())));
                               }
                           });
                       } else if (currentCount == 2) {
                           Glide.with(requireContext().getApplicationContext()).load(bytes).into(imageview4);
                           imageview4.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+reference.getName())));
                               }
                           });
                       } else if (currentCount == 3) {
                           Glide.with(requireContext().getApplicationContext()).load(bytes).into(imageview5);
                           imageview5.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+reference.getName())));
                               }
                           });
                       }
                   }
               });
           }
       });
       hostell =view.findViewById(R.id.Hostel);
       attend =view.findViewById(R.id.Attend);
       Event =view.findViewById(R.id.Event);
       exam =view.findViewById(R.id.Exam);
       assignment =view.findViewById(R.id.Assignment);
       lost =view.findViewById(R.id.Lost);
       library =view.findViewById(R.id.Library);
       cooperative =view.findViewById(R.id.Cooperative);




        Glide.with(this).load(R.drawable.attendence).into(attend);
        Glide.with(this).load(R.drawable.event).into(Event);
        Glide.with(this).load(R.drawable.exam).into(exam);
        Glide.with(this).load(R.drawable.assignment).into(assignment);
        Glide.with(this).load(R.drawable.lost).into(lost);
        Glide.with(this).load(R.drawable.hostle).into(hostell);
        Glide.with(this).load(R.drawable.libarary2).into(library);
        Glide.with(this).load(R.drawable.cooprative1).into(cooperative);


        //listners
        hostelclick=view.findViewById(R.id.Hostel_add_page);

        hello= view.findViewById(R.id.textView4);
        lostandfound=view.findViewById(R.id.lostandfoundhomestud);
        DocumentReference df= FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hello.setText("Hello , "+ documentSnapshot.getString("Fullname"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded()){
                    startActivity(new Intent(getActivity(), studassingment.class));
                }

            }
        });

        hostelclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), hostellmainstud.class));
            }
        });
        lostandfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), lostandfoundstud.class));
            }
        });
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), studLibrary.class));
            }
        });
        cooperative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), studCooperative.class));
            }
        });
        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), studattendance.class));
            }
        });
        Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), studevent.class));
            }
        });
        exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), studtimetable.class));
            }
        });
        return view;
    }

}