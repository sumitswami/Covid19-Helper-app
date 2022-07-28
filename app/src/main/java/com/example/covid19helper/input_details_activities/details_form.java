package com.example.covid19helper.input_details_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19helper.MainActivity;
import com.example.covid19helper.R;
import com.example.covid19helper.data_sets.detailSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class details_form extends AppCompatActivity {

    private EditText mName;
    private EditText mAddress;
    private EditText mCity;
    private EditText mContact;
    private Button mSubmit;
    private Button mwantHelp;
    private Button mhaveLead;
    private Button mRecheckAddress;
    private TextView mcategory;
    private String mUid;
    private int mRecheck = 0;
    private int category_no;


    //Firebase
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    //Location Components declaration
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_form);

        mName = findViewById(R.id.name_et);
        mAddress = findViewById(R.id.address_et);
        mCity = findViewById(R.id.city_et);
        mContact = findViewById(R.id.contact_no_et);
        mSubmit = findViewById(R.id.submit_btn);
        mwantHelp = findViewById(R.id.want_help_btn);
        mhaveLead = findViewById(R.id.have_lead_btn);
        mcategory = findViewById(R.id.category_tv);
        mRecheckAddress = findViewById(R.id.recheck_btn);

        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            mAddress.setText(intent.getStringExtra("address"));
            mName.setText(intent.getStringExtra("name"));
            mCity.setText(intent.getStringExtra("city"));
            mContact.setText(intent.getStringExtra("contact_no"));
            mcategory.setText(intent.getStringExtra("category"));
            category_no = intent.getIntExtra("category_no",0);
            mRecheck = intent.getIntExtra("recheck", 0);
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
        }


        if (mRecheck == 1) {
            mSubmit.setVisibility(View.VISIBLE);
            mRecheckAddress.setVisibility(View.GONE);
        } else {
            mSubmit.setVisibility(View.GONE);
            mRecheckAddress.setVisibility(View.VISIBLE);
        }

        //Firebase Components Intitalize
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference().child("user_details");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUid = user.getUid();
                }
            }
        };
        mfirebaseAuth.addAuthStateListener(mAuthStateListener);

        mwantHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcategory.setText("Want Help");
                category_no = 0;
            }
        });

        mhaveLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcategory.setText("Have Lead");
                category_no = 1;
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailSet mdetailSet = new detailSet(mName.getText().toString(),
                        mAddress.getText().toString(),
                        mCity.getText().toString(),
                        mContact.getText().toString(),category_no,longitude, latitude, mUid);

                mdatabaseReference.push().setValue(mdetailSet);

                Intent intent = new Intent(details_form.this, MainActivity.class);
                startActivity(intent);

            }
        });

        mRecheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalAddress = mAddress.getText().toString() + " " + mCity.getText().toString();

                Intent intent = new Intent(details_form.this, map_activity.class);
                intent.putExtra("calling_activity",details_form.class.toString());
                intent.putExtra("total_address", totalAddress);
                intent.putExtra("address", mAddress.getText().toString());
                intent.putExtra("name", mName.getText().toString());
                intent.putExtra("city", mCity.getText().toString());
                intent.putExtra("contact_no", mContact.getText().toString());
                intent.putExtra("category", mcategory.getText().toString());
                intent.putExtra("category_no",category_no);
                startActivity(intent);
            }
        });

    }
}
