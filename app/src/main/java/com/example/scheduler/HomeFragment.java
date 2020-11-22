package com.example.scheduler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements View.OnClickListener {
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

    //Int all the radio buttons
    private int[][] buttonViewIds = new int[][]{
            {R.id.mon6am, R.id.tue6am, R.id.wed6am, R.id.thr6am, R.id.fri6am, R.id.sat6am, R.id.sun6am},
            {R.id.mon7am, R.id.tue7am, R.id.wed7am, R.id.thr7am, R.id.fri7am, R.id.sat7am, R.id.sun7am},
            {R.id.mon8am, R.id.tue8am, R.id.wed8am, R.id.thr8am, R.id.fri8am, R.id.sat8am, R.id.sun8am},
            {R.id.mon9am, R.id.tue9am, R.id.wed9am, R.id.thr9am, R.id.fri9am, R.id.sat9am, R.id.sun9am},
            {R.id.mon10am, R.id.tue10am, R.id.wed10am, R.id.thr10am, R.id.fri10am, R.id.sat10am, R.id.sun10am},
            {R.id.mon11am, R.id.tue11am, R.id.wed11am, R.id.thr11am, R.id.fri11am, R.id.sat11am, R.id.sun11am},
            {R.id.mon12pm, R.id.tue12pm, R.id.wed12pm, R.id.thr12pm, R.id.fri12pm, R.id.sat12pm, R.id.sun12pm},
            {R.id.mon1pm, R.id.tue1pm, R.id.wed1pm, R.id.thr1pm, R.id.fri1pm, R.id.sat1pm, R.id.sun1pm},
            {R.id.mon2pm, R.id.tue2pm, R.id.wed2pm, R.id.thr2pm, R.id.fri2pm, R.id.sat2pm, R.id.sun2pm},
            {R.id.mon3pm, R.id.tue3pm, R.id.wed3pm, R.id.thr3pm, R.id.fri3pm, R.id.sat3pm, R.id.sun3pm},
            {R.id.mon4pm, R.id.tue4pm, R.id.wed4pm, R.id.thr4pm, R.id.fri4pm, R.id.sat4pm, R.id.sun4pm},
            {R.id.mon5pm, R.id.tue5pm, R.id.wed5pm, R.id.thr5pm, R.id.fri5pm, R.id.sat5pm, R.id.sun5pm},
            {R.id.mon6pm, R.id.tue6pm, R.id.wed6pm, R.id.thr6pm, R.id.fri6pm, R.id.sat6pm, R.id.sun6pm},
            {R.id.mon7pm, R.id.tue7pm, R.id.wed7pm, R.id.thr7pm, R.id.fri7pm, R.id.sat7pm, R.id.sun7pm},
            {R.id.mon8pm, R.id.tue8pm, R.id.wed8pm, R.id.thr8pm, R.id.fri8pm, R.id.sat8pm, R.id.sun8pm},
            {R.id.mon9pm, R.id.tue9pm, R.id.wed9pm, R.id.thr9pm, R.id.fri9pm, R.id.sat9pm, R.id.sun9pm},
            {R.id.mon10pm, R.id.tue10pm, R.id.wed10pm, R.id.thr10pm, R.id.fri10pm, R.id.sat10pm, R.id.sun10pm},
            {R.id.mon11pm, R.id.tue11pm, R.id.wed11pm, R.id.thr11pm, R.id.fri11pm, R.id.sat11pm, R.id.sun11pm},
            {R.id.mon12am, R.id.tue12am, R.id.wed12am, R.id.thr12am, R.id.fri12am, R.id.sat12am, R.id.sun12am},
    };

    //String days and times
    private String[][] stringDaysAndTime = new String[][]{
            {"mon6am", "tue6am", "wed6am", "thr6am", "fri6am", "sat6am", "sun6am"},
            {"mon7am", "tue7am", "wed7am", "thr7am", "fri7am", "sat7am", "sun7am"},
            {"mon8am", "tue8am", "wed8am", "thr8am", "fri8am", "sat8am", "sun8am"},
            {"mon9am", "tue9am", "wed9am", "thr9am", "fri9am", "sat9am", "sun9am"},
            {"mon10am", "tue10am", "wed10am", "thr10am", "fri10am", "sat10am", "sun10am"},
            {"mon11am", "tue11am", "wed11am", "thr11am", "fri11am", "sat11am", "sun11am"},
            {"mon12pm", "tue12pm", "wed12pm", "thr12pm", "fri12pm", "sat12pm", "sun12pm"},
            {"mon1pm", "tue1pm", "wed1pm", "thr1pm", "fri1pm", "sat1pm", "sun1pm"},
            {"mon2pm", "tue2pm", "wed2pm", "thr2pm", "fri2pm", "sat2pm", "sun2pm"},
            {"mon3pm", "tue3pm", "wed3pm", "thr3pm", "fri3pm", "sat3pm", "sun3pm"},
            {"mon4pm", "tue4pm", "wed4pm", "thr4pm", "fri4pm", "sat4pm", "sun4pm"},
            {"mon5pm", "tue5pm", "wed5pm", "thr5pm", "fri5pm", "sat5pm", "sun5pm"},
            {"mon6pm", "tue6pm", "wed6pm", "thr6pm", "fri6pm", "sat6pm", "sun6pm"},
            {"mon7pm", "tue7pm", "wed7pm", "thr7pm", "fri7pm", "sat7pm", "sun7pm"},
            {"mon8am", "tue8am", "wed8am", "thr8am", "fri8am", "sat8am", "sun8am"},
            {"mon9pm", "tue9pm", "wed9pm", "thr9pm", "fri9pm", "sat9pm", "sun9pm"},
            {"mon10pm", "tue10pm", "wed10pm", "thr10pm", "fri10pm", "sat10pm", "sun10pm"},
            {"mon11pm", "tue11pm", "wed11pm", "thr11pm", "fri11pm", "sat11pm", "sun11pm"},
            {"mon12am", "tue12am", "wed12am", "thr12am", "fri12am", "sat12am", "sun12am"},

    };

    // assuming each row is the same length you can do this
    private RadioButton[][] buttonArray = new RadioButton[buttonViewIds.length][buttonViewIds[0].length];
    private Button saveButton;

    //the button id in string format
    private String theIdString;

    //Main hashmap to save and push schedule to db
    Map<String, Object> saveDay = new HashMap<>();

    //Secondary hasmap placed into saveDay appropriately
    Map<String, Boolean> mon = new HashMap<>();
    Map<String, Boolean> tue = new HashMap<>();
    Map<String, Boolean> wed = new HashMap<>();
    Map<String, Boolean> thr = new HashMap<>();
    Map<String, Boolean> fri = new HashMap<>();
    Map<String, Boolean> sat = new HashMap<>();
    Map<String, Boolean> sun = new HashMap<>();


    //for showing image
    TableLayout tableLayout;
    ImageView imageView;

    //to save state of buttons
    private SharedPreferences sharedPreferences;


    @Nullable
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View finalView;
        finalView = loadRadioButtons(inflater, container);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        onCreate(savedInstanceState, finalView);


        //return v;
        return finalView;
    }

    public void onCreate(final Bundle savedInstanceState, final View view){
        super.onCreate(savedInstanceState);
        thisMember = new Member();

        tableLayout = view.findViewById(R.id.mainTable);
        imageView = view.findViewById(R.id.thepic);

        //for radio button color
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{
                        Color.BLACK, //disabled
                        Color.rgb(179, 55, 0)
                }
        );
        //for the million buttons
        for (int i = 0; i < buttonViewIds.length; i++) {
            for (int j = 0; j < buttonViewIds[0].length; j++) {
                buttonArray[i][j] = (RadioButton) view.findViewById(buttonViewIds[i][j]);
                buttonArray[i][j].setOnClickListener(this);
                buttonArray[i][j].setButtonTintList(colorStateList);

            }
        }


        openFriendsDialog = view.findViewById(R.id.compareFriends);
        openFriendsDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets current firebase authID
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseAcctId = currentFirebaseUser.getUid();
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
                        for (int i = 0; it.hasNext(); i++) {
                            Map.Entry pair = (Map.Entry) it.next();
                            String eachFriend = pair.getKey().toString();
                            friendList[i] = eachFriend;
                        }
                        DialogFragment newFragment = new FriendDialogBox();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("sendFriendList", friendList);
                        newFragment.setArguments(bundle);
                        newFragment.show(getActivity().getSupportFragmentManager(), "friendDialogBox");
                        it.remove();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Failed To Read", databaseError.toException());
                    }
                });
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchBar.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.top_to_visible, R.anim.visible_to_bottom);
            }
        });



        //Save Button
        saveButton = view.findViewById(R.id.saveSchedule);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseAcctId = currentFirebaseUser.getUid();
                db = FirebaseDatabase.getInstance().getReference("Schedules");

                //saves user info as well
                thisMember.setUserSchedule(saveDay);
                db.child(firebaseAcctId).setValue(thisMember);
                View smth = view;
                saveRadioButtons(smth);

            }
        });
    }


        @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mon6am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon6am), v);
                break;
            case R.id.tue6am:
                deselection((RadioButton) v.findViewById(R.id.tue6am), v);
                break;
            case R.id.wed6am:
                deselection((RadioButton) v.findViewById(R.id.wed6am), v);
                break;
            case R.id.thr6am:
                deselection((RadioButton) v.findViewById(R.id.thr6am), v);
                break;
            case R.id.fri6am:
                deselection((RadioButton) v.findViewById(R.id.fri6am), v);
                break;
            case R.id.sat6am:
                deselection((RadioButton) v.findViewById(R.id.sat6am), v);
                break;
            case R.id.sun6am:
                deselection((RadioButton) v.findViewById(R.id.sun6am), v);
                break;
            case R.id.mon7am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon7am), v);
                break;
            case R.id.tue7am:
                deselection((RadioButton) v.findViewById(R.id.tue7am), v);
                break;
            case R.id.wed7am:
                deselection((RadioButton) v.findViewById(R.id.wed7am), v);
                break;
            case R.id.thr7am:
                deselection((RadioButton) v.findViewById(R.id.thr7am), v);
                break;
            case R.id.fri7am:
                deselection((RadioButton) v.findViewById(R.id.fri7am), v);
                break;
            case R.id.sat7am:
                deselection((RadioButton) v.findViewById(R.id.sat7am), v);
                break;
            case R.id.sun7am:
                deselection((RadioButton) v.findViewById(R.id.sun7am), v);
                break;
            case R.id.mon8am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon8am), v);
                break;
            case R.id.tue8am:
                deselection((RadioButton) v.findViewById(R.id.tue8am), v);
                break;
            case R.id.wed8am:
                deselection((RadioButton) v.findViewById(R.id.wed8am), v);
                break;
            case R.id.thr8am:
                deselection((RadioButton) v.findViewById(R.id.thr8am), v);
                break;
            case R.id.fri8am:
                deselection((RadioButton) v.findViewById(R.id.fri8am), v);
                break;
            case R.id.sat8am:
                deselection((RadioButton) v.findViewById(R.id.sat8am), v);
                break;
            case R.id.sun8am:
                deselection((RadioButton) v.findViewById(R.id.sun8am), v);
                break;
            case R.id.mon9am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon9am), v);
                break;
            case R.id.tue9am:
                deselection((RadioButton) v.findViewById(R.id.tue9am), v);
                break;
            case R.id.wed9am:
                deselection((RadioButton) v.findViewById(R.id.wed9am), v);
                break;
            case R.id.thr9am:
                deselection((RadioButton) v.findViewById(R.id.thr9am), v);
                break;
            case R.id.fri9am:
                deselection((RadioButton) v.findViewById(R.id.fri9am), v);
                break;
            case R.id.sat9am:
                deselection((RadioButton) v.findViewById(R.id.sat9am), v);
                break;
            case R.id.sun9am:
                deselection((RadioButton) v.findViewById(R.id.sun9am), v);
                break;
            case R.id.mon10am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon10am), v);
                break;
            case R.id.tue10am:
                deselection((RadioButton) v.findViewById(R.id.tue10am), v);
                break;
            case R.id.wed10am:
                deselection((RadioButton) v.findViewById(R.id.wed10am), v);
                break;
            case R.id.thr10am:
                deselection((RadioButton) v.findViewById(R.id.thr10am), v);
                break;
            case R.id.fri10am:
                deselection((RadioButton) v.findViewById(R.id.fri10am), v);
                break;
            case R.id.sat10am:
                deselection((RadioButton) v.findViewById(R.id.sat10am), v);
                break;
            case R.id.sun10am:
                deselection((RadioButton) v.findViewById(R.id.sun10am), v);
                break;
            case R.id.mon11am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon11am), v);
                break;
            case R.id.tue11am:
                deselection((RadioButton) v.findViewById(R.id.tue11am), v);
                break;
            case R.id.wed11am:
                deselection((RadioButton) v.findViewById(R.id.wed11am), v);
                break;
            case R.id.thr11am:
                deselection((RadioButton) v.findViewById(R.id.thr11am), v);
                break;
            case R.id.fri11am:
                deselection((RadioButton) v.findViewById(R.id.fri11am), v);
                break;
            case R.id.sat11am:
                deselection((RadioButton) v.findViewById(R.id.sat11am), v);
                break;
            case R.id.sun11am:
                deselection((RadioButton) v.findViewById(R.id.sun11am), v);
                break;
            case R.id.mon12pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon12pm), v);
                break;
            case R.id.tue12pm:
                deselection((RadioButton) v.findViewById(R.id.tue12pm), v);
                break;
            case R.id.wed12pm:
                deselection((RadioButton) v.findViewById(R.id.wed12pm), v);
                break;
            case R.id.thr12pm:
                deselection((RadioButton) v.findViewById(R.id.thr12pm), v);
                break;
            case R.id.fri12pm:
                deselection((RadioButton) v.findViewById(R.id.fri12pm), v);
                break;
            case R.id.sat12pm:
                deselection((RadioButton) v.findViewById(R.id.sat12pm), v);
                break;
            case R.id.sun12pm:
                deselection((RadioButton) v.findViewById(R.id.sun12pm), v);
                break;
            case R.id.mon1pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon1pm), v);
                break;
            case R.id.tue1pm:
                deselection((RadioButton) v.findViewById(R.id.tue1pm), v);
                break;
            case R.id.wed1pm:
                deselection((RadioButton) v.findViewById(R.id.wed1pm), v);
                break;
            case R.id.thr1pm:
                deselection((RadioButton) v.findViewById(R.id.thr1pm), v);
                break;
            case R.id.fri1pm:
                deselection((RadioButton) v.findViewById(R.id.fri1pm), v);
                break;
            case R.id.sat1pm:
                deselection((RadioButton) v.findViewById(R.id.sat1pm), v);
                break;
            case R.id.sun1pm:
                deselection((RadioButton) v.findViewById(R.id.sun1pm), v);
                break;
            case R.id.mon2pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon2pm), v);
                break;
            case R.id.tue2pm:
                deselection((RadioButton) v.findViewById(R.id.tue2pm), v);
                break;
            case R.id.wed2pm:
                deselection((RadioButton) v.findViewById(R.id.wed2pm), v);
                break;
            case R.id.thr2pm:
                deselection((RadioButton) v.findViewById(R.id.thr2pm), v);
                break;
            case R.id.fri2pm:
                deselection((RadioButton) v.findViewById(R.id.fri2pm), v);
                break;
            case R.id.sat2pm:
                deselection((RadioButton) v.findViewById(R.id.sat2pm), v);
                break;
            case R.id.sun2pm:
                deselection((RadioButton) v.findViewById(R.id.sun2pm), v);
                break;
            case R.id.mon3pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon3pm), v);
                break;
            case R.id.tue3pm:
                deselection((RadioButton) v.findViewById(R.id.tue3pm), v);
                break;
            case R.id.wed3pm:
                deselection((RadioButton) v.findViewById(R.id.wed3pm), v);
                break;
            case R.id.thr3pm:
                deselection((RadioButton) v.findViewById(R.id.thr3pm), v);
                break;
            case R.id.fri3pm:
                deselection((RadioButton) v.findViewById(R.id.fri3pm), v);
                break;
            case R.id.sat3pm:
                deselection((RadioButton) v.findViewById(R.id.sat3pm), v);
                break;
            case R.id.sun3pm:
                deselection((RadioButton) v.findViewById(R.id.sun3pm), v);
                break;
            case R.id.mon4pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon4pm), v);
                break;
            case R.id.tue4pm:
                deselection((RadioButton) v.findViewById(R.id.tue4pm), v);
                break;
            case R.id.wed4pm:
                deselection((RadioButton) v.findViewById(R.id.wed4pm), v);
                break;
            case R.id.thr4pm:
                deselection((RadioButton) v.findViewById(R.id.thr4pm), v);
                break;
            case R.id.fri4pm:
                deselection((RadioButton) v.findViewById(R.id.fri4pm), v);
                break;
            case R.id.sat4pm:
                deselection((RadioButton) v.findViewById(R.id.sat4pm), v);
                break;
            case R.id.sun4pm:
                deselection((RadioButton) v.findViewById(R.id.sun4pm), v);
                break;
            case R.id.mon5pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon5pm), v);
                break;
            case R.id.tue5pm:
                deselection((RadioButton) v.findViewById(R.id.tue5pm), v);
                break;
            case R.id.wed5pm:
                deselection((RadioButton) v.findViewById(R.id.wed5pm), v);
                break;
            case R.id.thr5pm:
                deselection((RadioButton) v.findViewById(R.id.thr5pm), v);
                break;
            case R.id.fri5pm:
                deselection((RadioButton) v.findViewById(R.id.fri5pm), v);
                break;
            case R.id.sat5pm:
                deselection((RadioButton) v.findViewById(R.id.sat5pm), v);
                break;
            case R.id.sun5pm:
                deselection((RadioButton) v.findViewById(R.id.sun5pm), v);
                break;
            case R.id.mon6pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon6pm), v);
                break;
            case R.id.tue6pm:
                deselection((RadioButton) v.findViewById(R.id.tue6pm), v);
                break;
            case R.id.wed6pm:
                deselection((RadioButton) v.findViewById(R.id.wed6pm), v);
                break;
            case R.id.thr6pm:
                deselection((RadioButton) v.findViewById(R.id.thr6pm), v);
                break;
            case R.id.fri6pm:
                deselection((RadioButton) v.findViewById(R.id.fri6pm), v);
                break;
            case R.id.sat6pm:
                deselection((RadioButton) v.findViewById(R.id.sat6pm), v);
                break;
            case R.id.sun6pm:
                deselection((RadioButton) v.findViewById(R.id.sun6pm), v);
                break;
            case R.id.mon7pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon7pm), v);
                break;
            case R.id.tue7pm:
                deselection((RadioButton) v.findViewById(R.id.tue7pm), v);
                break;
            case R.id.wed7pm:
                deselection((RadioButton) v.findViewById(R.id.wed7pm), v);
                break;
            case R.id.thr7pm:
                deselection((RadioButton) v.findViewById(R.id.thr7pm), v);
                break;
            case R.id.fri7pm:
                deselection((RadioButton) v.findViewById(R.id.fri7pm), v);
                break;
            case R.id.sat7pm:
                deselection((RadioButton) v.findViewById(R.id.sat7pm), v);
                break;
            case R.id.sun7pm:
                deselection((RadioButton) v.findViewById(R.id.sun7pm), v);
                break;
            case R.id.mon8pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon8pm), v);
                break;
            case R.id.tue8pm:
                deselection((RadioButton) v.findViewById(R.id.tue8pm), v);
                break;
            case R.id.wed8pm:
                deselection((RadioButton) v.findViewById(R.id.wed8pm), v);
                break;
            case R.id.thr8pm:
                deselection((RadioButton) v.findViewById(R.id.thr8pm), v);
                break;
            case R.id.fri8pm:
                deselection((RadioButton) v.findViewById(R.id.fri8pm), v);
                break;
            case R.id.sat8pm:
                deselection((RadioButton) v.findViewById(R.id.sat8pm), v);
                break;
            case R.id.sun8pm:
                deselection((RadioButton) v.findViewById(R.id.sun8pm), v);
                break;
            case R.id.mon9pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon9pm), v);
                break;
            case R.id.tue9pm:
                deselection((RadioButton) v.findViewById(R.id.tue9pm), v);
                break;
            case R.id.wed9pm:
                deselection((RadioButton) v.findViewById(R.id.wed9pm), v);
                break;
            case R.id.thr9pm:
                deselection((RadioButton) v.findViewById(R.id.thr9pm), v);
                break;
            case R.id.fri9pm:
                deselection((RadioButton) v.findViewById(R.id.fri9pm), v);
                break;
            case R.id.sat9pm:
                deselection((RadioButton) v.findViewById(R.id.sat9pm), v);
                break;
            case R.id.sun9pm:
                deselection((RadioButton) v.findViewById(R.id.sun9pm), v);
                break;
            case R.id.mon10pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon10pm), v);
                break;
            case R.id.tue10pm:
                deselection((RadioButton) v.findViewById(R.id.tue10pm), v);
                break;
            case R.id.wed10pm:
                deselection((RadioButton) v.findViewById(R.id.wed10pm), v);
                break;
            case R.id.thr10pm:
                deselection((RadioButton) v.findViewById(R.id.thr10pm), v);
                break;
            case R.id.fri10pm:
                deselection((RadioButton) v.findViewById(R.id.fri10pm), v);
                break;
            case R.id.sat10pm:
                deselection((RadioButton) v.findViewById(R.id.sat10pm), v);
                break;
            case R.id.sun10pm:
                deselection((RadioButton) v.findViewById(R.id.sun10pm), v);
                break;
            case R.id.mon11pm:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon11pm), v);
                break;
            case R.id.tue11pm:
                deselection((RadioButton) v.findViewById(R.id.tue11pm), v);
                break;
            case R.id.wed11pm:
                deselection((RadioButton) v.findViewById(R.id.wed11pm), v);
                break;
            case R.id.thr11pm:
                deselection((RadioButton) v.findViewById(R.id.thr11pm), v);
                break;
            case R.id.fri11pm:
                deselection((RadioButton) v.findViewById(R.id.fri11pm), v);
                break;
            case R.id.sat11pm:
                deselection((RadioButton) v.findViewById(R.id.sat11pm), v);
                break;
            case R.id.sun11pm:
                deselection((RadioButton) v.findViewById(R.id.sun11pm), v);
                break;
            case R.id.mon12am:
                System.out.println("vID: " + v.getId());
                deselection((RadioButton) v.findViewById(R.id.mon12am), v);
                break;
            case R.id.tue12am:
                deselection((RadioButton) v.findViewById(R.id.tue12am), v);
                break;
            case R.id.wed12am:
                deselection((RadioButton) v.findViewById(R.id.wed12am), v);
                break;
            case R.id.thr12am:
                deselection((RadioButton) v.findViewById(R.id.thr12am), v);
                break;
            case R.id.fri12am:
                deselection((RadioButton) v.findViewById(R.id.fri12am), v);
                break;
            case R.id.sat12am:
                deselection((RadioButton) v.findViewById(R.id.sat12am), v);
                break;
            case R.id.sun12am:
                deselection((RadioButton) v.findViewById(R.id.sun12am), v);
                break;
        }
    }

    public void deselection(RadioButton theButton, View v) {
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

            //Toast.makeText(test_activity.this, day + time + " Added! ", Toast.LENGTH_SHORT).show();

        } else {
            theButton.setChecked(false);
            theButton.setSelected(false);

            theIdString = theButton.getResources().getResourceEntryName(redis);
            day = theIdString.substring(0,3);
            time = theIdString.substring(3, theIdString.length());

            boolean delete = true;
            handleIfForHashmaps(saveDay, day, time, delete);
        }
    }

    public void handleIfForHashmaps(Map<String, Object> main, String theDay, String theTime, boolean delete) {
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
        System.out.println("Final Saveday before Saving "+ Arrays.asList(main));

    }

    public void saveRadioButtons(View smth){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i=0; i<stringDaysAndTime.length; i++) {
            for (int j=0; j<stringDaysAndTime[0].length; j++) {
                buttonArray[i][j] = (RadioButton) smth.findViewById(buttonViewIds[i][j]);
                editor.putBoolean(stringDaysAndTime[i][j], (buttonArray[i][j].isChecked()));
            }
        }
        editor.apply();
    }

    public View loadRadioButtons(LayoutInflater inflater, ViewGroup container){
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //loads it from the sharedpreference xml file which is saved locally.
        //If you'd like to see where its under device file explorer/data/data/com.example.scheduler/sharedpreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String time;

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String firebaseAcctId =  currentFirebaseUser.getUid();

        //db ref to get to the userSchedule child
        db = FirebaseDatabase.getInstance().getReference("Schedules").child(firebaseAcctId);
        final DatabaseReference schedDB = db.child("userSchedule");

        //At load time, select the checked buttons that were checked the session before
        for (int i=0; i<stringDaysAndTime.length; i++) {
            for (int j=0; j<stringDaysAndTime[0].length; j++) {
                buttonArray[i][j] = (RadioButton) v.findViewById(buttonViewIds[i][j]);
                buttonArray[i][j].setChecked(sharedPreferences.getBoolean(stringDaysAndTime[i][j], true));
                buttonArray[i][j].setSelected(sharedPreferences.getBoolean(stringDaysAndTime[i][j], true));
                if(buttonArray[i][j].isChecked()) {
                    time = stringDaysAndTime[i][j].substring(3);
                    System.out.println("time " + time);
                }
            }
        }
        //if the hashmap is empty at loadTime, then add the existing values to the hashmap, and if any new values are added, deleted it will be appended accordingly
        if(saveDay.size()==0) {
            schedDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        System.out.println("Key: " + dataSnapshot.getKey());
                        System.out.println("Value: " + dataSnapshot.getValue());

                        //hashmap to retrieve higher level of our structure
                        Map<String, Object> getScheduleMap = (Map<String, Object>) dataSnapshot.getValue();
                        Iterator it = getScheduleMap.entrySet().iterator();
                        for (int i = 0; it.hasNext(); i++) {
                            Map.Entry pair = (Map.Entry) it.next();
                            //retrieve the "day" key
                            String eachDay = pair.getKey().toString();
                            System.out.println("Testing " + getScheduleMap.get(pair.getKey()).toString());
                            //hashmap to iterate through the time:true values from the hasmap
                            Map<String, Boolean> getTimeMap = (Map<String, Boolean>) getScheduleMap.get(pair.getKey());
                            Iterator lit = getTimeMap.entrySet().iterator();
                            for (int k = 0; lit.hasNext(); k++) {
                                Map.Entry pair2 = (Map.Entry) lit.next();

                                //saves time, and bool into var
                                String eachTime = pair2.getKey().toString();
                                String eachBool = pair2.getValue().toString();

                                System.out.println("TIME: " + eachTime);
                                System.out.println("BOOL: " + eachBool);
                                //passes it to the handler function for proper integration
                                handleIfForHashmaps(saveDay, eachDay, eachTime, false);
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
        return v;
    }
}


