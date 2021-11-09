package com.example.expense.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.expense.data.ExpenseRoomDatabase;
import com.example.expense.data.entity.Expense;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private final LiveData<List<Expense>> opsList;
    private final List<Expense> opsListList;
    private ExpenseRoomDatabase db;

    public ExpenseViewModel(Application application) {
        super(application);

        db = ExpenseRoomDatabase.getExpenseRoomDatabaseInstance(this.getApplication());
        opsList = db.expenseDao().getAllExpenses();
        opsListList = db.expenseDao().getAllExpensesList();
    }

    public LiveData<List<Expense>> getExpenseLiveList() {
        return opsList;
    }

    public List<Expense> getExpenseList() {
        return opsListList;
    }

    public void insertExpense(Expense Expense) {
        db.expenseDao().insertExpense(Expense);
    }

    public void updateExpense(Expense Expense) {
        db.expenseDao().updateExpense(Expense);
    }

    public void deleteExpense(Expense Expense) {
        db.expenseDao().deleteExpense(Expense);
    }

    public void deleteAllExpenses() {
        db.expenseDao().deleteAllExpenses();
    }
}
