package com.raghu.android.wakemeup.Database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.raghu.android.wakemeup.Database.Entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    public long insertCategory(Category category);

    @Delete
    public void deleteCategory(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateCategory(Category category);

    @Query("SELECT * FROM category")
    public List<Category> getCategories();

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    public Category getCategoryById(int categoryId);

    @Query("SELECT * FROM category WHERE name = :name")
    public Category getCategoryByName(String name);
}