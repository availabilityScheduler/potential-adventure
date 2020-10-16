package com.example.scheduler;

import android.net.Uri;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Member {
    private String firstName, lastName, aName, ID;
//    public Map<ArrayList<String>, Boolean> friends = new HashMap<>();
    Map<String, List<Boolean>> friends =  new HashMap<>();


    public Member(){}

    public Member(String aName, String ID) {
        this.aName = aName;
        this.ID = ID;
    }


    public Map<String, List<Boolean>> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, List<Boolean>> friends) {
        this.friends = friends;
    }

//
//    public Map<ArrayList<String>, Boolean> getFriends(Map<ArrayList<String>, Boolean> friends) {
//        return this.friends;
//    }
//
//    public void setFriends(Map<ArrayList<String>, Boolean> friends) {
//        this.friends = friends;
//    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public void setaName(String aName) {
        this.aName = aName;
    }
    public String getaName() {
        return aName;
    }


    public void setID(String ID) {
        this.ID = ID;
    }
    public String getID() {
        return ID;
    }





}
