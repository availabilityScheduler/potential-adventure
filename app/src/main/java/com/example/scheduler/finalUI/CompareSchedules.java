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
import com.google.firestore.v1.Value;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        scheduleDataRetrieval(friendList);

        //For the UI
        expandableListView = findViewById(R.id.expandableDays);
        listGroup =  new ArrayList<>();
        listItems = new HashMap<>();
        adapter =  new MainAdapter(this, listGroup, listItems);
        expandableListView.setAdapter(adapter);
        getListData();
    }

    private void compareSchedules(Map<String, Map<String, Boolean>> first, Map<String, Map<String, Boolean>> second) {

        //There probably is a better way to do this, ive got tunnel vision rn lol, but ive commented so u know what I was thinking at least.
        //It works if availability times are single values but yeah its really not flexible

        //For multi values of time, its working if say we were to compare Alex's-thu{10am=true, 12pm=true} with Mahir-thu{12pm=true}---this would work, but if alex
        //had one value and mahir had two, it wont work properly --> probably because the main for loop is dedicated to secondmap entries

        // Please test it out and/or fix or entirely make it anew, up to you lol.

        for(Map.Entry<String, Map<String, Boolean>> secondMap : second.entrySet()) {
            //gets the day from second user
            String day = secondMap.getKey();
            //gets the time pertaining to that day from the first user's
            Map<String, Boolean> myTime = first.get(day);

            //if the value has more than one availability times
            if(secondMap.getValue().entrySet().size() > 1){
                //iterator of the entrysets ==> ex {7am = true, 8am = true}
                Iterator<Map.Entry<String, Boolean>> j = secondMap.getValue().entrySet().iterator();
                while(j.hasNext()){
                    //j.next ==>  //{7am = true} ---> following iter --->{8am = true}
                    Map.Entry<String, Boolean> smth = j.next();
                    //creating a temp hashmap
                    Map<String, Boolean> finale = new HashMap<>();
                    //puts the key, value pair {7am = true} inside it, had to to do this to compare below, then {8am = true}
                    finale.put(smth.getKey(), smth.getValue());
                    System.out.println("finale " + finale);

                    //comparing that with {7am = true} != myTime{8am = true}, but the secound time around its a match! {8am = true}
                    if (finale.equals(myTime)) {
                        System.out.println("DayMatched " + day);
                        System.out.println("FriendTimeMatched " + finale);
                        System.out.println("myTime " + myTime);
                    }

                }

            }
            //if the value of second user ex: {7am = true} == firstuser ex {7am = true} ---then its a match, couldnt find a way to do this with
            // multiple value so had to make the other if statement
            else{
                if (secondMap.getValue().equals(myTime)) {
                    System.out.println("DayMatched " + day);
                    System.out.println("FriendTimeMatched " + secondMap.getValue());
                    System.out.println("myTime " + myTime);

                }
            }
        }
    }

    //To retrieve my own schedule data
    private void getMyOwnScheduleData(final MyCallback myCallback){
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAcctId = currentFirebaseUser.getUid();

        mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Schedules").child(firebaseAcctId).child("userSchedule");
        mUserFriendDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Map<String, Boolean>> getMyMap = (Map<String, Map<String, Boolean>>) dataSnapshot.getValue();
                myCallback.onCallback(getMyMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed To Read", databaseError.toException());
            }
        });
    }

    //callback to store my own schedule data as ondatachanged is async
    public interface MyCallback {
        void onCallback(Map<String, Map<String, Boolean>> ownMap);
    }

    private void scheduleDataRetrieval(final ArrayList<String> friendList){
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
                        //System.out.println("getScheduleMap " +getScheduleMap);

                        Iterator it = getScheduleMap.entrySet().iterator();
                        for (int i = 0; it.hasNext(); i++) {
                            Map.Entry pair = (Map.Entry) it.next();
                            //Retrieves User Schedule and the aName for that particular user
                            final Map<String, Object> getFriendsMap = (Map<String, Object>) getScheduleMap.get(pair.getKey());
                            //System.out.println("getFriendsMap " +getFriendsMap);

                            if(getFriendsMap.containsKey("userSchedule")){

                                //gets my own data, have to use callback method as onDataChange is a async method
                                getMyOwnScheduleData(new MyCallback() {
                                    @Override
                                    public void onCallback(Map<String, Map<String, Boolean>> ownMap) {
                                        //System.out.println("Own user " + ownMap);
                                        System.out.println("userSchedule " +getFriendsMap.get("userSchedule"));
                                        System.out.println("myschedule " +ownMap);

                                        //maybe we could implement a recursive sol here?, where the next user will get matched with
                                        //the just-newly-matched schedule
                                        compareSchedules(ownMap, (Map<String, Map<String, Boolean>>) getFriendsMap.get("userSchedule"));



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

    //Ideally we would save the matching days and times in here for it to show up in the UI, right now its just values from
    //string resources. You can have a alook at the mainadapter to see how it works
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

    //gets the friends to compare
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




