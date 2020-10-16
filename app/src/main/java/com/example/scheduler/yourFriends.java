package com.example.scheduler;

public class yourFriends {
    private String userID;
    Boolean friends, notFriends;

    public yourFriends(){}

    public yourFriends(String userID, Boolean friends) {
        this.userID = userID;
        this.friends = friends;
    }

    public String getUserID() {
        return userID;
    }

    public String setUserID(String userID) {
        return userID;
    }

    public void getFriends(boolean friends) {
        this.friends = friends;
    }

    public void setFriends(boolean friends) {
        this.friends = true;
    }


}
