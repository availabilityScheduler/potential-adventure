package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "";

    TextView getToLoginPageFromRegister;


    EditText mFullName, mEmail, mPassword;
    Member thisMember;

    EditText mPhone;

    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    DatabaseReference db;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.editTextEmail);
        mPassword   = findViewById(R.id.editTextTextPassword);
        //mPhone      = findViewById(R.id.phone);
        thisMember = new Member();


        mRegisterBtn= findViewById(R.id.registerBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Users");


        //Redirects to login page from register page
        getToLoginPageFromRegister = (TextView)findViewById(R.id.getToLoginPageFromRegister);
        getToLoginPageFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        //Commented this out, broke everything lmaooooooo
//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), Register.class));
//            finish();
//        }

        //Not Complete yet
        //Needs to redirect to login page
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                //final String phone    = mPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length() < 8){
                    mPassword.setError("Password Must be >= 8 Characters, Hehe");
                    return;
                }

                //Registering the user in firebase
                fAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //Sending the verification link
                            FirebaseUser m_user = fAuth.getCurrentUser();
                            m_user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fStore.collection("users").document(userID);
//                            Map<String,Object> user = new HashMap<>();
                            thisMember.setaName(fullName);
                            thisMember.setID(email);
//                            thisMember.setFirstName(personFirstName);
//                            thisMember.setLastName(personLastName);
//                            user.put("fName", fullName);
//                            user.put("email", email);
//                            user.put("phone",phone);
                            db.child(userID).setValue(thisMember);

//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d(TAG, "onFailure: " + e.toString());
//                                }
//                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Google Sign in//

    }
}