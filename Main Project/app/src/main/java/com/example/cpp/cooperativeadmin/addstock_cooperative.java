package com.example.cpp.cooperativeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cpp.NumericInputFilter;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addstock_cooperative extends AppCompatActivity {
    TextInputEditText name,price;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstock_cooperative);
        add=findViewById(R.id.additem);
        name=findViewById(R.id.additemname);
        price=findViewById(R.id.additemprice);
        price.setFilters(new InputFilter[]{new NumericInputFilter()});
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(addstock_cooperative.this, "Enter Name of Item", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price.getText().toString().isEmpty()){
                    Toast.makeText(addstock_cooperative.this, "Enter Price of Item", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> map=new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("price",price.getText().toString());
                map.put("inStock","1");
                FirebaseFirestore.getInstance().collection("cooperativestock").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addstock_cooperative.this, "ADDED ITEM SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
    }
}