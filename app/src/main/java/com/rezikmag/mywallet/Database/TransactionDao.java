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

    @Query("SELECT MIN(date) FROM transactions")
    long getMinDate();

    @Query("SELECT MAX(date) FROM transactions")
    long getMaxDate();

    @Query("SELECT SUM(amount) FROM transactions WHERE date=:currentDate AND transactionType='Income'")
    int getSumDayIncome(long currentDate);

    @Query("SELECT amount FROM transactions WHERE date=:currentDate AND transactionType='Income'")
    List<Integer> getAllDayIncome(long currentDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE date=:currentDate AND transactionType='Expenses'")
    int getSumDayExpenses(long currentDate);

    @Query("SELECT COUNT(*) FROM transactions")
    int getTransactionsCount();

    @Query("SELECT amount FROM transactions WHERE date=:currentDate AND transactionType='Expenses'")
    List<Integer> getAllDayExpenses(long currentDate);

}
