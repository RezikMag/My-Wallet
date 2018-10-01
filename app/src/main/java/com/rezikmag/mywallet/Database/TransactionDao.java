package com.rezikmag.mywallet.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions")
    Flowable<List<Transaction>> getTransactions();

    @Query("SELECT MIN(date) FROM transactions")
    Flowable<Long> getMinDate();

    @Query("SELECT MAX(date) FROM transactions")
    Flowable<Long> getMaxDate();

    @Query("SELECT SUM(amount) FROM transactions WHERE date=:currentDate AND transactionType='Income'")
    Flowable<Integer> getSumDayIncome(long currentDate);

    @Query("SELECT amount FROM transactions WHERE date=:currentDate AND transactionType='Income'")
    Flowable<List<Integer>> getAllDayIncome(long currentDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE date=:currentDate AND transactionType='Expenses'")
    Flowable<Integer> getSumDayExpenses(long currentDate);

    @Query("SELECT COUNT(*) FROM transactions")
    Flowable<Integer> getTransactionsCount();

    @Query("SELECT amount FROM transactions WHERE date=:currentDate AND transactionType='Expenses'")
    Flowable<List<Integer>> getAllDayExpenses(long currentDate);

}
