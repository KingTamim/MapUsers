package com.mangolab.mapusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Register extends AppCompatActivity {


    private EditText editTextName,editTextBusinessName,editTextAddress;
    private Button buttonSubmit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private StorageReference imageStorage;
    private ProgressDialog progressDialog;
    private ImageButton imageButtonProfilePicture;
    private Uri resultUri = null;
    private static final int GALLERY_REQUEST = 2;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        imageStorage = FirebaseStorage.getInstance().getReference();


        imageButtonProfilePicture = (ImageButton) findViewById(R.id.imageButtonRegister_PP);
        editTextName = (EditText) findViewById(R.id.editTextRegister_Name);
        editTextBusinessName = (EditText) findViewById(R.id.editTextRegister_Business);
        editTextAddress = (EditText) findViewById(R.id.editTextRegister_Address);
        buttonSubmit = (Button) findViewById(R.id.buttonRegister_Submit);

        progressDialog = new ProgressDialog(this);




        imageButtonProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextName.getText().toString().trim();
                String bName= editTextBusinessName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String userId = currentUser.getUid().toString();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(bName) || TextUtils.isEmpty(address) ){
                    Snackbar.make(view,"All fields are required", Snackbar.LENGTH_LONG).show();

                }
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(bName) && !TextUtils.isEmpty(address) && resultUri != null ){
                    progressDialog.setMessage("Saving User Data");
                    progressDialog.show();

                    final UserData storeUserData = new UserData(name,bName,address, userId); //, latitude, longitude);

                    StorageReference tempRef = imageStorage.child("profile_pictures").child(resultUri.getLastPathSegment());

                    tempRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            databaseReference.child("profile_picture").setValue(downloadUrl.toString());
                            databaseReference.setValue(storeUserData);
                            startActivity(new Intent(Register.this,MapsActivity.class));
                            Toast.makeText(Register.this, "Information saved successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                }
                progressDialog.dismiss();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }else{

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                imageButtonProfilePicture.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }


    }
}
