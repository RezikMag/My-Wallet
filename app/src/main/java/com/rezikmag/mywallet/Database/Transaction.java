package com.rezikmag.mywallet.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "transactions")
public class Transaction {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String transactionType;

    private int amount;

    public long date;

    public Transaction(long id, int amount, long date, String transactionType) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.transactionType=transactionType;
    }

    @Ignore
    public Transaction(int amount, long date, String transactionType) {
        this.amount = amount;
        this.date = date;
        this.transactionType=transactionType;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }
}
