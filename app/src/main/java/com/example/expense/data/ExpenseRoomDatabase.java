package com.example.expense.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.expense.data.dao.CategoryDao;
import com.example.expense.data.dao.ExpenseDao;
import com.example.expense.data.entity.Category;
import com.example.expense.data.entity.Expense;
import com.example.expense.util.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Category.class}, version = 3, exportSchema = false)
public abstract class ExpenseRoomDatabase extends RoomDatabase {
    private static ExpenseRoomDatabase INSTANCE;

    public static ExpenseRoomDatabase getExpenseRoomDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ExpenseRoomDatabase.class, Config.DB_NAME)
                    .allowMainThreadQueries()// Allowed only for 'if exists' queries and other exceptions
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();

}
