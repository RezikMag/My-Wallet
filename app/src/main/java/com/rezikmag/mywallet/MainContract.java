package com.rezikmag.mywallet;

import com.rezikmag.mywallet.Database.Transaction;

import java.util.List;

public interface MainContract {

    interface View{
        void setDayAfter(Integer daysAfter);
        void setDayBefore(Integer daysBefore);

            }

    interface Presenter{
        void addTransaction(int amount, long date, String type, String category);
        void geItemBefore();
        void getItemAfter();
    }


}
