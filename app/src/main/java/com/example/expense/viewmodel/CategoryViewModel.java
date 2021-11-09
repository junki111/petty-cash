package com.example.expense.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.expense.data.ExpenseRoomDatabase;
import com.example.expense.data.entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final List<Category> opsListList;
    private ExpenseRoomDatabase db;

    public CategoryViewModel(Application application) {
        super(application);

        db = ExpenseRoomDatabase.getExpenseRoomDatabaseInstance(this.getApplication());
        opsListList = db.categoryDao().getAllCategoriesList();
    }

    public List<Category> getCategoriesList() { return opsListList; }

    public void insertCategory(Category Category) {
        db.categoryDao().insertCategory(Category);
    }

    public void updateCategory(Category Category) {
        db.categoryDao().updateCategory(Category);
    }

    public void deleteCategory(Category Category) {
        db.categoryDao().deleteCategory(Category);
    }

    public void deleteAllCategories() {
        db.categoryDao().deleteAllCategories();
    }
}
