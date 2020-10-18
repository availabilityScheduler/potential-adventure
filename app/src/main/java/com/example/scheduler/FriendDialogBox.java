package com.example.scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class FriendDialogBox extends DialogFragment {
    ArrayList selectedFriends;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedFriends = new ArrayList(); //tracks chosen friends
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
