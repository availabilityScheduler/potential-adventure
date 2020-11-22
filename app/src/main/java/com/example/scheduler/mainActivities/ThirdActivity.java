package com.example.scheduler.mainActivities;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.scheduler.R;
import com.example.scheduler.finalUI.CompareSchedules;
import com.example.scheduler.fragments.AboutFragment;
import com.example.scheduler.fragments.HomeFragment;
import com.example.scheduler.social.searchBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.NavigationUI;

import de.hdodenhof.circleimageview.CircleImageView;

//expanding menu and stuff
public class ThirdActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //Navigation Stuff
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView mNavigationView;
    private DrawerLayout drawer;

    //Instance Member
    Member thisMember;

    //DB instance normal
    DatabaseReference db;

    //Google and nav display
    GoogleSignInClient mGoogleSignInClient;
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

    private Button someButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instance of Member class
        thisMember = new Member();



        //Retrieving ID's
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        //SearchBar
        FloatingActionButton fab = findViewById(R.id.fab);
        //Firebase Database instance
        db = FirebaseDatabase.getInstance().getReference().child("Users");
        try {
            db.keepSynced(true);
        } catch (DatabaseException e) {
            // Do anything
        }


        //Connect nav view
        mName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_name);
        mEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_email);
        mPhoto = (CircleImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_profile_pic);


        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
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
            //mSchedule
            Glide.with(this).load(personPhoto).into(mPhoto);

            //Firebase auth should be used instead of google for userID, as people who register through normal email wont show up otherwise
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String userAuthId = currentFirebaseUser.getUid();
            //save button to save schedule into db

            //for member db object
            thisMember.setaName(personName);
            thisMember.setID(personEmail);
            thisMember.setFirstName(personFirstName);
            thisMember.setLastName(personLastName);

            //saves user under their id, no duplicates
            db.child(userAuthId).setValue(thisMember);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
        }
        //Ends onCreate()
    }

    //Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_right_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_top_right_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                return true;
            case R.id.create_event:
                Toast.makeText(ThirdActivity.this, "Create your event!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.join_event:
                Toast.makeText(ThirdActivity.this, "Join an event!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
