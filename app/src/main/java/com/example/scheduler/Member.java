package com.example.scheduler;

public class Member {
    private String aName, ID;
    //Maybe add image string/uri as well

    public Member(){}

    public Member(String aName, String ID) {
        this.aName = aName;
        this.ID = ID;
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
