package com.example.scheduler;

import android.net.Uri;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Member {
    private String firstName, lastName, aName, ID;

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
