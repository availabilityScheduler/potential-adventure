package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class compareActivity extends AppCompatActivity {

    TextView userName;
    Member thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userName = (TextView) findViewById(R.id.usersName);


        //GoogleSignInAccount acct = GoogleSignIn.getAccountForExtension();


    }
    public class userProfileInfo extends RecyclerView.ViewHolder{

        public userProfileInfo(@NonNull View itemView) {
            super(itemView);
        }
        public void setDetails(Context ctx, String userName, String userID){
            
        }
    }
}