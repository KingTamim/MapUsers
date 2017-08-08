package com.mangolab.mapusers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login_Activity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private TextView textViewRegister;
    private FirebaseAuth tAuth;
    private FirebaseUser tUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing firebase objects
        tAuth = FirebaseAuth.getInstance();

        if (tAuth.getCurrentUser() != null){
            startActivity(new Intent(this,Profile_Activity.class));
            finish();
        }




        //Initializing views from layout
        editTextEmail = (EditText) findViewById(R.id.editTextLogIn_Email);
        editTextPassword = (EditText) findViewById(R.id.editTextLogIn_Password);
        buttonLogIn = (Button) findViewById(R.id.button_LogIn);
        textViewRegister = (TextView) findViewById(R.id.textView_Register);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
                    Snackbar.make(view,"Both fields are required",Snackbar.LENGTH_LONG).show();
                }

                tAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            startActivity(new Intent(Login_Activity.this,Profile_Activity.class));
                            finish();
                        }else{
                            Toast.makeText(Login_Activity.this, "Check email or password you entered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Toast.makeText(Login_Activity.this, "Good !", Toast.LENGTH_SHORT).show();

            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this,Register_Email.class));
                finish();
            }
        });

    }

}

