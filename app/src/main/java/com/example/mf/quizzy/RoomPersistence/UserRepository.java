package com.example.mf.quizzy.RoomPersistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

public class UserRepository {

    private final String DB_NAME = "users_db";

    private UsersDatabase mUsersDatabase;

    public UserRepository(Context context) {
        mUsersDatabase = Room.databaseBuilder(context, UsersDatabase.class, DB_NAME).build();
    }

    public LiveData<User> getUserByEmail(String email) {
        return mUsersDatabase.daoAccess().getUserByEmail(email);
    }

    public LiveData<User> getUserByName(String name) {
        return mUsersDatabase.daoAccess().getUserByName(name);
    }

    public LiveData<List<User>> getAllUsers() {
        return mUsersDatabase.daoAccess().getAllUsers();
    }

    public Long insertUser(User user) {
        return mUsersDatabase.daoAccess().insertUser(user);
    }

    public void updateUser(User user) {
        mUsersDatabase.daoAccess().updateUser(user);
    }

    public void deleteUser(User user) {
        mUsersDatabase.daoAccess().deleteUser(user);
    }
}
