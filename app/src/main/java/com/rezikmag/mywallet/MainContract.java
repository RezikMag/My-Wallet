package com.rezikmag.mywallet;

import java.util.List;

public interface MainContract {

    interface View{
        void setDayAfter(Integer daysAfter);
        void setDayBefore(Integer daysBefore);

    void setCurrentFragmentData(int totalIncome, int totalExpenses);
    }

    interface Presenter{
        void addTransaction(int amount, long date, String type);
        void getFragmentData(long date);
        void geItemBefore();
        void getItemAfter();
    }

    interface Repository{

    }
}
