package com.example.covid19helper.input_details_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19helper.MainActivity;
import com.example.covid19helper.R;
import com.example.covid19helper.data_sets.postSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class postActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private String lead_type;
    private EditText mAddress;
    private EditText mCity;
    private EditText mContact;
    private Button mSubmit;
    private Button mRecheckAddress;
    private int mRecheck = 0;
    private int mPosition = 0;


    //Location Components declaration
    private double longitude;
    private double latitude;
    private int feedback_value;
    private ArrayList<String> feedback_list;


    //Firebase
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAddress = findViewById(R.id.addressLead_et);
        mCity = findViewById(R.id.cityLead_et);
        mContact = findViewById(R.id.contact_no_lead_et);
        mSubmit = findViewById(R.id.submitLead_btn);
        mRecheckAddress = findViewById(R.id.recheckLead_btn);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        feedback_value = 0;
        feedback_list = new ArrayList<>();
        feedback_list.add("");

        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            mAddress.setText(intent.getStringExtra("address"));
            spinner.setSelection(intent.getIntExtra("spinner_pos",0));
            mCity.setText(intent.getStringExtra("city"));
            mContact.setText(intent.getStringExtra("contact_no"));
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
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference().child("posts");

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postSet mpostSet = new postSet(lead_type,
                        longitude,latitude,
                        mAddress.getText().toString(),
                        mCity.getText().toString(),
                        mContact.getText().toString(),feedback_value,feedback_list);

                mdatabaseReference.push().setValue(mpostSet);

                Intent intent = new Intent(postActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        mRecheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalAddress = mAddress.getText().toString() + " " + mCity.getText().toString();

                Intent intent = new Intent(postActivity.this, com.example.covid19helper.input_details_activities.map_activity.class);
                intent.putExtra("calling_activity",postActivity.class.toString());
                intent.putExtra("total_address", totalAddress);
                intent.putExtra("address", mAddress.getText().toString());
                intent.putExtra("city", mCity.getText().toString());
                intent.putExtra("contact_no", mContact.getText().toString());
                intent.putExtra("spinner_pos",mPosition);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        lead_type = parent.getItemAtPosition(position).toString();
        mPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
