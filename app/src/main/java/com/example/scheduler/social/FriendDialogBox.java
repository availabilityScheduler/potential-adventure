package com.example.scheduler.social;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.scheduler.R;
import com.example.scheduler.finalUI.CompareSchedules;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class FriendDialogBox extends DialogFragment{

    //friend check marks, tracks em
    private ArrayList<Integer> selectedFriends;
    private ArrayList<Integer> deleteFriends;
    private ArrayList<String> friendsToCompare;
    AlertDialog alertDialog;
    private String firebaseAcctId;
    private FirebaseUser currentFirebaseUser;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Tracks chosen friends
        selectedFriends = new ArrayList<Integer>();
        deleteFriends = new ArrayList<>();
        friendsToCompare = new ArrayList<String>();

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
                    deleteFriends.add(which);
                } else if (selectedFriends.contains(which)){
                    //If friend is already in array, remove
                    System.out.println("elseif" + which);
                    selectedFriends.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for(int i=0; i <selectedFriends.size();i++){
                    System.out.println("Compare this friend" + yourFriendFromDb[selectedFriends.get(i)]);
                    friendsToCompare.add(yourFriendFromDb[selectedFriends.get(i)]);
                }
                doTheComparison();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }

        }).setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
               removeAfriend(yourFriendFromDb, deleteFriends);
           }
       });
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return alertDialog;
    }
    private void doTheComparison() {
        Intent accept = new Intent(FriendDialogBox.this.getActivity(), CompareSchedules.class);
        accept.putExtra("friendsPassedToCompareSchedules", friendsToCompare);
        accept.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(accept);
    }

    private void removeAfriend(String[] yourFriendFromDb, ArrayList<Integer> deleteFriends){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAcctId = currentFirebaseUser.getUid();

        //removes from friend db
        for(int i=0; i <selectedFriends.size();i++){
            database.getReference("Friends")
                    .child(firebaseAcctId)
                    .child(yourFriendFromDb[deleteFriends.get(i)])
                    .removeValue();
        }

    }

}


