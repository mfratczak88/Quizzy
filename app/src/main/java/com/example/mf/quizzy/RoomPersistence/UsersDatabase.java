package com.example.mf.quizzy.RoomPersistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class, Category.class, Points.class}, version = 7, exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    public abstract CategoryDao getCategoryDao();
    public abstract PointsDao getPointsDao();
}
