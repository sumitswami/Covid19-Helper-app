package com.example.covid19helper.resources_activities;

import static java.lang.Math.sqrt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19helper.R;
import com.example.covid19helper.data_sets.detailSet;
import com.example.covid19helper.data_sets.item_set;
import com.example.covid19helper.data_sets.postSet;
import com.example.covid19helper.input_details_activites.feedback;
import com.example.covid19helper.itemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class plasma_activity extends AppCompatActivity implements itemAdapter.OnItemHoldListener,itemAdapter.OnItemClickListener {

    // Firebase instance variables
    private FirebaseAuth mfirebaseAuth;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference muser_details_databaseReference;
    private DatabaseReference mpost_databaseReference;
    private FirebaseUser muser;

    private String post_address;
    private String post_city;
    private String post_contact ;
    private String post_lead_type;
    private String post_distance;
    private String post_id;
    private int post_feedback_value;

    private double user_longitude;
    private double user_latitude;
    private double post_latitude;
    private double post_longitude;

    private RecyclerView recyclerView;
    private List<item_set> itemArrayList;
    private itemAdapter adapter;

    private boolean isUserFeedbackRecorded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        itemArrayList = new ArrayList<>();
        adapter = new itemAdapter(this,itemArrayList,this,this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase components
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        muser_details_databaseReference = mfirebaseDatabase.getReference().child("user_details");
        mpost_databaseReference = mfirebaseDatabase.getReference().child("posts");
        muser = mfirebaseAuth.getCurrentUser();

        isUserFeedbackRecorded = false;

        Toast.makeText(this,"Tap to Contact And Hold to give Feedback",Toast.LENGTH_SHORT).show();

        if (muser != null) {
            Query query1 = muser_details_databaseReference
                    .orderByChild("uid")
                    .equalTo(muser.getUid());

            query1.addListenerForSingleValueEvent(muser_details_valueEventListener);

            Query query2 = mpost_databaseReference
                    .orderByChild("lead_type")
                    .equalTo("Plasma");

            query2.addListenerForSingleValueEvent(mpost_valueEventListener);
        }
    }

    ValueEventListener muser_details_valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for(DataSnapshot snapshot_item : snapshot.getChildren()) {
                    detailSet mdetailSet = snapshot_item.getValue(detailSet.class);
                    user_latitude = mdetailSet.getLatitude();
                    user_longitude = mdetailSet.getLongitude();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener mpost_valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            itemArrayList.clear();
            if(snapshot.exists()){
                for(DataSnapshot snapshot_item : snapshot.getChildren()){
                    postSet mpostSet = snapshot_item.getValue(postSet.class);
                    post_feedback_value = mpostSet.getFeedback_value();
                    if( post_feedback_value > -3 ){
                        post_id = snapshot_item.getKey();
                        post_longitude = mpostSet.getLongitude();
                        post_latitude = mpostSet.getLatitude();
                        post_address = mpostSet.getAddress();
                        post_city = mpostSet.getCity();
                        post_contact = mpostSet.getContact_no();
                        post_lead_type = mpostSet.getLead_type();
                        post_distance = distance(user_latitude, user_longitude,
                                post_latitude, post_longitude);
                        item_set itemSet = new item_set(post_address, post_contact,
                                post_lead_type, post_city, post_distance,post_id,post_feedback_value);
                        itemArrayList.add(itemSet);
                    }
                }
                Collections.sort(itemArrayList,item_set.distanceComparator);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private String distance(double lat1, double lon1, double lat2, double lon2) {

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double ans = sqrt((dlon*dlon) + (dlat*dlat));

        double ans_km = ans * 100;
        String ans_string = String.valueOf(ans_km);
        return ans_string;
    }

    ValueEventListener mIsUserFeedbackRecordedValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                boolean flag = false;
                for(DataSnapshot snapshot_item: snapshot.getChildren()){
                    if(snapshot_item.getValue().equals(muser.getUid())){
                        Toast.makeText(plasma_activity.this,"Your Feedback for this lead is already been recorded",Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }
                if(!flag){

                    Intent intent = new Intent(plasma_activity.this, feedback.class);
                    intent.putExtra("post_id",post_id);
                    intent.putExtra("user_uid", muser.getUid());
                    intent.putExtra("calling_activity",plasma_activity.class.toString());
                    startActivity(intent);

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onItemHold(int position) {
        post_id = itemArrayList.get(position).getPost_id();
        Query query = mpost_databaseReference.child(post_id)
                .child("feedback_list");
        query.addListenerForSingleValueEvent(mIsUserFeedbackRecordedValueEventListener);
    }

    @Override
    public void onItemClick(int postition) {
        String uri = "tel:" + "+91" + itemArrayList.get(postition).getContact().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
