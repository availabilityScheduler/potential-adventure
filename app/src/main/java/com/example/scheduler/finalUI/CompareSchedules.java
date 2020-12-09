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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompareSchedules extends AppCompatActivity {

    private static final String TAG = "";
    private FloatingActionButton backButton;
    private ExpandableListView expandableListView;

    private String[]  listGroup =  new String[7];
    private HashMap<String, List<String>> listItems;

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

    //Main hashmap to save and push schedule to db
    private Map<String, Map<String, Boolean>> saveDay = new HashMap<>();

    //Secondary hasmap placed into saveDay appropriately
    private Map<String, Boolean> mon = new HashMap<>();
    private Map<String, Boolean> tue = new HashMap<>();
    private Map<String, Boolean> wed = new HashMap<>();
    private Map<String, Boolean> thr = new HashMap<>();
    private Map<String, Boolean> fri = new HashMap<>();
    private Map<String, Boolean> sat = new HashMap<>();
    private Map<String, Boolean> sun = new HashMap<>();

    private Map<String, Map<String, Boolean>> smth = new HashMap<>();


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

    private void handleIfForHashmaps(String theDay, String theTime) {
        if (theDay.equals("mon")){
                mon.put(theTime, true);
                saveDay.put(theDay, mon);
        }
        else if(theDay.equals("tue")) {

                tue.put(theTime, true);
            saveDay.put(theDay, tue);
        }
        else if(theDay.equals("wed")) {

            wed.put(theTime, true);
            saveDay.put(theDay, wed);
        }else if(theDay.equals("thr")) {

            thr.put(theTime, true);
            saveDay.put(theDay, thr);
        }else if(theDay.equals("fri")) {

            fri.put(theTime, true);
            saveDay.put(theDay, fri);
        }else if(theDay.equals("sat")) {

            sat.put(theTime, true);
            saveDay.put(theDay, sat);
        }else if(theDay.equals("sun")) {

            sun.put(theTime, true);
            saveDay.put(theDay, sun);
        }

        System.out.println("Final Saveday before Saving "+ Arrays.asList(saveDay));
    }

    private Map<String, Map<String, Boolean>> theFunction(Map<String, Map<String, Boolean>> first, Map<String, Map<String, Boolean>> second){
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
                if(day.equals(days)){
                    Map<String, Boolean> myTime = first.get(day);
                    Map<String, Boolean> theirTime = second.get(day);
                    if(secondMap.getValue().equals(myTime)) {
                        Map<String, String> matchedMap = new HashMap<>();
                        tempTime = myTime.keySet().toString().replace(']', ' ');
                        tempTime = tempTime.replace('[', ' ');
                        matchedMap.put(day, tempTime);
                        handleIfForHashmaps(day, tempTime);
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
                                    handleIfForHashmaps(day, theirTime1.getKey());
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
                                handleIfForHashmaps(day, myTime1.getKey());
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
                                handleIfForHashmaps(day, theirTime1.getKey());
                                putTheTimesIntoList(matchedMap);
                            }
                        }
                    }
                }
            }
        }
        return saveDay;
    }


    private void getsUserSchedules(Map<String, Map<String, Boolean>> first, final String friendList) {

        System.out.println("friend " + friendList);
        if(!friendList.isEmpty()){
            mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Schedules");
            mUserFriendDatabase.orderByChild("aName").equalTo(friendList).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        Map<String, Map<String, Boolean>> users = (Map<String, Map<String, Boolean>>) getFriendsMap.get("userSchedule");
                                        smth = theFunction(ownMap, users);
                                        //scheduleDataRetrieval(friends, smth);
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

    private void scheduleDataRetrieval(final ArrayList<String> friendList){
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAcctId = currentFirebaseUser.getUid();

//        final int[] count = {0};
//        for(int i=0; i<friendList.size();i++) {
            getMyOwnScheduleData(new MyCallback() {
                @Override
                public void onCallback(Map<String, Map<String, Boolean>> ownMap) {
                    getsUserSchedules(ownMap, friendList.get(0));

                    for(int i=1; i<friendList.size();i++) {
                        getsUserSchedules(ownMap, friendList.get(i));
                    }
                }
            });
//        }
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

    private void putTheTimesIntoList(Map<String, String> matchedMap){

        System.out.println("matchedTimesMap " +  matchedMap);
        List<String> array = new ArrayList<>();


        if(matchedMap.containsKey("mon")){
            array.add(matchedMap.get("mon"));
            for(String item: array){
                if(item == null){
                }
                else {
                    monday.add(item);
                    setTheDays("mon");
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
                    setTheDays("tue");
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
                    setTheDays("wed");
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
                    setTheDays("thr");
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
                    setTheDays("fri");
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
                    setTheDays("sat");
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
                    setTheDays("sun");
                    listItems.put(listGroup[6], sunday);
                }
            }
            array.clear();
        }
    }

    private void setTheDays(String set) {
        if(set.contains("mon"))
            listGroup[0] = (getString(R.string.Monday));
        else if(set.contains("tue"))
            listGroup[1] = (getString(R.string.Tuesday));
        else if(set.contains("wed"))
            listGroup[2] = (getString(R.string.Wednesday));
        else if(set.contains("thr"))
            listGroup[3] = (getString(R.string.Thursday));
        else if(set.contains("fri"))
            listGroup[4] = (getString(R.string.Friday));
        else if(set.contains("sat"))
            listGroup[5] = (getString(R.string.Saturday));
        else if(set.contains("sun"))
            listGroup[6] = (getString(R.string.Sunday));

        //list needs to go into adapter as an arraylist of string, so conversion here
        List<String> intoTheAdapter = new ArrayList<String>(Arrays.asList(listGroup));

        //removing the null values that were created
        intoTheAdapter.removeAll(Collections.singleton(null));

        adapter =  new MainAdapter(this, intoTheAdapter, this.listItems);
        expandableListView.setAdapter(adapter);
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




