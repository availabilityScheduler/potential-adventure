package com.example.scheduler.finalUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.scheduler.R;
import com.example.scheduler.mainActivities.ThirdActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompareSchedules extends AppCompatActivity {

    private static final String TAG = "";
    private FloatingActionButton backButton;
    private ExpandableListView expandableListView;

    private List<String>  listGroup;
    private HashMap<String, List<String>> listItems;

    MainAdapter adapter;
    private DatabaseReference mUserFriendDatabase;
    private String firebaseAcctId;
    private FirebaseUser currentFirebaseUser;
    private Map<String, Object> saveDay = new HashMap<>();

    //Secondary hasmap placed into saveDay appropriately
    private Map<String, Boolean> mon = new HashMap<>();
    private Map<String, Boolean> tue = new HashMap<>();
    private Map<String, Boolean> wed = new HashMap<>();
    private Map<String, Boolean> thr = new HashMap<>();
    private Map<String, Boolean> fri = new HashMap<>();
    private Map<String, Boolean> sat = new HashMap<>();
    private Map<String, Boolean> sun = new HashMap<>();

    private Map<String, Object> ownUser = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_schedules);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //For the logic
        ArrayList<String> friendList = getTheFriendsToCompare();
        findAvailabilityTimes(friendList);

        //For the UI
        expandableListView = findViewById(R.id.expandableDays);
        listGroup =  new ArrayList<>();
        listItems = new HashMap<>();
        adapter =  new MainAdapter(this, listGroup, listItems);
        expandableListView.setAdapter(adapter);
        getListData();
    }


    private void getMyScheduleData(final MyCallback myCallback){
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAcctId = currentFirebaseUser.getUid();

        mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Schedules").child(firebaseAcctId).child("userSchedule");
        mUserFriendDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> getMyMap = (Map<String, Object>) dataSnapshot.getValue();
                myCallback.onCallback(getMyMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed To Read", databaseError.toException());
            }
        });
    }

    public interface MyCallback {
        void onCallback(Map<String, Object> ownMap);
    }

    private void findAvailabilityTimes(final ArrayList<String> friendList){
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAcctId = currentFirebaseUser.getUid();

        for(int i=0; i<friendList.size();i++){
            mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Schedules");
            mUserFriendDatabase.orderByChild("aName").equalTo(friendList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //HashMap to retrieve higher level of our structure, this retrieves each user's entire object
                        Map<String, Object> getScheduleMap = (Map<String, Object>) dataSnapshot.getValue();
                        System.out.println("getScheduleMap " +getScheduleMap);

                        Iterator it = getScheduleMap.entrySet().iterator();
                        for (int i = 0; it.hasNext(); i++) {
                            Map.Entry pair = (Map.Entry) it.next();
                            //Retrieves User Schedule and the aName for that particular user
                            final Map<String, Object> getFriendsMap = (Map<String, Object>) getScheduleMap.get(pair.getKey());
                            System.out.println("getFriendsMap " +getFriendsMap);

                            if(getFriendsMap.containsKey("userSchedule")){

                                //gets my own data, have to use callback method as onDataChange is a async method
                                getMyScheduleData(new MyCallback() {
                                    @Override
                                    public void onCallback(Map<String, Object> ownMap) {
                                        System.out.println("Own user " + ownMap);
                                        System.out.println("userSchedule " +getFriendsMap.get("userSchedule"));

                                    }
                                });
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed To Read", databaseError.toException());
                }
            });
        }
    }

    private void getListData(){
        System.out.println("Here you want the Days");
        listGroup.add(getString(R.string.Monday));
        listGroup.add(getString(R.string.Tuesday));
        listGroup.add(getString(R.string.Wednesday));
        listGroup.add(getString(R.string.Thursday));
        listGroup.add(getString(R.string.Friday));
        listGroup.add(getString(R.string.Saturday));
        listGroup.add(getString(R.string.Sunday));

        String[] array;
        List<String> monday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Monday);
        for(String item: array){
            monday.add(item);
        }
        List<String> tuesday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Tuesday);
        for(String item: array){
            tuesday.add(item);
        }
        List<String> wednesday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Wednesday);
        for(String item: array){
            wednesday.add(item);
        }
        List<String> thursday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Thursday);
        for(String item: array){
            thursday.add(item);
        }
        List<String> friday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Friday);
        for(String item: array){
            friday.add(item);
        }
        List<String> saturday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Saturday);
        for(String item: array){
            saturday.add(item);
        }
        List<String> sunday = new ArrayList<>();
        array = getResources().getStringArray(R.array.Sunday);
        for(String item: array){
            sunday.add(item);
        }

        listItems.put(listGroup.get(0), monday);
        listItems.put(listGroup.get(1), tuesday);
        listItems.put(listGroup.get(2), wednesday);
        listItems.put(listGroup.get(3), thursday);
        listItems.put(listGroup.get(4), friday);
        listItems.put(listGroup.get(5), saturday);
        listItems.put(listGroup.get(6), sunday);
        adapter.notifyDataSetChanged();


    }


    private ArrayList<String> getTheFriendsToCompare(){
        Bundle extras = getIntent().getExtras();
        ArrayList<String> theFriends = null;
        if(extras != null) {
            theFriends = extras.getStringArrayList("friendsPassedToCompareSchedules");
            //just to see/and test it out
            Toast.makeText(CompareSchedules.this, theFriends.get(0), Toast.LENGTH_SHORT).show();
        }
        return theFriends;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CompareSchedules.this, ThirdActivity.class);
        startActivity(intent);
    }

}




