package com.mangolab.mapusers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarkerInfoView extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewInfo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info_view);

        textViewName = (TextView) findViewById(R.id.tv_showName);
        textViewInfo = (TextView) findViewById(R.id.tv_showInfo);

        Intent intent = getIntent();

        String uid = intent.getStringExtra(MapsActivity.UID);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                textViewName.setText(dataSnapshot.child("name").getValue().toString());
                textViewInfo.setText(dataSnapshot.child("address").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
