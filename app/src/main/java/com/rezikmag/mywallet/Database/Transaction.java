package com.rezikmag.mywallet.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String transactionType;

    public int amount;

//    @TypeConverters({TimeConverters.class})
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
}
