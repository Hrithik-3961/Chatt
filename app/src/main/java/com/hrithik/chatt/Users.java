package com.hrithik.chatt;

public class Users {

    String uid;
    String name;
    String emailId;

    public Users() {
    }

    public Users(String uid, String name, String emailId) {
        this.uid = uid;
        this.name = name;
        this.emailId = emailId;
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
