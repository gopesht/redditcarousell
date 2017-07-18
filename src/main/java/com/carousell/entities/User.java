package com.carousell.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 15/07/17.
 */

public class User {
    private String uid;
    private String userName;
    private List<String> upvotes = new ArrayList<>();
    private List<String> downvotes = new ArrayList<>();


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public List<String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<String> upvotes) {
        this.upvotes = upvotes;
    }

    public List<String> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(List<String> downvotes) {
        this.downvotes = downvotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!uid.equals(user.uid)) return false;
        if (!userName.equals(user.userName)) return false;
        if (!upvotes.equals(user.upvotes)) return false;
        return downvotes.equals(user.downvotes);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + upvotes.hashCode();
        result = 31 * result + downvotes.hashCode();
        return result;
    }
}
