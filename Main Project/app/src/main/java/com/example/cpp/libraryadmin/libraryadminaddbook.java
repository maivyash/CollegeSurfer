package com.example.cpp.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cpp.NumericInputFilter;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class libraryadminaddbook extends AppCompatActivity {
    EditText number,name,writer,edition,genrea;
    Button addbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_libraryadminaddbook);
        number=findViewById(R.id.adminaddbooknumber);
        name=findViewById(R.id.adminaddbookname);
        writer=findViewById(R.id.adminaddbookwriter);
        edition=findViewById(R.id.adminaddbookedition);
        genrea=findViewById(R.id.adminaddbookgenrea);
        addbook=findViewById(R.id.adminaddbookbutton);
        number.setFilters(new InputFilter[]{new NumericInputFilter()});
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitbook();
            }
        });

    }

    private void submitbook() {
        if (number.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Book Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Book Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (genrea.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Book Genrea", Toast.LENGTH_SHORT).show();
            return;
        }
        if (writer.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Book Writer Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edition.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Book Edition", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("bookno",number.getText().toString());
        map.put("writer",writer.getText().toString());
        map.put("edition",edition.getText().toString());
        map.put("genrea",genrea.getText().toString());
        map.put("inStock","1");

        FirebaseFirestore.getInstance().collection("librarybooks").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(libraryadminaddbook.this, "ADDED", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(libraryadminaddbook.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}