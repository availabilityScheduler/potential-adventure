package com.example.scheduler.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.scheduler.mainActivities.Member;
import com.example.scheduler.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class UserProfile extends AppCompatActivity {

    private TextView userName;
    private Member thisUser;
    private FloatingActionButton backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles);

        userName = (TextView) findViewById(R.id.usersName);
        backbtn = (FloatingActionButton)findViewById(R.id.bckButtonForUserProfile);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, searchBar.class);
                startActivity(intent);
            }
        });


        //GoogleSignInAccount acct = GoogleSignIn.getAccountForExtension();


    }
    private class userProfileInfo extends RecyclerView.ViewHolder{

        public userProfileInfo(@NonNull View itemView) {
            super(itemView);
        }
        public void setDetails(Context ctx, String userName, String userID){
            
        }
    }
}