package com.example.cpp.bookbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.CharacterInputFilter;
import com.example.cpp.NumericInputFilter;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class addbook extends AppCompatActivity {
EditText name,number,code,subject;
Spinner publication,branch,semester,year,scheme;
Button submit;
String Sbranch,Syear,Spublication,Ssemester,Sscheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        CollectionReference cf =FirebaseFirestore.getInstance().collection("booktosells");
        cf.whereEqualTo("uid",FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>4){
                    Toast.makeText(addbook.this, "Please delete the alredy present unsoled book entry", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        name=findViewById(R.id.booksellername);
        name.setFilters(new InputFilter[]{new CharacterInputFilter()});
        number=findViewById(R.id.booksellermob);
        number.setFilters(new InputFilter[]{new NumericInputFilter()});
        publication=findViewById(R.id.bookpublication);
        branch=findViewById(R.id.bookBranch);
        semester=findViewById(R.id.booksemester);
        year=findViewById(R.id.bookyearpurchace);
        submit=findViewById(R.id.sellbooksubmit);
        code=findViewById(R.id.subcode);
        code.setFilters(new InputFilter[]{new NumericInputFilter()});
        subject=findViewById(R.id.subject);
        subject.setFilters(new InputFilter[]{new CharacterInputFilter()});
        scheme=findViewById(R.id.selectiongschemeofbook);
        //bellows are List's Options Adapter
        ArrayAdapter<CharSequence> publicationadapter = ArrayAdapter.createFromResource(this, R.array.Publication,android.R.layout.simple_spinner_item);
        publicationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        publication.setAdapter(publicationadapter);

        ArrayAdapter<CharSequence> branchadapter = ArrayAdapter.createFromResource(this, R.array.numbers,android.R.layout.simple_spinner_item);
        branchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(branchadapter);

        ArrayAdapter<CharSequence> yearadapter = ArrayAdapter.createFromResource(this, R.array.year,android.R.layout.simple_spinner_item);
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearadapter);

        ArrayAdapter<CharSequence> semesteradapter = ArrayAdapter.createFromResource(this, R.array.semester,android.R.layout.simple_spinner_item);
        semesteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(semesteradapter);

        ArrayAdapter<CharSequence> schemeadapter = ArrayAdapter.createFromResource(this, R.array.scheme,android.R.layout.simple_spinner_item);
        semesteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheme.setAdapter(schemeadapter);

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Ssemester=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Syear=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Sbranch=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        publication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spublication=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        scheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Sscheme=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(addbook.this, "Enter a name first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.getText().toString().isEmpty()){
                    Toast.makeText(addbook.this, "Enter a Mobile Number first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Ssemester.isEmpty()){
                    Toast.makeText(addbook.this, "Enter a SEMESTER first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.getText().toString().length()<10||number.getText().toString().length()>10){
                    Toast.makeText(addbook.this, "Enter a Valid Mobile Number first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code.getText().toString().length()>5||code.getText().toString().length()<5){
                    Toast.makeText(addbook.this, "Enter a Valid Subject Code First!", Toast.LENGTH_SHORT).show();
                    return;
                }



                Map<String,Object> bookinfo=new HashMap<>();
                bookinfo.put("Name",name.getText().toString());
                bookinfo.put("Contact",number.getText().toString());
                bookinfo.put("branch",Sbranch);
                bookinfo.put("year",Syear);
                bookinfo.put("semester",Ssemester);
                bookinfo.put("publication",Spublication);
                bookinfo.put("code",code.getText().toString());
                bookinfo.put("subject",subject.getText().toString());
                bookinfo.put("scheme",Sscheme);
                bookinfo.put("uid",FirebaseAuth.getInstance().getUid());
                FirebaseFirestore.getInstance().collection("booktosells").document().set(bookinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addbook.this, "Books Information Submitted", Toast.LENGTH_SHORT).show();
                        FCMSend.Builder builder=new FCMSend.Builder("book",true)
                                .setTitle("BOOK BUY AND SELL")
                                .setBody("SOMEONE WANTED TO SELL BOOK OF  "+subject.getText().toString());
                        builder.send();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addbook.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        Toast.makeText(addbook.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}