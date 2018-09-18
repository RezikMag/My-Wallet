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

//    public String transactionType;

    public int amount;

    @TypeConverters({TimeConverters.class})
    public Date date;

    public Transaction(long id, int amount, Date date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    @Ignore
    public Transaction(int amount, Date date) {
        this.amount = amount;
        this.date = date;
    }
}
