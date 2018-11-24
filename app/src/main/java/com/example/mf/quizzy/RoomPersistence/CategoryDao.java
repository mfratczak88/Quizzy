package com.example.mf.quizzy.roomPersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category WHERE id=:id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM category WHERE external_id=:externalId")
    Category getCategoryByExternalId(String externalId);

    @Query("SELECT * FROM category WHERE name=:categoryName")
    Category getCategoryByName(String categoryName);

    @Query("SELECT COUNT(*) from category")
    int countCategories();

    @Insert
    Long insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);


}
