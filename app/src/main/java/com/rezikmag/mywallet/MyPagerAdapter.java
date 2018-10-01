package com.rezikmag.mywallet;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private int maxDate;
    private int minDate;
    private int totalIncome;
    private int totalExpenses;


    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.US);
        long time = getDayTime(position - getMinDate());
        Date date = new Date(time);
        return sdf.format(date);
    }

    MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
     Fragment fragment = PageFragment.newInstance(totalIncome, totalExpenses);
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;

    }

    @Override
    public int getCount() {
        Log.d("RX_Test", "ItemCount: " + (getMinDate() + getMaxDate() + 1));
        return getMinDate() + getMaxDate() + 1;
    }


    private void setFragment(Fragment fragment) {
//        this.fragment = fragment;
    }

    public void setMaxDate(int max) {
        maxDate = max;
    }

    public int getMaxDate() {
        return maxDate;
    }

    public int getMinDate() {
        return minDate;
    }

    public void setMinDate(int minDate) {
        this.minDate = minDate;
    }

    private long getDayTime(int dayBefore) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, dayBefore);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public void setBalance(int totalIncome, int totalExpences) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpences;
    }
}

