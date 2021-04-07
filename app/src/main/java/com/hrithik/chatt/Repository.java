package com.hrithik.chatt;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private RoomDao roomDao;
    private LiveData<List<Users>> allUsers;
    private LiveData<List<UserMessages>> messages;
    private RoomDatabase roomDatabase;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Repository(Application application, long id) {
        roomDatabase = RoomDatabase.getInstance(application);
        roomDao = roomDatabase.roomDao();
        allUsers = roomDao.getAllUsers();
        messages = roomDao.getMessages(id);
    }

    public void insertUser(final Users user) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                roomDao.insertUser(user);
            }
        });
    }

    public void insertMessage(final Messages msg){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                roomDao.insertMessage(msg);
            }
        });
    }

    public void clearAllTables(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                roomDatabase.clearAllTables();
            }
        });
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<UserMessages>> getMessages() {
        return messages;
    }
}
