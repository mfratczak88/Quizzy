package com.example.mf.quizzy.roomPersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public abstract class UserDao {

    @Insert
    public abstract Long insertUser(User user);

    @Query("SELECT * FROM user")
    public abstract List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE email =:email")
    public abstract User getUserByEmail(String email);

    @Query("SELECT * FROM user WHERE email =:name")
    public abstract User getUserByName(String name);

    @Query("SELECT * FROM user where id =:id")
    public abstract User getUserById(int id);

    @Query("SELECT COUNT(*) from user")
    public abstract int countUsers();

    @Update
    public abstract void updateUser(User user);

    @Delete
    public abstract void deleteUser(User user);

    @Transaction
    public void insertUserAndSettings(User user, SettingsDao settingsDao, Settings settings) {
        long result = insertUser(user);
        int id = (int) result;
        settings.setUserId(id);
        settingsDao.insertSettings(settings);
    }
}
