package com.example.covid19helper.input_details_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class feedback extends AppCompatActivity {

    private String post_id;
    private String user_id;
    private TextView verified;
    private TextView noResponse;
    private TextView scam;
    private String calling_activity;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private int feedback_value;
    private ArrayList<String> feedback_list;
    private Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");
        user_id = intent.getStringExtra("user_uid");
        calling_activity = intent.getStringExtra("calling_activity");


        verified = findViewById(R.id.verified_tv);
        noResponse = findViewById(R.id.no_response_tv);
        scam = findViewById(R.id.scam_tv);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mfirebaseDatabase.getReference().child("posts").child(post_id);

        Query query = mDatabaseReference.child("feedback_list");
        query.addListenerForSingleValueEvent(mFeedbackRecordedValueEventListener);


        if(calling_activity.equals(oxygen_cylinder_activity.class.toString())) {
            intent1 = new Intent(feedback.this,oxygen_cylinder_activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if(calling_activity.equals(plasma_activity.class.toString())) {
            intent1 = new Intent(feedback.this,plasma_activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if(calling_activity.equals(hospital_beds_activity.class.toString())) {
            intent1 = new Intent(feedback.this,hospital_beds_activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if(calling_activity.equals(corona_patients_activity.class.toString())) {
            intent1 = new Intent(feedback.this,corona_patients_activity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }


        verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = mDatabaseReference.child("feedback_value");
                query.addListenerForSingleValueEvent(mVerifiedValueEventListener);
                startActivity(intent1);
            }
        });

        noResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = mDatabaseReference.child("feedback_value");
                query.addListenerForSingleValueEvent(mNoResponseValueEventListener);
                startActivity(intent1);
            }
        });

        scam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = mDatabaseReference.child("feedback_value");
                query.addListenerForSingleValueEvent(mScamValueEventListener);
                startActivity(intent1);
            }
        });

    }

    ValueEventListener mVerifiedValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
                feedback_value = (int) snapshot.getValue(Integer.class);

            feedback_value = feedback_value + 1;
            mDatabaseReference.child("feedback_value").setValue(feedback_value);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener mNoResponseValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
                feedback_value = snapshot.getValue(Integer.class);

            feedback_value = feedback_value - 1 ;
            mDatabaseReference.child("feedback_value").setValue(feedback_value);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener mScamValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
                feedback_value = snapshot.getValue(Integer.class);

            feedback_value = feedback_value - 3;
            mDatabaseReference.child("feedback_value").setValue(feedback_value);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener mFeedbackRecordedValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
                feedback_list = (ArrayList<String>) snapshot.getValue();


            feedback_list.add(user_id);
            mDatabaseReference.child("feedback_list").setValue(feedback_list);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}
