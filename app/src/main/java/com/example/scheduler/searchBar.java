package com.example.scheduler;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import android.view.View;
import android.content.Context;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.firebase.ui.database.FirebaseRecyclerAdapter;


import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchButton = (ImageButton) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) this.findViewById(R.id.result_list);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        //onClick
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);

            }
        });

        //For when pressing enter to get search"
        final EditText edittext = (EditText) findViewById(R.id.search_field);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchText = mSearchField.getText().toString();
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

    private void firebaseUserSearch(String searchText) {
        Toast.makeText(searchBar.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("aName").startAt(searchText).endAt(searchText + "\uf8ff");

        System.out.println(firebaseSearchQuery);

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
    public static class userViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public userViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userID) {
            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_id = (TextView) mView.findViewById(R.id.userID);
            System.out.println(user_name);
            //user_image = (ImageView) mView.findViewById(R.id.profile_image);

            user_name.setText(userName);
            user_id.setText(userID);
            //Glide.with(ctx).load(userImage).into(user_image);
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



