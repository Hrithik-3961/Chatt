package com.hrithik.chatt;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private long id;

    public ViewModelFactory(Application application, long id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new com.hrithik.chatt.ViewModel(application, id);
    }
}
