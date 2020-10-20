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
import java.util.Iterator;
import java.util.Map;

public class FriendDialogBox extends DialogFragment {
    ArrayList<Integer> selectedFriends;
    private DatabaseReference mUserFriendDatabase;
    private static final String TAG = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //tracks chosen friends
        selectedFriends = new ArrayList<Integer>();
        //gets current firebase auth id
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebaseAcctId =  currentFirebaseUser.getUid();

        //access the current users friend
        mUserFriendDatabase = FirebaseDatabase.getInstance().getReference("Friends").child(firebaseAcctId);
        mUserFriendDatabase.addValueEventListener(new ValueEventListener() {
            //to see in the terminal
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //convert hashmap to string type beat
                Map<String, Object> getFriendMaps = (Map<String, Object>) dataSnapshot.getValue();
                //iterate through the values of our hashmap
                Iterator it = getFriendMaps.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    //Prints key value pairs, for now i only wanna save the key or display that in dialog box
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    System.out.println(pair.getKey());
                    it.remove();
                }
                //Log.i(TAG, "Value is: " + getFriendMaps);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed To Read", databaseError.toException());
            }
        });


        //Use the builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_choose_friends)
                //specify list array, items to be selected  by default (null for none)
                .setMultiChoiceItems(R.array.friends, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked){
                            //if user selects add to selected friends
                            selectedFriends.add(which);
                        } else if (selectedFriends.contains(which)){
                            //If friend is already in array, remove
                            selectedFriends.remove(Integer.valueOf(which));
                        }
                    }
                })

                //Set positive action
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //accept friends
                    }
                })
                //set negative action
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
