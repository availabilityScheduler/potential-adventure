package com.example.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserProfile extends AppCompatActivity {

    private TextView userName;
    private Member thisUser;
    private FloatingActionButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        userName = (TextView) findViewById(R.id.usersName);
        backbtn = (FloatingActionButton)findViewById(R.id.bckButtonForUserProfile
        );

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, searchBar.class);
                startActivity(intent);
            }
        });


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