package com.rezikmag.mywallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    static final int MIN_DAYS_NUMBER = 30;
    int range;

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.US);
      /*  Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, position - getDaysBeforeCurrent());

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
*/

        long time =  getDayTime(position - getDaysBeforeCurrent());
        Date date = new Date(time);
        String title = sdf.format(date);
        return title;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PageFragment();

        long date = getDayTime(position - getDaysBeforeCurrent());

        Bundle args = new Bundle();

        ArrayList<Integer> dayIncome = (ArrayList<Integer>) MainActivity.getmDb()
                .transactionDao().getAllDayIncome(date);
        ArrayList<Integer> dayExpenses = (ArrayList<Integer>) MainActivity.getmDb()
                .transactionDao().getAllDayExpenses(date);

        args.putIntegerArrayList(PageFragment.ARGUMENT_TRANSACTION_INCOME, dayIncome);
        args.putIntegerArrayList(PageFragment.ARGUMENT_TRANSACTION_EXPENSES, dayExpenses);

        int income = MainActivity.getmDb().transactionDao().getSumDayIncome(date);
        int expenses = MainActivity.getmDb().transactionDao().getSumDayExpenses(date);

        args.putInt(PageFragment.ARGUMENT_TOTAL_EXPENSES, expenses);
        args.putInt(PageFragment.ARGUMENT_TOTAL_INCOME, income);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return getDaysBeforeCurrent() + getDAysAfterCurrent() + 1;
    }

    public int getDaysBeforeCurrent() {
        long minDbTime = MainActivity.getmDb().transactionDao().getMinDate();
        long currentDayTime = getDayTime(0);
        long minShowTime = getDayTime(-MIN_DAYS_NUMBER);
        if (minDbTime < minShowTime) {
            minShowTime = minDbTime;
        }

        int dayRange = (int) TimeUnit.DAYS.convert(currentDayTime - minShowTime, TimeUnit.MILLISECONDS);
        return dayRange;
    }


    public int getDAysAfterCurrent() {
        long currentDayTime = getDayTime(0);
        long maxDbTime = MainActivity.getmDb().transactionDao().getMaxDate();
        if (maxDbTime < currentDayTime) {
            return 0;
        }
        int dayRange = (int) TimeUnit.DAYS.convert(maxDbTime - currentDayTime, TimeUnit.MILLISECONDS);
        return dayRange;
    }

    public long getDayTime(int dayBefore) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, dayBefore);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}

