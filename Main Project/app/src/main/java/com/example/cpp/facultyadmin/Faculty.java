package com.example.cpp.facultyadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cpp.R;

public class Faculty extends AppCompatActivity {
        Button addresult,assingment,attendence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        addresult=findViewById(R.id.btnaddresult);
        assingment=findViewById(R.id.facultyadminaddassingment);
        attendence=findViewById(R.id.facultyadminaddattendence);
        addresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Facultyadminaddtestresult.class));
            }
        });
        assingment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), facultyadminassingment.class));
            }
        });
        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), facultyadminattendence.class));
            }
        });

    }
}