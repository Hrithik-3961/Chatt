package com.hrithik.chatt;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomDao {

    @Insert
    void insertUser(Users user);

    @Insert
    void insertMessage(Messages msg);

    @Query("SELECT * FROM Users")
    LiveData<List<Users>> getAllUsers();

    @Query("SELECT * FROM UserMessages WHERE id = :id")
    LiveData<List<UserMessages>> getMessages(long id);

}
