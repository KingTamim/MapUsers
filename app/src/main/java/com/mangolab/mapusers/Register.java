package com.mangolab.mapusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {


    private EditText editTextName,editTextBusinessName,editTextAddress;
    private Button buttonSubmit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());


        editTextName = (EditText) findViewById(R.id.editTextRegister_Name);
        editTextBusinessName = (EditText) findViewById(R.id.editTextRegister_Business);
        editTextAddress = (EditText) findViewById(R.id.editTextRegister_Address);
        buttonSubmit = (Button) findViewById(R.id.buttonRegister_Submit);

        progressDialog = new ProgressDialog(this);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextName.getText().toString().trim();
                String bName= editTextBusinessName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(bName) || TextUtils.isEmpty(address) ){
                    Snackbar.make(view,"All fields are required", Snackbar.LENGTH_LONG).show();

                }
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(bName) && !TextUtils.isEmpty(address) ){
                    progressDialog.setMessage("Saving User Data");
                    progressDialog.show();

                    UserData storeUserData = new UserData(name,bName,address);
                    databaseReference.setValue(storeUserData);

                    Snackbar.make(view,"Data saved successfully",Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this,Profile_Activity.class));
                    finish();


                }
                progressDialog.dismiss();




            }
        });


    }
}
