package com.example.mf.quizzy.RoomPersistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertUser(User user);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM User WHERE email =:email")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE email =:name")
    LiveData<User> getUserByName(String name);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
