package com.rezikmag.mywallet;

import com.rezikmag.mywallet.Database.Transaction;

import java.util.List;

public interface PagerFpagmentContract {

    interface View{

        void setIncomeAndExpenses(int totalIncome,int totalExpenses);

        void setTransactionsList( List<Transaction> list, List<Transaction> list2);
    }

    interface Presenter{
        void getIncomeAndExpenses (long date);
        void getTransactions(long date);
        void onDetach();
    }

}
