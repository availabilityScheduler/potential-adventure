package com.example.scheduler.mainActivities;

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

    Map<String, Object> userSchedule = new HashMap<>();

    public Map<String, Object> getUserSchedule() {
        return userSchedule;
    }

    public void setUserSchedule(Map<String, Object> userSchedule) {
        this.userSchedule = userSchedule;
    }

    Map<String, Boolean> memberMap =  new HashMap<>();

    public void setMemberMap(Map<String, Boolean> memberMap) {
        this.memberMap = memberMap;
    }
    public Map<String, Boolean> getMemberMap() {
        return memberMap;
    }


    public Member(){}

    public Member(String aName, String ID) {
        this.aName = aName;
        this.ID = ID;
    }


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
