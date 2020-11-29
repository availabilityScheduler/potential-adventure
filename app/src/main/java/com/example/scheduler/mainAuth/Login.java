package com.example.scheduler.mainAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduler.mainActivities.MainActivity;
import com.example.scheduler.mainActivities.Member;
import com.example.scheduler.R;
import com.example.scheduler.mainActivities.ThirdActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {



    //buttons and stuff yk
    private EditText mEmail,mPassword;
    private Button mLoginBtn;
    private TextView mCreateBtn;
    private TextView forgotTextLink;
    private TextView createAccountFromLoginPage;

    // to actually display the login-ed user
    private NavigationView mNavigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private CircleImageView mPhoto;


    //for auth and database
    private FirebaseAuth fAuth;
    private DatabaseReference db;
    private Member thisMember;

    //GSO!!
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    private MainActivity mBinding;
    private int RC_SIGN_IN = 9001;

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //the stuff yk
        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextTextPassword);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);

        //instance of member class
        thisMember = new Member();

        //not sure if i need this for login
        db = FirebaseDatabase.getInstance().getReference().child("Users");


        //Redirects to register page
        createAccountFromLoginPage = (TextView) findViewById(R.id.createAccountFromLoginPage);
        createAccountFromLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });


        //Login Logic seems to work in terms of auth but does not show up in the nav bar
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 8 Characters");
                    return;
                }


                // authenticating the user wOOPWOOP
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ThirdActivity.class));
                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //google sign in

        signInButton = findViewById(R.id.sign_in_button);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Log.d(TAG, "gso:" + gso);

        //Creates sign in client with specified options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Log.d(TAG, "mgooglesigninclient:" + mGoogleSignInClient);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }







    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(Login.this, ThirdActivity.class));
        }
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "currentUser:" + currentUser);
        updateUI(currentUser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestcodde:" + requestCode);
        Log.d(TAG, "RCSIGNIN:" + RC_SIGN_IN);
        Log.d(TAG, "data:" + data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "account:" + account);
                Log.d(TAG, "accountidtoken:" + account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Log.d(TAG, "cred:" + credential);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, ThirdActivity.class));
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //mBinding.eMail.setText(user.getEmail());
            //mBinding.userName.setText(user.getUid());

            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();
            signInButton.setVisibility(View.VISIBLE);
            //mBinding.signOutAndDisconnect.setVisibility(View.VISIBLE);

        } else {
            //mBinding.eMail.setText(null);
            //mBinding.userName.setText(null);
            //signInButton.setVisibility(View.GONE);
            //mBinding.signOutAndDisconnect.setVisibility(View.GONE);
        }
    }
}