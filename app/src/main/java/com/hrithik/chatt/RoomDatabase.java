package com.hrithik.chatt;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

@Database(entities = {Users.class, Messages.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public static RoomDatabase instance;
    public abstract RoomDao roomDao();

    public static synchronized RoomDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabase.class, "room_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
