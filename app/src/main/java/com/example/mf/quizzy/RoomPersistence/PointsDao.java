package com.example.mf.quizzy.RoomPersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PointsDao {

    @Query("SELECT * FROM points WHERE userId=:userId ")
    List<Points> getAllPointsForUserIdInAllCategories(int userId);

    @Query("SELECT * FROM points WHERE userId=:userId AND categoryId=:categoryId")
    Points getPointsForUserIdInCategory(int userId, int categoryId);

    @Insert
    Long insertPoints(Points points);

    @Update
    void updatePoints(Points points);

    @Delete
    void deletePoints(Points points);

}
