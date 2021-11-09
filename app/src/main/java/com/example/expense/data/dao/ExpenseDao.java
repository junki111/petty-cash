package com.example.expense.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expense.data.entity.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {
    // LiveData List
    @Query("SELECT * FROM Expense ORDER BY id ASC")
    LiveData<List<Expense>> getAllExpenses();

    // Normal List
    @Query("SELECT * FROM Expense ORDER BY id ASC")
    List<Expense> getAllExpensesList();

    @Query("SELECT COUNT(*) FROM Expense")
    int getExpensesCount();

    @Insert
    void insertExpense(Expense... Expenses);

    @Update
    void updateExpense(Expense... Expenses);

    @Delete
    void deleteExpense(Expense Expenses);

    @Query("DELETE FROM Expense")
    void deleteAllExpenses();
}
