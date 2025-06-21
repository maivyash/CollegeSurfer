package com.example.cpp.principleadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class changerole extends AppCompatActivity {

        Spinner role;
        EditText editText;
        TextView textView,noadmin;
        Button submit,check;
        LinearLayout l1;
    String newrole=null;
    String pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changerole);
        role=findViewById(R.id.changerolespinner);
        editText = findViewById(R.id.changeroleuid);
        textView =findViewById(R.id.changerolename);
        submit=findViewById(R.id.changerolesubmit);
        check=findViewById(R.id.changerolecheckuid);
        noadmin=findViewById(R.id.changerolenoadmin);
        noadmin.setVisibility(View.GONE);
        role.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> roleadapter = ArrayAdapter.createFromResource(this, R.array.roles,android.R.layout.simple_spinner_item);
        roleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleadapter);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(changerole.this, "Enter UID First!!!", Toast.LENGTH_SHORT).show();
                    role.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    return ;
                }
                if (!editText.getText().toString().isEmpty()){
                    CollectionReference cf = FirebaseFirestore.getInstance().collection("User");
                    cf.whereEqualTo(FieldPath.documentId(),editText.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()){
                                Toast.makeText(changerole.this, "No UID FOUND", Toast.LENGTH_SHORT).show();
                                role.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);
                                return;
                            }
                            if (!queryDocumentSnapshots.isEmpty()){
                                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                                    textView.setText("NAME: "+documentSnapshot.getString("Fullname"));
                                    textView.setVisibility(View.VISIBLE);
                                    role.setVisibility(View.VISIBLE);
                                    submit.setVisibility(View.VISIBLE);

                                    role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            int k=adapterView.getSelectedItemPosition();
                                             pos =String.valueOf(k);
                                        }


                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (pos.isEmpty()){
                                                Toast.makeText(changerole.this, "Select Role First!!!", Toast.LENGTH_SHORT).show();
                                                return ;

                                            }
                                            if (!pos.isEmpty()){
                                                Map<String,Object> updaterole=new HashMap<>();
                                                updaterole.put("Role",pos);
                                                DocumentReference df= FirebaseFirestore.getInstance().collection("User").document(editText.getText().toString());
                                                df.update(updaterole).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(changerole.this, "Updated Role Suc cessfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(changerole.this, "Something went wrong"+e, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                editText.getText().clear();
                                                finish();

                                            }

                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(changerole.this, "No UID FOUND", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            }
        });


        //DISPLAYING ADMINS
        l1=findViewById(R.id.rolesdisplay);
        CollectionReference cf =FirebaseFirestore.getInstance().collection("User");
        cf.whereNotEqualTo("Role","0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    noadmin.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.GONE);
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        View view =getLayoutInflater().inflate(R.layout.activity_adminsrolesdisplayview,null);

                        TextView name=view.findViewById(R.id.admindisplayname);
                        TextView branch=view.findViewById(R.id.admindisplaybranch);
                        TextView uid=view.findViewById(R.id.admindisplayUID);
                        TextView eno=view.findViewById(R.id.admindisplayeno);
                        TextView year=view.findViewById(R.id.admindisplayyear);
                        TextView role=view.findViewById(R.id.admindisplayrole);

                        Spinner spinner=view.findViewById(R.id.admindisplayrolechangespinner);
                        Button changerolebrn=view.findViewById(R.id.admindisplaychangerolebtn);



                        ArrayAdapter<CharSequence> rolechangeadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.roles,android.R.layout.simple_spinner_item);
                        rolechangeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(rolechangeadapter);
                        int i;
                        i=Integer.parseInt(documentSnapshot.getString("Role"));
                        spinner.setSelection(i);

                        name.setText("Name: "+documentSnapshot.getString("Fullname"));
                        branch.setText("Branch: "+documentSnapshot.getString("Branch"));
                        uid.setText("UID: "+documentSnapshot.getId());
                        eno.setText("Enrollment Number: "+documentSnapshot.getString("Enrolmentnumber"));
                        year.setText("Addmission Year: "+documentSnapshot.getString("AddmisionYear"));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                newrole=String.valueOf(adapterView
                                        .getSelectedItemPosition());

                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        changerolebrn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (newrole.isEmpty()){
                                    Toast.makeText(changerole.this, "Select Role First", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                                DocumentReference df =FirebaseFirestore.getInstance().collection("User").document(documentSnapshot.getId());
                                Map<String,Object> changerolemap=new HashMap<>();
                                changerolemap.put("Role",newrole);
                                df.update(changerolemap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(changerole.this, "Role Changer Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(changerole.this, "Something went wrong"+e, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                        switch (documentSnapshot.getString("Role")){
                            case "0":
                                role.setText("Role: Student");
                                break;
                            case "1" :
                                role.setText("Role: Principal");
                                break;
                            case "2" :
                                role.setText("Role: Boys Hostell Admin");
                                break;
                            case "3" :
                                role.setText("Role: Cooperative Admin");
                                break;
                            case "4" :
                                role.setText("Role: Faculty");
                                break;
                            case "5" :
                                role.setText("Role: Library Admin");
                                break;
                            case "6" :
                                role.setText("Role: Girls Hostell Admin");
                                break;
                            default:
                                Toast.makeText(changerole.this, "Something went worng with "+documentSnapshot.getString("Fullname")+documentSnapshot.getString("Role"), Toast.LENGTH_SHORT).show();
                                return;



                        }
                        l1.addView(view);




                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(changerole.this, "Somethiing went wrong"+e, Toast.LENGTH_SHORT).show();
            }
        });


    }
}