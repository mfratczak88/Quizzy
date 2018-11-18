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

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE email =:email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM user WHERE email =:name")
    User getUserByName(String name);

    @Query("SELECT * FROM user where id =:id")
    User getUserById(int id);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
