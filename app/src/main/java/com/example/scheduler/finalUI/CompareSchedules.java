package com.example.scheduler.finalUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CompareSchedules extends AppCompatActivity {

    private static final String TAG = "";
    private FloatingActionButton backButton;
    private ExpandableListView expandableListView;

    private String[]  listGroup =  new String[7];
    Set<String> set = new HashSet<>();
    private HashMap<String, List<String>> listItems;

    MainAdapter adapter;
    private DatabaseReference mUserFriendDatabase;
    private String firebaseAcctId;
    private FirebaseUser currentFirebaseUser;

    ArrayList<String> listOfKeys;
    ArrayList<String> finalKeys =  new ArrayList<String>();


    List<String> monday = new ArrayList<>();
    List<String> tuesday = new ArrayList<>();
    List<String> wednesday = new ArrayList<>();
    List<String> thursday = new ArrayList<>();
    List<String> friday = new ArrayList<>();
    List<String> saturday = new ArrayList<>();
    List<String> sunday = new ArrayList<>();

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
        listItems = new HashMap<>();

    }

    private void setTheDays(Set<String> set) {
        if(set.contains("mon"))
            listGroup[0] = (getString(R.string.Monday));
        if(set.contains("tue"))
            listGroup[1] = (getString(R.string.Tuesday));
        if(set.contains("wed"))
            listGroup[2] = (getString(R.string.Wednesday));
        if(set.contains("thr"))
            listGroup[3] = (getString(R.string.Thursday));
        if(set.contains("fri"))
            listGroup[4] = (getString(R.string.Friday));
        if(set.contains("sat"))
            listGroup[5] = (getString(R.string.Saturday));
        if(set.contains("sun"))
            listGroup[6] = (getString(R.string.Sunday));

        System.out.println("this is listitem " + listItems);
        System.out.println("this is listitem.size " + listItems.size());

        List<String> intoTheAdapter = Arrays.asList(listGroup);
        adapter =  new MainAdapter(this, intoTheAdapter, this.listItems);
        expandableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    private void compareSchedules(Map<String, Map<String, Boolean>> first, Map<String, Map<String, Boolean>> second) {
        System.out.println("userSchedule " + second);
        System.out.println("myschedule " + first);


        Set<String> keys = first.keySet();
        listOfKeys = new ArrayList<>(keys);

        Set<String> anotherKey = second.keySet();
        ArrayList<String> AnotherlistOfKeys = new ArrayList<>(anotherKey);

        listOfKeys.retainAll(AnotherlistOfKeys);
        System.out.println("simmilar keys " + listOfKeys);
        String tempTime;


        for(String days: listOfKeys){
            for(Map.Entry<String, Map<String, Boolean>> secondMap : second.entrySet()) {
                String day = secondMap.getKey();
                //System.out.println("day Retrieved from user's " + day);
                if(day.equals(days)){
                    Map<String, Boolean> myTime = first.get(day);
                    Map<String, Boolean> theirTime = second.get(day);
                    //System.out.println("my time pertaining to that day " + myTime); //{6am= true}
                    //System.out.println("Their time pertaining to that day " + theirTime); //{6am= true, 8am = true}
                    if(secondMap.getValue().equals(myTime)) {
//                        System.out.println("DayMatched " + day);
//                        System.out.println("FriendTimeMatched " + secondMap.getValue());
//                        System.out.println("myTime " + myTime);
                        Map<String, String> matchedMap = new HashMap<>();
                        tempTime = myTime.keySet().toString().replace(']', ' ');
                        tempTime = tempTime.replace('[', ' ');
                        matchedMap.put(day, tempTime);
                        putTheTimesIntoList(matchedMap);

                    }
                    else if(myTime.entrySet().size() > 1 && theirTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> myTime1 : myTime.entrySet()) {
                            //System.out.println("mytime1.entryset " + myTime1);
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(myTime1.getKey(), myTime1.getValue());
                            for(Map.Entry<String, Boolean> theirTime1 : theirTime.entrySet()) {
                                //System.out.println("theirTime1.entryset " + theirTime1);
                                Map<String, Boolean> theirFinale = new HashMap<>();
                                theirFinale.put(theirTime1.getKey(), theirTime1.getValue());
                                if(finale.equals(theirFinale)){
//                                    System.out.println("Matched day " + day);
//                                    System.out.println("Matched Time " + finale);
                                        //System.out.println("TheirMatched Time " + theirFinale);
                                    Map<String, String> matchedMap = new HashMap<>();
                                    matchedMap.put(day, theirTime1.getKey());
                                    putTheTimesIntoList(matchedMap);
                                }
                            }
                        }
                    }
                    else if(myTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> myTime1 : myTime.entrySet()) {
                            //System.out.println("mytime1.entryset " + myTime1);
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(myTime1.getKey(), myTime1.getValue());
                            if(finale.equals(second.get(day))){
//                                System.out.println("Matched day " + day);
//                                System.out.println("Matched Time " + finale);
                                Map<String, String> matchedMap = new HashMap<>();
                                matchedMap.put(day, myTime1.getKey());
                                putTheTimesIntoList(matchedMap);
                            }
                        }
                    }
                    else if(theirTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> theirTime1 : theirTime.entrySet()) {
                           // System.out.println("theirTime1.entryset " + theirTime1);
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(theirTime1.getKey(), theirTime1.getValue());
                            if(finale.equals(first.get(day))){
//                                System.out.println("Matched day " + day);
//                                System.out.println("Matched Time " + finale);
                                Map<String, String> matchedMap = new HashMap<>();
                                matchedMap.put(day, theirTime1.getKey());
                                putTheTimesIntoList(matchedMap);
                            }
                        }
                    }
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


    private void putTheTimesIntoList(Map<String, String> matchedMap){

        System.out.println("matchedTimesMap " +  matchedMap);
        String notAvailable = "No available times today :(";
        List<String> array = new ArrayList<>();

        for(Map.Entry<String, String> secondMap : matchedMap.entrySet()) {
            finalKeys.add(secondMap.getKey());
        }
        for(int i = 0; i < finalKeys.size(); i++){
            set.add(finalKeys.get(i));
        }
        System.out.println(set);


        if(matchedMap.containsKey("mon")){
            array.add(matchedMap.get("mon"));
            for(String item: array){
                if(item == null){
                }
                else {
                    monday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[0], monday);
                }
            }
            array.clear();

        }
        else if(matchedMap.containsKey("tue")){
            array.add(matchedMap.get("tue"));
            for (String item : array) {
                if(item == null){
                }
                else {
                    tuesday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[1], tuesday);
                }
            }
            array.clear();
        }
        else if(matchedMap.containsKey("wed")){
            array.add(matchedMap.get("wed"));
            for (String item : array) {
                if(item == null){
                }
                else {
                    wednesday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[2], wednesday);
                }
            }
            array.clear();

        }
        else if(matchedMap.containsKey("thr")){
            array.add(matchedMap.get("thr"));
            for (String item : array) {
                if(item == null){

                }
                else {
                    thursday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[3], thursday);
                }
            }
            array.clear();
        }
        else if(matchedMap.containsKey("fri")){
            array.add(matchedMap.get("fri"));
            for (String item : array) {
                if(item == null){
                }
                else {
                    friday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[4], friday);
                }
            }
            array.clear();
        }
        else if(matchedMap.containsKey("sat")){
            array.add(matchedMap.get("sat"));
            for (String item : array) {
                if(item == null){
                }
                else {
                    saturday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[5], saturday);
                }
            }
            array.clear();
        }
        else if(matchedMap.containsKey("sun")){
            array.add(matchedMap.get("sun"));
            for (String item : array) {
                if(item == null){
                }
                else {
                    sunday.add(item);
                    setTheDays(set);
                    listItems.put(listGroup[6], sunday);
                }
            }
            array.clear();
        }
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




