package com.example.cpp.hostelladmin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.example.cpp.Usermodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;
public class adminhostellpenalty extends AppCompatActivity {
EditText eno,amt;
Button submit,chk;
TextView t1;
TextView t2;

    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhostellpenalty);

        eno=findViewById(R.id.enrolmentforpenalty);
        amt=findViewById(R.id.Penaltyamt);
        t2=findViewById(R.id.imposepenalty);
        submit=findViewById(R.id.submitpenalty);
        chk=findViewById(R.id.checkeno);
        t1=findViewById(R.id.texteno);


        TextView t3 = findViewById(R.id.getdesc);
        t3.setVisibility(View.GONE);

        TextView t4 = findViewById(R.id.textepe);
        t4.setVisibility(View.GONE);


        t2.setVisibility(View.GONE);
        amt.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        t1.setVisibility(View.GONE);

        desc=findViewById(R.id.Penaltydesc);
        desc.setVisibility(View.GONE);

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eno.getText().toString().isEmpty()){
                    Toast.makeText(adminhostellpenalty.this, "Enter Enrolment Number", Toast.LENGTH_SHORT).show();
                    return ;
                }
                chekuser();
            }
        });
    }
    private void chekuser() {
        CollectionReference cf=FirebaseFirestore.getInstance().collection("Hostellstud");
        cf.whereEqualTo("Enrolmentnumber",eno.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(adminhostellpenalty.this, "NO Student is Found", Toast.LENGTH_SHORT).show();
                }

                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    if (!documentSnapshot.getString("gender").equals("MALE")){
                        Toast.makeText(adminhostellpenalty.this, "This is Girl Student ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!documentSnapshot.getString("confirmation").equals("1")){
                        Toast.makeText(adminhostellpenalty.this, "Student Addmision request is alredy pending ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usermodel model = documentSnapshot.toObject(Usermodel.class);

                    TextView t2=findViewById(R.id.textepe);


                    TextView t3 =findViewById(R.id.getdesc);


                    t1.setVisibility(View.VISIBLE);

                    t3.setVisibility(View.VISIBLE);

                    amt.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    desc.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);

                    t1.setText("Name - "+model.getFullname());
                    try {
                        t2.setText("Penalty (Older) - "+documentSnapshot.getString("penalty"));
                        t3.setText("Description - "+documentSnapshot.getString("Description"));
                    }catch (Exception e){
                        Log.d("ERROR",e.toString());
                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (amt.getText().toString().isEmpty()){
                                Toast.makeText(adminhostellpenalty.this, "Enter Penalty Amount", Toast.LENGTH_SHORT).show();
                            }
                            if (desc.getText().toString().isEmpty()){
                                Toast.makeText(adminhostellpenalty.this, "Enter Description", Toast.LENGTH_SHORT).show();
                            }
                            Map <String,Object> m1=new HashMap<>();
                            m1.put("penalty",amt.getText().toString());
                            m1.put("Description",desc.getText().toString());
                            DocumentReference df = FirebaseFirestore.getInstance().collection("Hostellstud").document(documentSnapshot.getId());
                            df.update(m1);
                            Toast.makeText(adminhostellpenalty.this, "Penalty updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(adminhostellpenalty.this, "Enrolment  Number is Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}