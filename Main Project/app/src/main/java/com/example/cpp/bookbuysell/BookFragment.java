package com.example.cpp.bookbuysell;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cpp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookFragment extends Fragment {
    LinearLayout l1;
    Button books;
    TextView t1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_book,container,false);
        Button addbookbtn;
        addbookbtn=view.findViewById(R.id.addbooks);
        t1=view.findViewById(R.id.nobooktosell);
        t1.setVisibility(View.GONE);
        addbookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), addbook.class));
            }
        });
        books=view.findViewById(R.id.seesellbooks);

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), seebook.class));
            }
        });
        // Inflate the layout for this fragment
        l1=view.findViewById(R.id.booksview);
        CollectionReference cf= FirebaseFirestore.getInstance().collection("booktosells");
        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    l1.setVisibility(View.GONE);
                    t1.setVisibility(View.VISIBLE);
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    /*line no 59*/    View itemView = inflater.inflate(R.layout.activity_booksview, null);
                    TextView branch=itemView.findViewById(R.id.getbooksellbranch);
                    TextView name=itemView.findViewById(R.id.getbooksellname);
                    TextView year=itemView.findViewById(R.id.getbooksellyear);
                    TextView contact=itemView.findViewById(R.id.getbooksellcontact);
                    TextView publication=itemView.findViewById(R.id.getbooksellpublication);
                    TextView semester=itemView.findViewById(R.id.getbooksellsemester);
                    TextView code=itemView.findViewById(R.id.getbooksellcode);
                    TextView subject=itemView.findViewById(R.id.getbooksellsubject);
                    TextView scheme=itemView.findViewById(R.id.getbooksellscheme);
                    MaterialButton button=itemView.findViewById(R.id.delsell);
                    button.setVisibility(View.GONE);
                    // Make starting 10 char bold
                    formatTextView(branch, "BRANCH : " + documentSnapshot.getString("branch"),9);
                    formatTextView(name, "SELLER NAME : " + documentSnapshot.getString("Name"),14);
                    formatTextView(year, "YEAR : " + documentSnapshot.getString("year"),7);
                    formatTextView(contact, "CONTACT : " + documentSnapshot.getString("Contact"),10);
                    formatTextView(publication, "PUBLICATION : " + documentSnapshot.getString("publication"),14);
                    formatTextView(semester, "SEMESTER : " + documentSnapshot.getString("semester"),11);
                    formatTextView(code, "CODE : " + documentSnapshot.getString("code"),7);
                    formatTextView(subject, "SUBJECT : " + documentSnapshot.getString("subject"),10);
                    formatTextView(scheme, "SCHEME : " + documentSnapshot.getString("scheme"),9);

                    l1.addView(itemView);
                }
            }
        });

        return view;
    }

    private void formatTextView(TextView textView, String text, int boldLength) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, Math.min(boldLength, text.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

}
