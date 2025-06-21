package com.example.cpp.lostfoundstud;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.cpp.CharacterInputFilter;
import com.example.cpp.NumericInputFilter;
import com.example.cpp.R;
import com.example.cpp.facultyadmin.facultyadminassingment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class formtoaddlostreport extends AppCompatActivity {
    EditText description,name,con,address;
    TextInputEditText date;

    Button submitnew;
    FirebaseFirestore FStore;
    Uri photouri;
    Spinner spinner;
    String status;
Button addphoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formtoaddlostreport);
        submitnew=findViewById(R.id.submitnewlostreport);
        description=findViewById(R.id.descriptionofnewlostreport);
        name=findViewById(R.id.lostnewreportname);
        name.setFilters(new InputFilter[]{new CharacterInputFilter()});
        con=findViewById(R.id.mobileno);
        con.setFilters(new InputFilter[]{new NumericInputFilter()});
        date=findViewById(R.id.lostnewreportdate);

        address=findViewById(R.id.lostnewreportaddress);
        FStore=FirebaseFirestore.getInstance();
        addphoto=findViewById(R.id.photoofnewlostreport);
        spinner=findViewById(R.id.lostfoundspinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        status=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
        try {
            DocumentReference df=FirebaseFirestore.getInstance().collection("lostreport").document(FirebaseAuth.getInstance().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        Toast.makeText(formtoaddlostreport.this, "MAXIMUM NUMBER OF REPORTS EXCEEDED", Toast.LENGTH_SHORT).show();
                        Toast.makeText(formtoaddlostreport.this, "DELETE AVAILABLE BLE REPORTS FIRST", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
            });
        }catch (Exception e){

        }
            addphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photointent = new Intent();
                    photointent.setType("image/jpeg");
                    photointent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photointent,"CHOOSE PHOTO"),1);

                }
            });

        submitnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendreport();

            }
        });
    }

    private void sendreport() {

        if (con.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter contact number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (con.getText().toString().length()>10||con.getText().toString().length()<10){
            Toast.makeText(getApplicationContext(), "Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter name number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter Description number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (photouri==null){
            Toast.makeText(getApplicationContext(), " Select Photo First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), " Fill Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (status.isEmpty()){
            Toast.makeText(getApplicationContext(), " SELECT STATUS", Toast.LENGTH_SHORT).show();
        return;
    }
        try {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference file=storageReference.child("lostandfound/"+FirebaseAuth.getInstance().getUid()+"/photoloastobject.jpeg");
            file.putFile(photouri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(formtoaddlostreport.this, "Something Went Wrong While Uploading Photo To DATABASE....", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        } catch (Exception e) {
            if (e instanceof Exception){
                Toast.makeText(formtoaddlostreport.this, "Something Went Wrong While Uploading Photo To DATABASE....", Toast.LENGTH_SHORT).show();
                return;
            }
            throw new RuntimeException(e);
        }

        Map<String,Object> loststore = new HashMap<>();
        loststore.put("Mobileno",con.getText().toString());
        loststore.put("Name",name.getText().toString());
        loststore.put("description",description.getText().toString());
        loststore.put("address",address.getText().toString());
        loststore.put("date",date.getText().toString());
        loststore.put("status",status);
        FStore.collection("lostreport").document(FirebaseAuth.getInstance().getUid()).set(loststore).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(formtoaddlostreport.this, "Something went worng please try again", Toast.LENGTH_SHORT).show();

                finish();
                return;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(formtoaddlostreport.this, "Successfully updated your request", Toast.LENGTH_SHORT).show();
                FCMSend.Builder builder=new FCMSend.Builder("lostandfound",true)
                        .setTitle("LOST AND FOUNDED")
                        .setBody(name.getText().toString()+" HAS "+status+name.getText().toString() +"SOMETHING "+"\nclick here to view");
                builder.send();
                finish();
                return;

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            photouri=data.getData();
        }
    }
}