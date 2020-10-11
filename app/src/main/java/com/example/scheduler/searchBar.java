//package com.example.scheduler;
//
//import android.app.SearchManager;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//
//import android.view.View;
//import android.content.Context;
//import android.widget.Toast;
//
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//
//
//import com.bumptech.glide.Glide;
//
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//
//public class searchBar extends AppCompatActivity {
//    private EditText mSearchField;
//    private ImageButton mSearchButton;
//    private RecyclerView mResultList;
//    private DatabaseReference mUserDatabase;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search_bar);
//
//        mSearchField = (EditText) findViewById(R.id.search_field);
//        mSearchButton = (ImageButton) findViewById(R.id.search_btn);
//        mResultList = (RecyclerView) findViewById(R.id.result_list);
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
//
//        mResultList.setHasFixedSize(true);
//        mResultList.setLayoutManager(new LinearLayoutManager(this));
//
//        mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String searchText = mSearchField.getText().toString();
//                firebaseUserSearch(searchText);
//
//            }
//        });
//    }
//
//    private void firebaseUserSearch(String searchText) {
//        Toast.makeText(searchBar.this, "Started Search", Toast.LENGTH_LONG).show();
//        Query firebaseSearchQuery =
//                mUserDatabase.orderByChild("aName").startAt(searchText).endAt(searchText + "\uf8ff");
//
//        FirebaseRecyclerOptions personsOptions =
//                new FirebaseRecyclerOptions.Builder<Member>().setQuery(firebaseSearchQuery, Member.class).build();
//
//        FirebaseRecyclerAdapter mPeopleRVAdapter =
//                new FirebaseRecyclerAdapter<Member, userViewHolder>(personsOptions) {
//            @Override
//            protected void onBindViewHolder(userViewHolder holder, int position, Member model) {
//                holder.setDetails(getApplicationContext(), model.getaName(), model.getID());
//            }
//
//            @Override
//            public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
//                return new userViewHolder(view);
//            }
//        };
//
//        mResultList.setAdapter(mPeopleRVAdapter);
//
//    }
//
//
//    // View Holder Class
//    public static class userViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public userViewHolder(View itemView) {
//            super(itemView);
//            //initalize content from userlistlayout
//            mView = itemView;
//
//        }
//
//        public void setDetails(Context ctx, String userName, String userID){
//
//            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
//            TextView user_id = (TextView) mView.findViewById(R.id.userID);
//            //user_image = (ImageView) mView.findViewById(R.id.profile_image);
//
//            user_name.setText(userName);
//            user_id.setText(userID);
//
//            //Glide.with(ctx).load(userImage).into(user_image);
//
//
//        }
//    }
//}
package com.example.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class searchBar extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";
    private CalendarView calendarView;
    TextView myDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                Log.d(TAG, "onSelectedDatChange: mm/dd/yyyy:" + date);

                Intent intent = new Intent(searchBar.this, ThirdActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}