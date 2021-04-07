package com.hrithik.chatt;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel  {

    private Repository repository;
    private LiveData<List<Users>> allUsers;
    private LiveData<List<Messages>> messages;


    public ViewModel(Application application, long id) {
        super(application);
        repository = new Repository(application, id);
    }

    public void insertUser(Users user){
        repository.insertUser(user);
    }

    public void insertMessage(Messages msg) {
        repository.insertMessage(msg);
    }

    public void clearAllTables() {
        repository.clearAllTables();
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<Messages>> getMessages() {
        return messages;
    }
}
