package com.example.mf.quizzy.roomPersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface SettingsDao {

    @Query("SELECT * FROM settings WHERE userId=:userId")
    Settings getSettingsForUserId(int userId);

    @Insert
    Long insertSettings(Settings settings);

    @Update
    void updateSettings(Settings settings);

    @Delete
    void deleteSettings(Settings settings);
}
