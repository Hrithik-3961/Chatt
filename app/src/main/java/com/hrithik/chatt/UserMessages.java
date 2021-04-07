package com.hrithik.chatt;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.List;

@Entity
public class UserMessages {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    private long id;

    List<Messages> msg;

    public UserMessages() {
    }

    public UserMessages(List<Messages> msg) {
        this.msg = msg;
    }

    @Exclude
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
