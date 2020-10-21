package com.example.scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class FriendDialogBox extends DialogFragment {
    ArrayList<Integer> selectedFriends;

    private DatabaseReference mUserFriendDatabase;
    private static final String TAG = "";
    private int friendCount;

    //friend string aray
    String friendList[];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //tracks chosen friends
        selectedFriends = new ArrayList<Integer>();

        //firebase authid
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebaseAcctId =  currentFirebaseUser.getUid();

        //dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //access the current users friend's
        mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseAcctId);

        //gets the number of friends the user have
        mUserFriendDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendCount = (int) dataSnapshot.getChildrenCount();
                System.out.println("count " + friendCount);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //retrieves the friend and puts it into the friend array
        mUserFriendDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //convert hashmap to string type
                Map<String, Object> getFriendMaps = (Map<String, Object>) dataSnapshot.getValue();
                //iterate through the values of our hashmap
                Iterator it = getFriendMaps.entrySet().iterator();
                friendList = new String[friendCount];
                for(int i=0 ; it.hasNext() ;i++){
                    Map.Entry pair = (Map.Entry)it.next();
                    String friend1 = pair.getKey().toString();
                    friendList[i] = friend1;
                    System.out.println(Arrays.toString(friendList));
                }
                it.remove();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed To Read", databaseError.toException());
            }
        });


        //if u pass in cars in the parameters, it works just fine, just for testing purposes
        final String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};

        //the problem is retrieving the data from firebase through the above onDataChange calls is an async function,
        // so you're never really getting it back? or smth like that
        //doesnt work if u put friendlist as the parameter, tried using the debugger apparently value is null
       builder.setTitle(R.string.dialog_choose_friends).setMultiChoiceItems(friendList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    //testing pringint car string array
                    System.out.println("printCars" + Arrays.toString(cars));

                    //but for some reason it prints the friends just fine here?
                    System.out.println("printFriendList" + Arrays.toString(friendList));

                    //if user selects add to selected friends
                    selectedFriends.add(which);
                } else if (selectedFriends.contains(which)){
                    //If friend is already in array, remove
                    System.out.println("elseif" + which);

                    selectedFriends.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //accept friends
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

}
