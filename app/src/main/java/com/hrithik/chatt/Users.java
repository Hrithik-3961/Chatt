package com.hrithik.chatt;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

@Entity
public class Users {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    private long id;

    private String uid;
    private String name;
    private String emailId;

    public Users() {
        //required for firebase
    }

    public Users(String uid, String name, String emailId) {
        this.uid = uid;
        this.name = name;
        this.emailId = emailId;
    }

    @Exclude
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
