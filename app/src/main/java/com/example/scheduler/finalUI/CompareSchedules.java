package com.example.scheduler.finalUI;


import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.example.scheduler.R;
import com.example.scheduler.mainActivities.ThirdActivity;
import com.example.scheduler.social.searchBar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import java.util.Optional;
import java.util.Set;

public class CompareSchedules extends AppCompatActivity {

    private static final String TAG = "";
    private FloatingActionButton backButton;
    private ExpandableListView expandableListView;

    private String[]  listGroup =  new String[7];
    private HashMap<String, List<String>> listItems;
    HashMap<String, List<String>> finalListItems = new HashMap<>();

    ArrayList<String> friendList;

    MainAdapter adapter;
    private DatabaseReference mUserFriendDatabase;
    private String firebaseAcctId;
    private FirebaseUser currentFirebaseUser;

    ArrayList<String> listOfKeys;

    List<String> monday = new ArrayList<>();
    List<String> tuesday = new ArrayList<>();
    List<String> wednesday = new ArrayList<>();
    List<String> thursday = new ArrayList<>();
    List<String> friday = new ArrayList<>();
    List<String> saturday = new ArrayList<>();
    List<String> sunday = new ArrayList<>();


    private List<String> finalTimeString;

//    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_schedules);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        //For the logic
        friendList = getTheFriendsToCompare();
        scheduleDataRetrieval(friendList);

        //For the UI
        expandableListView = findViewById(R.id.expandableDays);
        listItems = new HashMap<>();

        //back button
        backButton = findViewById(R.id.backButtonInComparison);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                startActivity(intent);
            }
        });

    }


    private void comparisonLogic(Map<String, Map<String, Boolean>> first, Map<String, Map<String, Boolean>> second){


        Set<String> keys = first.keySet();
        listOfKeys = new ArrayList<>(keys);

        Set<String> anotherKey = second.keySet();
        ArrayList<String> AnotherlistOfKeys = new ArrayList<>(anotherKey);

        listOfKeys.retainAll(AnotherlistOfKeys);
        //("simmilar keys " + listOfKeys);


        for(String days: listOfKeys){
            for(Map.Entry<String, Map<String, Boolean>> secondMap : second.entrySet()) {
                String day = secondMap.getKey();
                if(day.equals(days)){
                    Map<String, Boolean> myTime = first.get(day);
                    Map<String, Boolean> theirTime = second.get(day);
                    if(secondMap.getValue().equals(myTime)) {
                        Map<String, String> matchedMap = new HashMap<>();
                        Set<String> arr = myTime.keySet();
                        Optional<String> var = arr.stream().findFirst();
                        String mytime = var.get();
                        matchedMap.put(day, mytime);
                        putTheTimesIntoList(matchedMap);

                    }
                    else if(myTime.entrySet().size() > 1 && theirTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> myTime1 : myTime.entrySet()) {
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(myTime1.getKey(), myTime1.getValue());
                            for(Map.Entry<String, Boolean> theirTime1 : theirTime.entrySet()) {
                                Map<String, Boolean> theirFinale = new HashMap<>();
                                theirFinale.put(theirTime1.getKey(), theirTime1.getValue());
                                if(finale.equals(theirFinale)){
                                    Map<String, String> matchedMap = new HashMap<>();
                                    matchedMap.put(day, theirTime1.getKey());
                                    putTheTimesIntoList(matchedMap);
                                }
                            }
                        }
                    }
                    else if(myTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> myTime1 : myTime.entrySet()) {
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(myTime1.getKey(), myTime1.getValue());
                            if(finale.equals(second.get(day))){
                                Map<String, String> matchedMap = new HashMap<>();
                                matchedMap.put(day, myTime1.getKey());
                                putTheTimesIntoList(matchedMap);
                            }
                        }
                    }
                    else if(theirTime.entrySet().size() > 1){
                        for(Map.Entry<String, Boolean> theirTime1 : theirTime.entrySet()) {
                            Map<String, Boolean> finale = new HashMap<>();
                            finale.put(theirTime1.getKey(), theirTime1.getValue());
                            if(finale.equals(first.get(day))){
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


    private void getsUserSchedules(final ArrayList<String> friendList) {
        for(int i=0; i<friendList.size();i++) {
            mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Schedules");
            mUserFriendDatabase.orderByChild("aName").equalTo(friendList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //HashMap to retrieve higher level of our structure, this retrieves each user's entire object
                    Map<String, Object> getScheduleMap = (Map<String, Object>) dataSnapshot.getValue();
                    //("getScheduleMap " +getScheduleMap);
                    Iterator it = getScheduleMap.entrySet().iterator();
                    for (int i = 0; it.hasNext(); i++) {
                        Map.Entry pair = (Map.Entry) it.next();
                        //Retrieves User Schedule and the aName for that particular user
                        final Map<String, Object> getFriendsMap = (Map<String, Object>) getScheduleMap.get(pair.getKey());
                        //("getFriendsMap " +getFriendsMap);
                        if(getFriendsMap.containsKey("userSchedule")){
                            //gets my own data, have to use callback method as onDataChange is a async method
                            getMyOwnScheduleData(new MyCallback() {
                                @Override
                                public void onCallback(Map<String, Map<String, Boolean>> ownMap) {
                                    Map<String, Map<String, Boolean>> users = (Map<String, Map<String, Boolean>>) getFriendsMap.get("userSchedule");
                                    comparisonLogic(ownMap, users);

                                }
                            });
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

    private void scheduleDataRetrieval(final ArrayList<String> friendList){
        getsUserSchedules(friendList);
    }

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

    private void putTheTimesIntoList(Map<String, String> matchedMap){

        List<String> array = new ArrayList<>();

        if(matchedMap.containsKey("mon")){
            array.add(matchedMap.get("mon"));
            for(String item: array){
                if(item == null){
                }
                else {
                    monday.add(item);
                    setTheDays("Monday");
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
                    setTheDays("Tuesday");
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
                    setTheDays("Wednesday");
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
                    setTheDays("Thursday");
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
                    setTheDays("Friday");
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
                    setTheDays("Saturday");
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
                    setTheDays("Sunday");
                    listItems.put(listGroup[6], sunday);
                }
            }
            array.clear();
        }
    }

    private void setTheDays(String set) {
        if (set.equals("Monday")) {
            listGroup[0] = (getString(R.string.Monday));
        } else if (set.equals("Tuesday")) {
            listGroup[1] = (getString(R.string.Tuesday));
        }
        else if (set.equals("Wednesday")) {
            listGroup[2] = (getString(R.string.Wednesday));
        }
        else if (set.equals("Thursday")) {
            listGroup[3] = (getString(R.string.Thursday));
        }
        else if (set.equals("Friday")) {
            listGroup[4] = (getString(R.string.Friday));
        }
        else if (set.equals("Saturday")) {
            listGroup[5] = (getString(R.string.Saturday));
        }
        else if (set.equals("Sunday")){
            listGroup[6] = (getString(R.string.Sunday));
        }

        if(friendList.size()>1){
            HashMap<String, List<String>> finalListItems =  doTheStuff(set);
            String[] finalDaysArray = new String[finalListItems.size()];
            int count =0;
            for(int i=0;i<listGroup.length;i++){
                for(Map.Entry<String, List<String>> finalItemsKey : finalListItems.entrySet()) {
                    if(listGroup[i] != null && listGroup[i].equals(finalItemsKey.getKey())){
                        finalDaysArray[count] = listGroup[i];
                        count++;
                    }
                }
            }


            List<String> intoTheAdapter = new ArrayList<>(Arrays.asList(finalDaysArray));

            adapter = new MainAdapter(this, intoTheAdapter, finalListItems);
            expandableListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else {
            List<String> intoTheAdapter = new ArrayList<String>(Arrays.asList(listGroup));
            intoTheAdapter.removeAll(Collections.singleton(null));

            adapter = new MainAdapter(this, intoTheAdapter, this.listItems);
            expandableListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private HashMap<String, List<String>> doTheStuff(String day){
        HashMap<String, Integer> map = new HashMap<>();
        Set<String> set1 = new HashSet<>();


        for(Map.Entry<String, List<String>> listMaps : listItems.entrySet()) {
            if(listMaps.getKey().equals(day)){
                for(String entry : listMaps.getValue()) {
                    if (map.containsKey(entry)) {
                        int count = map.get(entry);
                        count=count+1;
                        map.put(entry, count);
                    } else {
                        map.put(entry, 1);
                    }
                }
            }
        }

        for(Map.Entry<String, Integer> mapsVals : map.entrySet()) {
            if(mapsVals.getValue() ==  friendList.size()){
                set1.add(mapsVals.getKey());
                finalTimeString = new ArrayList<>(set1);
                finalListItems.put(day, finalTimeString);
            }
            else{
                listItems.remove(day,mapsVals.getKey());
            }
        }

        return finalListItems;
    }

    //gets the friends to compare
    private ArrayList<String> getTheFriendsToCompare(){
        Bundle extras = getIntent().getExtras();
        ArrayList<String> theFriends = null;
        if(extras != null) {
            theFriends = extras.getStringArrayList("friendsPassedToCompareSchedules");
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




