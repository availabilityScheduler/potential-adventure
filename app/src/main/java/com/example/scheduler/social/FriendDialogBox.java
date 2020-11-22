package com.example.scheduler.social;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.scheduler.finalUI.CompareSchedules;
import com.example.scheduler.R;

import java.util.ArrayList;

public class FriendDialogBox extends DialogFragment {

    //friend check marks, tracks em
    ArrayList<Integer> selectedFriends;
    ArrayList<String> friendsToCompare;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Tracks chosen friends
        selectedFriends = new ArrayList<Integer>();
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
                Intent accept = new Intent(FriendDialogBox.this.getActivity(), CompareSchedules.class);
                accept.putExtra("friendsPassedToCompareSchedules", friendsToCompare);
                accept.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(accept);
                //ig in another activity class which will show the final output, we can handle the logic there by using these
                //names and retrieving their schedules to compare and show

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

}
