package com.example.scheduler.social;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.scheduler.R;
import com.example.scheduler.finalUI.CompareSchedules;

import java.util.ArrayList;

public class FriendDialogBox extends DialogFragment {

    //friend check marks, tracks em
    private ArrayList<Integer> selectedFriends;
    private ArrayList<String> friendsToCompare;
    AlertDialog alertDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Tracks chosen friends
        selectedFriends = new ArrayList<>();
        friendsToCompare = new ArrayList<>();

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
                    selectedFriends.remove(Integer.valueOf(which));
                }
            }
        }).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for(int i=0; i <selectedFriends.size();i++){
                    friendsToCompare.add(yourFriendFromDb[selectedFriends.get(i)]);
                }
                doTheComparison();

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

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

}
