package com.example.scheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import android.view.View;
import android.content.Context;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class searchBar extends AppCompatActivity {
    private static final String TAG = "";

    private EditText mSearchField;
    private ImageButton mSearchButton;

    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);


        //Retrieving Id's
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchButton = (ImageButton) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) this.findViewById(R.id.result_list);

        //Retrieving Users Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getBaseContext()));


        //Working but bug--> gotta pull the keyboard down to see result(at least in my oneplus7)
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);

            }
        });




        //visual feedback of click
        mSearchButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        //For when pressing enter to get search
            final EditText searchField = (EditText) findViewById(R.id.search_field);
            searchField.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String searchText = mSearchField.getText().toString().toLowerCase();
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        firebaseUserSearch(searchText);
                        return true;
                    }
                    return false;
                }
            });
    }



    //Main logic to search users
    private void firebaseUserSearch(final String searchText) {
        Toast.makeText(searchBar.this, "Started Search", Toast.LENGTH_LONG).show();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("aName").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions personsOptions =
                new FirebaseRecyclerOptions.Builder<Member>().setQuery(firebaseSearchQuery, Member.class).build();

        FirebaseRecyclerAdapter mPeopleRVAdapter =
                new FirebaseRecyclerAdapter<Member, userViewHolder>(personsOptions) {
                    @Override
                    public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
                        return new userViewHolder(view);
                    }
                    @Override
                    protected void onBindViewHolder(userViewHolder holder, int position, Member model) {
                        holder.setDetails(getApplicationContext(), model.getaName(), model.getID());

                    }
                };
        mPeopleRVAdapter.startListening();
        mResultList.setAdapter(mPeopleRVAdapter);

    }


    // View Holder Class
    public class userViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public userViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userID) {
            final TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            final TextView theEmail = (TextView) mView.findViewById(R.id.userID);


            user_name.setText(userName);
            theEmail.setText(userID);

            Button add_button = (Button)mView.findViewById(R.id.add_friends);
            add_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(searchBar.this, "Adding friend", Toast.LENGTH_LONG).show();
                    final TextView user_name = (TextView) findViewById(R.id.name_text);
                    String username = user_name.getText().toString();
                    writeFriendData(username);
                    Intent intent = new Intent(searchBar.this, ThirdActivity.class);
                    startActivity(intent);

                }
            });
        }
    }



    public void writeFriendData(String username){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebaseAcctId =  currentFirebaseUser.getUid();

        //creating a new category of friend and under your own ID
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseAcctId);
        Member member = new Member();

        //the object pushed to the database
        Map<String, Object> friendDbHashMap = new HashMap<>();

        //local db in member class
        Map<String, Boolean> memberMap =  new HashMap<>();
        //Storing the username and boolean value, and setting it up
        memberMap.put(username, true);
        member.setMemberMap(memberMap);

        //passing local db as the object value into this database
        friendDbHashMap.put(username, memberMap);
        mUserDatabase.updateChildren(friendDbHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(searchBar.this, "Friend Added", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(searchBar.this, "Adding Unsuccessful", Toast.LENGTH_LONG).show();
            }
        });
    }



}



