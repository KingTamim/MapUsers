package com.mangolab.mapusers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class Profile_Activity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);

        firebaseAuth = FirebaseAuth.getInstance();



        Button startMap = (Button) findViewById(R.id.button_StartMap);
        Button logOut = (Button) findViewById(R.id.buttonLogOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile_Activity.this, Login_Activity.class));
                finish();

            }
        });



        startMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Activity.this,MapsActivity.class));

            }
        });
    }
}
