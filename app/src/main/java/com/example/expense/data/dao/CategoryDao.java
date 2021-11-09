package com.example.expense.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expense.data.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    //Normal list
    @Query("SELECT * FROM Categories ORDER BY id ASC")
    List<Category> getAllCategoriesList();

    @Insert
    void insertCategory(Category... categories);

    @Update
    void updateCategory(Category... categories);

    @Delete
    void deleteCategory(Category... categories);

    @Query("DELETE FROM Categories")
    void deleteAllCategories();
}
