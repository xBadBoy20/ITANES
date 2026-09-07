package com.renzoleguia.itanes.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.renzoleguia.itanes.data.local.dao.PlaceDao;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;

@Database(entities = {PlaceEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract PlaceDao placeDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "itanes_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
