package com.mangolab.mapusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Email extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegisterEmail;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__email);

        firebaseAuth = FirebaseAuth.getInstance();



        editTextEmail = (EditText) findViewById(R.id.editTextRegEmail_Email);
        editTextPassword = (EditText) findViewById(R.id.editTextRegEmail_Password);
        buttonRegisterEmail = (Button) findViewById(R.id.buttonRegEmail_Register);

        progressDialog = new ProgressDialog(this);

        buttonRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
                    Snackbar.make(view,"Both fields are required for registration",Snackbar.LENGTH_LONG).show();
                }

                progressDialog.setMessage("Registering Email ID");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email , password )
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(Register_Email.this, "Email ID registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register_Email.this,Register.class));

                                }else{
                                    Toast.makeText(Register_Email.this, "Something went wrong! Try again.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Register_Email.this, "Check if you are already registered!", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });


            }
        });


    }
}
