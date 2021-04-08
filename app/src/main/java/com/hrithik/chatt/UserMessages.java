package com.hrithik.chatt;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserMessages {

    @PrimaryKey(autoGenerate = true)
    @Exclude
    private long id;

    @TypeConverters(Converter.class)
    ArrayList<Messages> msg;

    String roomId;

    public UserMessages() {
    }

    public UserMessages(ArrayList<Messages> msg, String roomId) {
        this.msg = msg;
        this.roomId = roomId;
    }

    @Exclude
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Messages> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<Messages> msg) {
        this.msg = msg;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
