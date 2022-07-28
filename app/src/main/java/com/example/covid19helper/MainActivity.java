package com.example.covid19helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19helper.data_sets.detailSet;
import com.example.covid19helper.input_details_activities.details_form;
import com.example.covid19helper.input_details_activities.postActivity;
import com.example.covid19helper.resources_activities.corona_patients_activity;
import com.example.covid19helper.resources_activities.hospital_beds_activity;
import com.example.covid19helper.resources_activities.oxygen_cylinder_activity;
import com.example.covid19helper.resources_activities.plasma_activity;
import com.example.covid19helper.resources_activities.vaccinationactivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView moxygenCylinder;
    private TextView mplasma;
    private TextView mbeds;
    private TextView mpost;
    private TextView mCoronaPatients;
    private TextView mVaccination;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    public static final int RC_SIGN_IN = 1;

    // Firebase instance variables
    private FirebaseAuth mfirebaseAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mdatabaseReference;

    private ValueEventListener mvalueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moxygenCylinder = findViewById(R.id.oxygen_cylinder_tv);
        mplasma = findViewById(R.id.plasma_tv);
        mbeds = findViewById(R.id.beds_tv);
        mpost = findViewById(R.id.post_tv);
        mCoronaPatients = findViewById(R.id.corona_patients_tv);

        mUsername = ANONYMOUS;

        // Initialize Firebase components
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference().child("user_details");


        mvalueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Intent intent =  new Intent(MainActivity.this, details_form.class);
                    Toast.makeText(MainActivity.this,"Please fill the Personal Details",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    for(DataSnapshot snapshot_item : snapshot.getChildren()){
                        detailSet mdetailSet = snapshot_item.getValue(detailSet.class);
                        if(mdetailSet.getCategory_no()==0)
                            mpost.setVisibility(View.GONE);
                        else
                            mpost.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    // User is signed in

                    Query query = mdatabaseReference
                            .orderByChild("uid")
                            .equalTo(user.getUid());

                    query.addListenerForSingleValueEvent(mvalueEventListener);
                }

                else{
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(MainActivity.this, postActivity.class);
                startActivity(postIntent);
            }
        });

        moxygenCylinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, oxygen_cylinder_activity.class);
                startActivity(intent);
            }
        });

        mplasma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, plasma_activity.class);
                startActivity(intent);
            }
        });

        mbeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, hospital_beds_activity.class);
                startActivity(intent);
            }
        });

        mCoronaPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, corona_patients_activity.class);
                startActivity(intent);
            }
        });

        TextView vaccines = (TextView) findViewById(R.id.vaccination);

        //Set an onClick Listener on that View
        vaccines.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link NumbersActivity}
                Intent vaccinationIntent = new Intent(MainActivity.this, vaccinationactivity.class);

                // Start the new activity
                startActivity(vaccinationIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!=null)
            mfirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
