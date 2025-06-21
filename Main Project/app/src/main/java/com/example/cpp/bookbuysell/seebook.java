package com.example.cpp.bookbuysell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cpp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class seebook extends AppCompatActivity {
    Button del;
    ImageView no_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seebook);

        no_book = findViewById(R.id.no_book);
        Glide.with(this).load(R.drawable.no_book).into(no_book);

        LinearLayout l1=findViewById(R.id.yourbook);


        TextView t1;
        t1=findViewById(R.id.nobooksreported);
        t1.setVisibility(View.GONE);
        no_book.setVisibility(View.GONE);
        DocumentReference df = FirebaseFirestore.getInstance().collection("booktosells").document(FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("booktosells").whereEqualTo("uid",FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.isEmpty()){
                    l1.setVisibility(View.GONE);
                    t1.setVisibility(View.VISIBLE);
                    no_book.setVisibility(View.VISIBLE);
                    return;
                }
                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    View itemView = getLayoutInflater().inflate(R.layout.activity_booksview, null);
                    TextView branch=itemView.findViewById(R.id.getbooksellbranch);
                    TextView name=itemView.findViewById(R.id.getbooksellbranch);
                    TextView year=itemView.findViewById(R.id.getbooksellyear);
                    TextView contact=itemView.findViewById(R.id.getbooksellcontact);
                    TextView publication=itemView.findViewById(R.id.getbooksellpublication);
                    TextView semester=itemView.findViewById(R.id.getbooksellsemester);
                    TextView code=itemView.findViewById(R.id.getbooksellcode);
                    TextView subject=itemView.findViewById(R.id.getbooksellsubject);
                    TextView scheme=itemView.findViewById(R.id.getbooksellscheme);
                    MaterialButton  del=itemView.findViewById(R.id.delsell);
                    // set each text view that how many char be bold

                    formatTextView(branch, "BRANCH : " + documentSnapshot.getString("branch"),9);
                    formatTextView(name, "SELLER NAME : " + documentSnapshot.getString("Name"),14);
                    formatTextView(year, "YEAR : " + documentSnapshot.getString("year"),7);
                    formatTextView(contact, "CONTACT : " + documentSnapshot.getString("Contact"),10);
                    formatTextView(publication, "PUBLICATION : " + documentSnapshot.getString("publication"),14);
                    formatTextView(semester, "SEMESTER : " + documentSnapshot.getString("semester"),11);
                    formatTextView(code, "CODE : " + documentSnapshot.getString("code"),7);
                    formatTextView(subject, "SUBJECT : " + documentSnapshot.getString("subject"),10);
                    formatTextView(scheme, "SCHEME : " + documentSnapshot.getString("scheme"),9);



                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DocumentReference df = FirebaseFirestore.getInstance().collection("booktosells").document(documentSnapshot.getId());
                            df.delete();
                            Toast.makeText(seebook.this, "Deleted.......", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    l1.addView(itemView);
                }

            }
            private void formatTextView(TextView textView, String text, int boldLength) {
                SpannableString spannableString = new SpannableString(text);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(boldLength, text.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            }
        });
    }
}