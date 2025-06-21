package com.example.cpp.lostfoundstud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class lostandfoundstud extends AppCompatActivity {
    Button addreport,reports;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostandfoundstud);
        addreport=findViewById(R.id.addlostreport);
        reports=findViewById(R.id.yourreport);



        CollectionReference  collectionReference = FirebaseFirestore.getInstance().collection("lostreport");
        LinearLayout l1;
        l1=findViewById(R.id.linerlayout);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data="";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);


                    View itemView = getLayoutInflater().inflate(R.layout.item_document, null);

                    TextView textViewName = itemView.findViewById(R.id.textName);
                    TextView textViewaddress = itemView.findViewById(R.id.textaddress);
                    TextView textViewmobile = itemView.findViewById(R.id.textmo);
                    TextView textViewDate = itemView.findViewById(R.id.textDate);
                    TextView textViewdesc = itemView.findViewById(R.id.textDesc);
                    TextView status = itemView.findViewById(R.id.status);
                    ImageView imageView=itemView.findViewById(R.id.image);
                    StorageReference sf = FirebaseStorage.getInstance().getReference().child("lostandfound/"+documentSnapshot.getId()+"/photoloastobject.jpeg");
                    sf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(lostandfoundstud.this, "Unable to load "+documentSnapshot.getString("Name")+"'s image", Toast.LENGTH_SHORT).show();
                        }
                    });

                    textViewName.setText("Name - "+note.getName());
                    textViewaddress.setText("Address - "+note.getAddress());
                    textViewmobile.setText("Contact Number - "+note.getMobileno());
                    textViewDate.setText("Lost Date - "+note.getDate());
                    textViewdesc.setText("Item Description - "+note.getDescription());


                    if (documentSnapshot.contains("status")){
                        status.setText(documentSnapshot.getString("status"));
                        Drawable backgroundDrawable1 = ContextCompat.getDrawable(lostandfoundstud.this, R.drawable.cutout_loop_red);
                        Drawable backgroundDrawable = ContextCompat.getDrawable(lostandfoundstud.this, R.drawable.cutout_loop_greeen);
                        itemView.setBackground(backgroundDrawable);
                        itemView.setBackground(backgroundDrawable1);

                        if (documentSnapshot.getString("status").equals("Lost")) {
                            itemView.setBackground(backgroundDrawable1);

                        } else if (documentSnapshot.getString("status").equals("Founded")) {
                            itemView.setBackgroundColor(getColor(R.color.green));
                            itemView.setBackground(backgroundDrawable);

                        }
                    }
                    l1.addView(itemView);
                   // t1.setText(data);
                }
            }
        });
        addreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),formtoaddlostreport.class));

            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),userscurrentlostreports.class));

            }
        });
    }


}