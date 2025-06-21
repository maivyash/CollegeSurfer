package com.example.cpp.principleadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cpp.R;

public class Principal_HomePage extends AppCompatActivity {

        Button Role,viewflipper,examshedule,event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_home_page);
        Role=findViewById(R.id.principaladminchangerole);
        viewflipper=findViewById(R.id.principaladminviewflipper);
        examshedule=findViewById(R.id.principaladminexamshedule);
        event=findViewById(R.id.principaladminevents);
        Role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), changerole.class));

            }
        });
        viewflipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), principaladminviewflipper.class));
            }
        });
        examshedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), adminexamsheduleupload.class));

            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), principaladminevent.class));
            }
        });

    }
}