package com.example.scheduler;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class searchBar extends AppCompatActivity {
    private EditText mSearchField;
    private ImageButton mSearchButton;
    private RecyclerView mResultList;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);


        //Retrieving Id's
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchButton = (ImageButton) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) this.findViewById(R.id.result_list);

        //Retrieving Users Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

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
    //Main logic Rn, should be updated to include lowercase searching
    //Keyboard could pop up right away when you click on the FAB
    private void firebaseUserSearch(String searchText) {
        //Toast.makeText(searchBar.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("aName").startAt(searchText).endAt(searchText + "\uf8ff");


        FirebaseRecyclerOptions personsOptions =
                new FirebaseRecyclerOptions.Builder<Member>().setQuery(firebaseSearchQuery, Member.class).build();

        FirebaseRecyclerAdapter mPeopleRVAdapter =
                new FirebaseRecyclerAdapter<Member, userViewHolder>(personsOptions) {

                    @Override
                    protected void onBindViewHolder(userViewHolder holder, int position, Member model) {
                        holder.setDetails(getApplicationContext(), model.getaName(), model.getID());

                    }


                    @Override
                    public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
                        return new userViewHolder(view);
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
            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_id = (TextView) mView.findViewById(R.id.userID);
            Button add_button = (Button) mView.findViewById(R.id.add_friends);
            Button view_profile = (Button) mView.findViewById(R.id.view_profile);

            System.out.println("haha" + user_name);
            //user_image = (ImageView) mView.findViewById(R.id.profile_image);

            user_name.setText(userName);
            user_id.setText(userID);
            add_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(searchBar.this, "Adding friend", Toast.LENGTH_LONG).show();
                    //add button redirects now, all we gotta do now is to handle the logic of adding friend to db in another class o
                    Intent intent = new Intent(searchBar.this, ThirdActivity.class);
                    startActivity(intent);
                }
            });
            view_profile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(searchBar.this, userProfile.class);
                    startActivity(intent);
                }

            } );

        }

    }




}

//works locally, will print out users
//    private void firebaseUserSearch(String searchText){
//        Toast.makeText(searchBar.this, "Started Search", Toast.LENGTH_LONG).show();
//        Query firebaseSearchQuery = mUserDatabase.orderByChild("aName").startAt(searchText).endAt(searchText + "\uf8ff");
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference childDB = db.child("Users");
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String name = ds.child("aName").getValue(String.class);
//                    Log.d("TAG", name);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        };
//        childDB.addListenerForSingleValueEvent(eventListener);
//    }



