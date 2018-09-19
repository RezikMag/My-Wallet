package com.rezikmag.mywallet.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions")
    List<Transaction> getTransactions();

    @Query("SELECT SUM(amount) FROM transactions WHERE date=:currentDate")
    int getDayIncome(long currentDate);

    @Query("SELECT MAX(date) FROM transactions")
    long getMaxDate();

    @Query("SELECT MIN(date) FROM transactions")
    long getMinDate();
}
