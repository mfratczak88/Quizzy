package com.example.mf.quizzy.RoomPersistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class}, version = 6, exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
