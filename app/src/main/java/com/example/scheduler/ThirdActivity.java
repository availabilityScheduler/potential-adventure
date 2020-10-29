package com.example.scheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

//expanding menu and stuff
public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    //Navigation Stuff
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView mNavigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    //Fab
    private FloatingActionButton fab;

    //Instance Member
    Member thisMember;

    //DB instance normal
    DatabaseReference db;

    //Google and nav display
    GoogleSignInClient mGoogleSignInClient;
    Button mSign_out;
    TextView firstName;
    TextView lastName;
    TextView mName;
    TextView mEmail;
    TextView id;
    CircleImageView mPhoto;

    //Dialog box button
    private Button openFriendsDialog;

    //For dialog box retrieving friends
    private DatabaseReference mUserFriendDatabase;
    private String firebaseAcctId;
    private int friendCount;
    String friendList[];
    private static final String EXTRA_MESSAGE = "";


    //Tag string
    private static final String TAG = "ThirdActivity";

    //Button theory
    private int[][] buttonViewIds = new int[][] {
            { R.id.mon6am, R.id.tue6am, R.id.wed6am, R.id.thr6am, R.id.fri6am, R.id.sat6am, R.id.sun6am },
            { R.id.mon7am, R.id.tue7am, R.id.wed7am, R.id.thr7am, R.id.fri7am, R.id.sat7am, R.id.sun7am },
            { R.id.mon8am, R.id.tue8am, R.id.wed8am, R.id.thr8am, R.id.fri8am, R.id.sat8am, R.id.sun8am },
            { R.id.mon9am, R.id.tue9am, R.id.wed9am, R.id.thr9am, R.id.fri9am, R.id.sat9am, R.id.sun9am },
            { R.id.mon10am, R.id.tue10am, R.id.wed10am, R.id.thr10am, R.id.fri10am, R.id.sat10am, R.id.sun10am },
            { R.id.mon11am, R.id.tue11am, R.id.wed11am, R.id.thr11am, R.id.fri11am, R.id.sat11am, R.id.sun11am },
            { R.id.mon12pm, R.id.tue12pm, R.id.wed12pm, R.id.thr12pm, R.id.fri12pm, R.id.sat12pm, R.id.sun12pm },
            { R.id.mon1pm, R.id.tue1pm, R.id.wed1pm, R.id.thr1pm, R.id.fri1pm, R.id.sat1pm, R.id.sun1pm },
            { R.id.mon2pm, R.id.tue2pm, R.id.wed2pm, R.id.thr2pm, R.id.fri2pm, R.id.sat2pm, R.id.sun2pm },
            { R.id.mon3pm, R.id.tue3pm, R.id.wed3pm, R.id.thr3pm, R.id.fri3pm, R.id.sat3pm, R.id.sun3pm },
            { R.id.mon4pm, R.id.tue4pm, R.id.wed4pm, R.id.thr4pm, R.id.fri4pm, R.id.sat4pm, R.id.sun4pm },
            { R.id.mon5pm, R.id.tue5pm, R.id.wed5pm, R.id.thr5pm, R.id.fri5pm, R.id.sat5pm, R.id.sun5pm },
            { R.id.mon6pm, R.id.tue6pm, R.id.wed6pm, R.id.thr6pm, R.id.fri6pm, R.id.sat6pm, R.id.sun6pm },
            { R.id.mon7pm, R.id.tue7pm, R.id.wed7pm, R.id.thr7pm, R.id.fri7pm, R.id.sat7pm, R.id.sun7pm },
            { R.id.mon8pm, R.id.tue8pm, R.id.wed8pm, R.id.thr8pm, R.id.fri8pm, R.id.sat8pm, R.id.sun8pm },
            { R.id.mon9pm, R.id.tue9pm, R.id.wed9pm, R.id.thr9pm, R.id.fri9pm, R.id.sat9pm, R.id.sun9pm },
            { R.id.mon10pm, R.id.tue10pm, R.id.wed10pm, R.id.thr10pm, R.id.fri10pm, R.id.sat10pm, R.id.sun10pm},
            { R.id.mon11pm, R.id.tue11pm, R.id.wed11pm, R.id.thr11pm, R.id.fri11pm, R.id.sat11pm, R.id.sun11pm},
            { R.id.mon12am, R.id.tue12am, R.id.wed12am, R.id.thr12am, R.id.fri12am, R.id.sat12am, R.id.sun12am},
    };
    // assuming each row is the same length you can do this
    private RadioButton[][] buttonArray = new RadioButton[buttonViewIds.length][buttonViewIds[0].length];
    private Button saveButton;

    //the button id in string format
    private String theIdString;

    //Hashmap to save and push schedule to db
    Map<String, Map<String,Boolean>> saveDay =  new HashMap<>();
    Map<String,Boolean> mon = new HashMap<>();
    Map<String,Boolean> tue = new HashMap<>();
    Map<String,Boolean> wed = new HashMap<>();
    Map<String,Boolean> thr = new HashMap<>();
    Map<String,Boolean> fri = new HashMap<>();
    Map<String,Boolean> sat = new HashMap<>();
    Map<String,Boolean> sun = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instance of Member class
        thisMember = new Member();


        //for radio button color
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        Color.BLACK, //disabled
                        Color.rgb(179,55,0)
                }
        );
        //for the million buttons
        for (int i=0; i<buttonViewIds.length; i++) {
            for (int j=0; j<buttonViewIds[0].length; j++) {
                buttonArray[i][j] = (RadioButton) findViewById(buttonViewIds[i][j]);
                buttonArray[i][j].setOnClickListener(this);
                buttonArray[i][j].setButtonTintList(colorStateList);

            }
        }

        //Retrieving ID's
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        //SearchBar
        FloatingActionButton fab = findViewById(R.id.fab);
        //Firebase Database instance
        db = FirebaseDatabase.getInstance().getReference().child("Users");


        //Connect nav view
        mName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_name);
        mEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_email);
        mPhoto = (CircleImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_profile_pic);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        //Sign out
        mNavigationView.getMenu().findItem(R.id.sign_out_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                signOut();
                return true;
            }
        });


        //Google Sign in and Display to NAV bar also saves new user into database
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ThirdActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName().toLowerCase();
            String personFirstName = acct.getGivenName().toLowerCase();
            String personLastName = acct.getFamilyName().toLowerCase();
            String personEmail = acct.getEmail().toLowerCase();
            Uri personPhoto = acct.getPhotoUrl();

            //for nav bar
            mName.setText(personName);
            mEmail.setText(personEmail);
            Glide.with(this).load(personPhoto).into(mPhoto);

            //Firebase auth should be used instead of google for userID, as people who register through normal email wont show up otherwise
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String userAuthId = currentFirebaseUser.getUid();

            //for member db object
            thisMember.setaName(personName);
            thisMember.setID(personEmail);
            thisMember.setFirstName(personFirstName);
            thisMember.setLastName(personLastName);

            //saves user under their id, no duplicates
            db.child(userAuthId).setValue(thisMember);
        }

        //Friends dialog box compare click listener, and logic to retrieve friends from db and put in to string array
        openFriendsDialog = findViewById(R.id.compareFriends);
        openFriendsDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                //Gets current firebase authID
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseAcctId =  currentFirebaseUser.getUid();
                //Gets the path to friend list
                mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseAcctId);
                mUserFriendDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friendCount = (int) dataSnapshot.getChildrenCount();
                        Map<String, Object> getFriendMaps = (Map<String, Object>) dataSnapshot.getValue();
                        //Iterates through the values of our hashmap
                        Iterator it = getFriendMaps.entrySet().iterator();
                        friendList = new String[friendCount];
                        for(int i=0 ; it.hasNext() ;i++){
                            Map.Entry pair = (Map.Entry)it.next();
                            String eachFriend = pair.getKey().toString();
                            friendList[i] = eachFriend;
                        }
                        DialogFragment newFragment = new FriendDialogBox();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("sendFriendList",friendList);
                        newFragment.setArguments(bundle);
                        newFragment.show(getSupportFragmentManager(), "friendDialogBox");
                        it.remove();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Failed To Read", databaseError.toException());
                    }
                });

            }
        });

        //the pop up at the right corner, FAB, Floating Action Bar
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, searchBar.class);
                startActivity(intent);
            }
        });

        //Drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.sign_out_button)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //save button to save schedule into db
        saveButton = findViewById(R.id.saveSchedule);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseAcctId =  currentFirebaseUser.getUid();
                thisMember.setUserSchedule(saveDay);
                db.child(firebaseAcctId).setValue(thisMember);


//
//                //the object pushed to the database
//                Map<String, Object> friendDbHashMap = new HashMap<>();
//
//
//                //local db in member class
//                Map<String, Boolean> memberMap =  new HashMap<>();
//                //Storing the username and boolean value, and setting it up
//                memberMap.put(username, true);
//                member.setMemberMap(memberMap);
//
//                //passing local db as the object value into this database
//                friendDbHashMap.put(username, memberMap);
//
//                mUserDatabase.updateChildren(friendDbHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ThirdActivity.this, "Friend Added", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ThirdActivity.this, "Adding Unsuccessful", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

    //Ends onCreate()
    }


    //Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Google Sign Out
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ThirdActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ThirdActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mon6am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon6am));
                break;
            case R.id.tue6am:
                deselection((RadioButton) findViewById(R.id.tue6am));
                break;
            case R.id.wed6am:
                deselection((RadioButton) findViewById(R.id.wed6am));
                break;
            case R.id.thr6am:
                deselection((RadioButton) findViewById(R.id.thr6am));
                break;
            case R.id.fri6am:
                deselection((RadioButton) findViewById(R.id.fri6am));
                break;
            case R.id.sat6am:
                deselection((RadioButton) findViewById(R.id.sat6am));
                break;
            case R.id.sun6am:
                deselection((RadioButton) findViewById(R.id.sun6am));
                break;
            case R.id.mon7am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon7am));
                break;
            case R.id.tue7am:
                deselection((RadioButton) findViewById(R.id.tue7am));
                break;
            case R.id.wed7am:
                deselection((RadioButton) findViewById(R.id.wed7am));
                break;
            case R.id.thr7am:
                deselection((RadioButton) findViewById(R.id.thr7am));
                break;
            case R.id.fri7am:
                deselection((RadioButton) findViewById(R.id.fri7am));
                break;
            case R.id.sat7am:
                deselection((RadioButton) findViewById(R.id.sat7am));
                break;
            case R.id.sun7am:
                deselection((RadioButton) findViewById(R.id.sun7am));
                break;
            case R.id.mon8am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon8am));
                break;
            case R.id.tue8am:
                deselection((RadioButton) findViewById(R.id.tue8am));
                break;
            case R.id.wed8am:
                deselection((RadioButton) findViewById(R.id.wed8am));
                break;
            case R.id.thr8am:
                deselection((RadioButton) findViewById(R.id.thr8am));
                break;
            case R.id.fri8am:
                deselection((RadioButton) findViewById(R.id.fri8am));
                break;
            case R.id.sat8am:
                deselection((RadioButton) findViewById(R.id.sat8am));
                break;
            case R.id.sun8am:
                deselection((RadioButton) findViewById(R.id.sun8am));
                break;
            case R.id.mon9am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon9am));
                break;
            case R.id.tue9am:
                deselection((RadioButton) findViewById(R.id.tue9am));
                break;
            case R.id.wed9am:
                deselection((RadioButton) findViewById(R.id.wed9am));
                break;
            case R.id.thr9am:
                deselection((RadioButton) findViewById(R.id.thr9am));
                break;
            case R.id.fri9am:
                deselection((RadioButton) findViewById(R.id.fri9am));
                break;
            case R.id.sat9am:
                deselection((RadioButton) findViewById(R.id.sat9am));
                break;
            case R.id.sun9am:
                deselection((RadioButton) findViewById(R.id.sun9am));
                break;
            case R.id.mon10am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon10am));
                break;
            case R.id.tue10am:
                deselection((RadioButton) findViewById(R.id.tue10am));
                break;
            case R.id.wed10am:
                deselection((RadioButton) findViewById(R.id.wed10am));
                break;
            case R.id.thr10am:
                deselection((RadioButton) findViewById(R.id.thr10am));
                break;
            case R.id.fri10am:
                deselection((RadioButton) findViewById(R.id.fri10am));
                break;
            case R.id.sat10am:
                deselection((RadioButton) findViewById(R.id.sat10am));
                break;
            case R.id.sun10am:
                deselection((RadioButton) findViewById(R.id.sun10am));
                break;
            case R.id.mon11am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon11am));
                break;
            case R.id.tue11am:
                deselection((RadioButton) findViewById(R.id.tue11am));
                break;
            case R.id.wed11am:
                deselection((RadioButton) findViewById(R.id.wed11am));
                break;
            case R.id.thr11am:
                deselection((RadioButton) findViewById(R.id.thr11am));
                break;
            case R.id.fri11am:
                deselection((RadioButton) findViewById(R.id.fri11am));
                break;
            case R.id.sat11am:
                deselection((RadioButton) findViewById(R.id.sat11am));
                break;
            case R.id.sun11am:
                deselection((RadioButton) findViewById(R.id.sun11am));
                break;
            case R.id.mon12pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon12pm));
                break;
            case R.id.tue12pm:
                deselection((RadioButton) findViewById(R.id.tue12pm));
                break;
            case R.id.wed12pm:
                deselection((RadioButton) findViewById(R.id.wed12pm));
                break;
            case R.id.thr12pm:
                deselection((RadioButton) findViewById(R.id.thr12pm));
                break;
            case R.id.fri12pm:
                deselection((RadioButton) findViewById(R.id.fri12pm));
                break;
            case R.id.sat12pm:
                deselection((RadioButton) findViewById(R.id.sat12pm));
                break;
            case R.id.sun12pm:
                deselection((RadioButton) findViewById(R.id.sun12pm));
                break;
            case R.id.mon1pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon1pm));
                break;
            case R.id.tue1pm:
                deselection((RadioButton) findViewById(R.id.tue1pm));
                break;
            case R.id.wed1pm:
                deselection((RadioButton) findViewById(R.id.wed1pm));
                break;
            case R.id.thr1pm:
                deselection((RadioButton) findViewById(R.id.thr1pm));
                break;
            case R.id.fri1pm:
                deselection((RadioButton) findViewById(R.id.fri1pm));
                break;
            case R.id.sat1pm:
                deselection((RadioButton) findViewById(R.id.sat1pm));
                break;
            case R.id.sun1pm:
                deselection((RadioButton) findViewById(R.id.sun1pm));
                break;
            case R.id.mon2pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon2pm));
                break;
            case R.id.tue2pm:
                deselection((RadioButton) findViewById(R.id.tue2pm));
                break;
            case R.id.wed2pm:
                deselection((RadioButton) findViewById(R.id.wed2pm));
                break;
            case R.id.thr2pm:
                deselection((RadioButton) findViewById(R.id.thr2pm));
                break;
            case R.id.fri2pm:
                deselection((RadioButton) findViewById(R.id.fri2pm));
                break;
            case R.id.sat2pm:
                deselection((RadioButton) findViewById(R.id.sat2pm));
                break;
            case R.id.sun2pm:
                deselection((RadioButton) findViewById(R.id.sun2pm));
                break;
            case R.id.mon3pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon3pm));
                break;
            case R.id.tue3pm:
                deselection((RadioButton) findViewById(R.id.tue3pm));
                break;
            case R.id.wed3pm:
                deselection((RadioButton) findViewById(R.id.wed3pm));
                break;
            case R.id.thr3pm:
                deselection((RadioButton) findViewById(R.id.thr3pm));
                break;
            case R.id.fri3pm:
                deselection((RadioButton) findViewById(R.id.fri3pm));
                break;
            case R.id.sat3pm:
                deselection((RadioButton) findViewById(R.id.sat3pm));
                break;
            case R.id.sun3pm:
                deselection((RadioButton) findViewById(R.id.sun3pm));
                break;
            case R.id.mon4pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon4pm));
                break;
            case R.id.tue4pm:
                deselection((RadioButton) findViewById(R.id.tue4pm));
                break;
            case R.id.wed4pm:
                deselection((RadioButton) findViewById(R.id.wed4pm));
                break;
            case R.id.thr4pm:
                deselection((RadioButton) findViewById(R.id.thr4pm));
                break;
            case R.id.fri4pm:
                deselection((RadioButton) findViewById(R.id.fri4pm));
                break;
            case R.id.sat4pm:
                deselection((RadioButton) findViewById(R.id.sat4pm));
                break;
            case R.id.sun4pm:
                deselection((RadioButton) findViewById(R.id.sun4pm));
                break;
            case R.id.mon5pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon5pm));
                break;
            case R.id.tue5pm:
                deselection((RadioButton) findViewById(R.id.tue5pm));
                break;
            case R.id.wed5pm:
                deselection((RadioButton) findViewById(R.id.wed5pm));
                break;
            case R.id.thr5pm:
                deselection((RadioButton) findViewById(R.id.thr5pm));
                break;
            case R.id.fri5pm:
                deselection((RadioButton) findViewById(R.id.fri5pm));
                break;
            case R.id.sat5pm:
                deselection((RadioButton) findViewById(R.id.sat5pm));
                break;
            case R.id.sun5pm:
                deselection((RadioButton) findViewById(R.id.sun5pm));
                break;
            case R.id.mon6pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon6pm));
                break;
            case R.id.tue6pm:
                deselection((RadioButton) findViewById(R.id.tue6pm));
                break;
            case R.id.wed6pm:
                deselection((RadioButton) findViewById(R.id.wed6pm));
                break;
            case R.id.thr6pm:
                deselection((RadioButton) findViewById(R.id.thr6pm));
                break;
            case R.id.fri6pm:
                deselection((RadioButton) findViewById(R.id.fri6pm));
                break;
            case R.id.sat6pm:
                deselection((RadioButton) findViewById(R.id.sat6pm));
                break;
            case R.id.sun6pm:
                deselection((RadioButton) findViewById(R.id.sun6pm));
                break;
            case R.id.mon7pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon7pm));
                break;
            case R.id.tue7pm:
                deselection((RadioButton) findViewById(R.id.tue7pm));
                break;
            case R.id.wed7pm:
                deselection((RadioButton) findViewById(R.id.wed7pm));
                break;
            case R.id.thr7pm:
                deselection((RadioButton) findViewById(R.id.thr7pm));
                break;
            case R.id.fri7pm:
                deselection((RadioButton) findViewById(R.id.fri7pm));
                break;
            case R.id.sat7pm:
                deselection((RadioButton) findViewById(R.id.sat7pm));
                break;
            case R.id.sun7pm:
                deselection((RadioButton) findViewById(R.id.sun7pm));
                break;
            case R.id.mon8pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon8pm));
                break;
            case R.id.tue8pm:
                deselection((RadioButton) findViewById(R.id.tue8pm));
                break;
            case R.id.wed8pm:
                deselection((RadioButton) findViewById(R.id.wed8pm));
                break;
            case R.id.thr8pm:
                deselection((RadioButton) findViewById(R.id.thr8pm));
                break;
            case R.id.fri8pm:
                deselection((RadioButton) findViewById(R.id.fri8pm));
                break;
            case R.id.sat8pm:
                deselection((RadioButton) findViewById(R.id.sat8pm));
                break;
            case R.id.sun8pm:
                deselection((RadioButton) findViewById(R.id.sun8pm));
                break;
            case R.id.mon9pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon9pm));
                break;
            case R.id.tue9pm:
                deselection((RadioButton) findViewById(R.id.tue9pm));
                break;
            case R.id.wed9pm:
                deselection((RadioButton) findViewById(R.id.wed9pm));
                break;
            case R.id.thr9pm:
                deselection((RadioButton) findViewById(R.id.thr9pm));
                break;
            case R.id.fri9pm:
                deselection((RadioButton) findViewById(R.id.fri9pm));
                break;
            case R.id.sat9pm:
                deselection((RadioButton) findViewById(R.id.sat9pm));
                break;
            case R.id.sun9pm:
                deselection((RadioButton) findViewById(R.id.sun9pm));
                break;
            case R.id.mon10pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon10pm));
                break;
            case R.id.tue10pm:
                deselection((RadioButton) findViewById(R.id.tue10pm));
                break;
            case R.id.wed10pm:
                deselection((RadioButton) findViewById(R.id.wed10pm));
                break;
            case R.id.thr10pm:
                deselection((RadioButton) findViewById(R.id.thr10pm));
                break;
            case R.id.fri10pm:
                deselection((RadioButton) findViewById(R.id.fri10pm));
                break;
            case R.id.sat10pm:
                deselection((RadioButton) findViewById(R.id.sat10pm));
                break;
            case R.id.sun10pm:
                deselection((RadioButton) findViewById(R.id.sun10pm));
                break;
            case R.id.mon11pm:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon11pm));
                break;
            case R.id.tue11pm:
                deselection((RadioButton) findViewById(R.id.tue11pm));
                break;
            case R.id.wed11pm:
                deselection((RadioButton) findViewById(R.id.wed11pm));
                break;
            case R.id.thr11pm:
                deselection((RadioButton) findViewById(R.id.thr11pm));
                break;
            case R.id.fri11pm:
                deselection((RadioButton) findViewById(R.id.fri11pm));
                break;
            case R.id.sat11pm:
                deselection((RadioButton) findViewById(R.id.sat11pm));
                break;
            case R.id.sun11pm:
                deselection((RadioButton) findViewById(R.id.sun11pm));
                break;
            case R.id.mon12am:
                System.out.println("vID: " +v.getId());
                deselection((RadioButton) findViewById(R.id.mon12am));
                break;
            case R.id.tue12am:
                deselection((RadioButton) findViewById(R.id.tue12am));
                break;
            case R.id.wed12am:
                deselection((RadioButton) findViewById(R.id.wed12am));
                break;
            case R.id.thr12am:
                deselection((RadioButton) findViewById(R.id.thr12am));
                break;
            case R.id.fri12am:
                deselection((RadioButton) findViewById(R.id.fri12am));
                break;
            case R.id.sat12am:
                deselection((RadioButton) findViewById(R.id.sat12am));
                break;
            case R.id.sun12am:
                deselection((RadioButton) findViewById(R.id.sun12am));
                break;
        }
    }

    //Deselection and saving data into temp array before pushing it to db on "save"
    public void deselection(RadioButton theButton) {
        int redis = theButton.getId();
        String temp;
        String day, time;
        if (!theButton.isSelected()) {
            theButton.setChecked(true);
            theButton.setSelected(true);

            //retrieve substring from time object and split it, and push it into hashmap
            theIdString = theButton.getResources().getResourceEntryName(redis);
            day = theIdString.substring(0,3);
            time = theIdString.substring(3, theIdString.length());

            boolean noDelete = false;
            handleIfForHashmaps(saveDay, day, time, noDelete);


            Toast.makeText(ThirdActivity.this, day + time + " Added! ", Toast.LENGTH_SHORT).show();

        } else {
            theButton.setChecked(false);
            theButton.setSelected(false);

            theIdString = theButton.getResources().getResourceEntryName(redis);
            day = theIdString.substring(0,3);
            time = theIdString.substring(3, theIdString.length());

            boolean delete = true;
            handleIfForHashmaps(saveDay, day, time, delete);

            Toast.makeText(ThirdActivity.this, day + time + " Deleted! ", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleIfForHashmaps(Map<String, Map<String, Boolean>> main, String theDay, String theTime, boolean delete) {
        if (theDay.equals("mon")){
            if(delete == true)
                mon.remove(theTime, true);
            else {
                mon.put(theTime, true);
                main.put(theDay, mon);
            }
        }else if(theDay.equals("tue")) {
            if(delete == true)
                tue.remove(theTime, true);
            else {
                tue.put(theTime, true);
                main.put(theDay, tue);
            }
        }else if(theDay.equals("wed")){
            if(delete == true)
                wed.remove(theTime, true);
            else {
                wed.put(theTime, true);
                main.put(theDay, wed);
            }
        }else if(theDay.equals("thr")){
            if(delete == true)
                thr.remove(theTime, true);
            else {
                thr.put(theTime, true);
                main.put(theDay, thr);
            }
        }
        else if(theDay.equals("fri")){
            if(delete == true)
                fri.remove(theTime, true);
            else {
                fri.put(theTime, true);
                main.put(theDay, fri);
            }
        }
        else if(theDay.equals("sat")){
            if(delete == true)
                sat.remove(theTime, true);
            else {
                sat.put(theTime, true);
                main.put(theDay, sat);
            }
        }
        else if(theDay.equals("sun")){
            if(delete == true)
                sun.remove(theTime, true);
            else {
                sun.put(theTime, true);
                main.put(theDay, sun);
            }
        }
        System.out.println("my hashmap "+ Arrays.asList(main));

    }

}
