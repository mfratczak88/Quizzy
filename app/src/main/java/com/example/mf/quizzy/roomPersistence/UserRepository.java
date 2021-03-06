package com.example.mf.quizzy.roomPersistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

public class UserRepository {
    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // nothing to do here
        }
    };
    final String DB_NAME = "users_db";
    private UsersDatabase mUsersDatabase;

    public UserRepository(Context context) {
        mUsersDatabase = Room.databaseBuilder(context, UsersDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }


    public User getUserByEmail(String email) {
        return mUsersDatabase.getUserDao().getUserByEmail(email);
    }

    public User getUserByName(String name) {
        return mUsersDatabase.getUserDao().getUserByName(name);
    }

    public User getUserById(int id) {
        return mUsersDatabase.getUserDao().getUserById(id);
    }

    public List<User> getAllUsers() {
        return mUsersDatabase.getUserDao().getAllUsers();
    }

    public Long insertUser(User user) {
        return mUsersDatabase.getUserDao().insertUser(user);
    }

    public int countUsers() {
        return mUsersDatabase.getUserDao().countUsers();
    }

    public void updateUser(User user) {
        mUsersDatabase.getUserDao().updateUser(user);
    }

    public void deleteUser(User user) {
        mUsersDatabase.getUserDao().deleteUser(user);
    }


    public List<Category> getAllCategories() {
        return mUsersDatabase.getCategoryDao().getAllCategories();
    }

    public Category getCategoryById(int categoryId) {
        return mUsersDatabase.getCategoryDao().getCategoryById(categoryId);
    }

    public Category getCategoryByExternalId(String externalId) {
        return mUsersDatabase.getCategoryDao().getCategoryByExternalId(externalId);
    }

    public Category getCategoryByName(String categoryName) {
        return mUsersDatabase.getCategoryDao().getCategoryByName(categoryName);
    }

    public int countCategories() {
        return mUsersDatabase.getCategoryDao().countCategories();
    }


    public Long insertCategory(Category category) {
        return mUsersDatabase.getCategoryDao().insertCategory(category);
    }


    public void updateCategory(Category category) {
        mUsersDatabase.getCategoryDao().updateCategory(category);
    }


    public void deleteCategory(Category category) {
        mUsersDatabase.getCategoryDao().deleteCategory(category);
    }


    public List<Points> getAllPointsForUserIdInAllCategories(int userId) {
        return mUsersDatabase.getPointsDao().getAllPointsForUserIdInAllCategories(userId);
    }


    public Points getPointsForUserIdInCategory(int userId, int categoryId) {
        return mUsersDatabase.getPointsDao().getPointsForUserIdInCategory(userId, categoryId);
    }


    public Long insertPoints(Points points) {
        return mUsersDatabase.getPointsDao().insertPoints(points);
    }


    public void updatePoints(Points points) {
        mUsersDatabase.getPointsDao().updatePoints(points);
    }


    public void deletePoints(Points points) {
        mUsersDatabase.getPointsDao().deletePoints(points);
    }

    public Settings getSettingsForUserId(int userId) {
        return mUsersDatabase.getSettingsDao().getSettingsForUserId(userId);
    }

    public Long insertSettings(Settings settings) {
        return mUsersDatabase.getSettingsDao().insertSettings(settings);
    }

    public void insertUserAndSettings(User user, Settings settings) {
        SettingsDao settingsDao = mUsersDatabase.getSettingsDao();
        mUsersDatabase.getUserDao().insertUserAndSettings(user, settingsDao, settings);
    }


    public void updateSettings(Settings settings) {
        mUsersDatabase.getSettingsDao().updateSettings(settings);
    }

    public void deleteSettings(Settings settings) {
        mUsersDatabase.getSettingsDao().deleteSettings(settings);
    }
}
