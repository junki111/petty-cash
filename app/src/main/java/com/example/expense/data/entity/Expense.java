package com.example.expense.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "expense")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "merchant")
    private String mMerchant;
    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;
    @NonNull
    @ColumnInfo(name = "amount")
    private String mAmount;
    @NonNull
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "category")
    private String mCategory;


    public String getCategory() {
        return this.mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchant() {
        return this.mMerchant;
    }

    public void setMerchant(String merchant) {
        this.mMerchant = merchant;
    }

    public String getDate() {
        return this.mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getAmount() {
        return this.mAmount;
    }

    public void setAmount(String amount) {
        this.mAmount = amount;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
