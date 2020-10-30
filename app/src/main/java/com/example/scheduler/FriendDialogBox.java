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

    //friend check marks, tracks em
    ArrayList<Integer> selectedFriends;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Tracks chosen friends
        selectedFriends = new ArrayList<Integer>();

        //Dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //retrieves the friend String Array list made in Third Activity
        Bundle bundle = getArguments();
        final String[] yourFriendFromDb = bundle.getStringArray("sendFriendList");


       builder.setTitle(R.string.dialog_choose_friends).setMultiChoiceItems(yourFriendFromDb, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    selectedFriends.add(which);
                } else if (selectedFriends.contains(which)){
                    //If friend is already in array, remove
                    System.out.println("elseif" + which);
                    selectedFriends.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //ig in another activity class which will show the final output, we can handle the logic there by using these
                //names and retrieving their schedules to compare and show
                for(int i=0; i <selectedFriends.size();i++){
                    System.out.println(yourFriendFromDb[selectedFriends.get(i)]);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

}
