package com.example.mf.quizzy.RoomPersistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class UserRepository {

    public final String DB_NAME = "users_db";

    private UsersDatabase mUsersDatabase;

    public UserRepository(Context context) {
        mUsersDatabase = Room.databaseBuilder(context, UsersDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    public User getUserByEmail(String email) {
        return mUsersDatabase.daoAccess().getUserByEmail(email);
    }

    public User getUserByName(String name) {

        return mUsersDatabase.daoAccess().getUserByName(name);
    }

    public User getUserById(int id) {
        return mUsersDatabase.daoAccess().getUserById(id);
    }

    public List<User> getAllUsers() {
        return mUsersDatabase.daoAccess().getAllUsers();
    }

    public Long insertUser(User user) {
        return mUsersDatabase.daoAccess().insertUser(user);
    }

    public int countUsers() {
        return mUsersDatabase.daoAccess().countUsers();
    }

    public void updateUser(User user) {
        mUsersDatabase.daoAccess().updateUser(user);
    }

    public void deleteUser(User user) {
        mUsersDatabase.daoAccess().deleteUser(user);
    }

    private static class QueryAsyncTask extends AsyncTask<String, Void, List<User>> {
        @Override
        protected List<User> doInBackground(String... strings) {
            return null;
        }
    }
}
