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
    static final int PAGE_COUNT = 5;
    int range;

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.US);
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR , position - getDayRange()+1);
        Date date = c.getTime();
        String title =   sdf.format(date);
        return title;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PageFragment();


        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR , position - getDayRange()+1);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long date = calendar.getTimeInMillis();
        Bundle args = new Bundle();
        ArrayList<Integer> dayIncome = (ArrayList<Integer>) MainActivity.mDb
                .transactionDao().getAllDayIncome(date);
        args.putIntegerArrayList(PageFragment.ARGUMENT_INCOME,dayIncome);
        int income = MainActivity.mDb.transactionDao().getSumDayIncome(date);
        args.putInt(PageFragment.ARGUMENT_TOTAL_INCOME,income);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return getDayRange();
    }

    public int getDayRange(){
        long maxTime = MainActivity.mDb.transactionDao().getMaxDate();
        long minTime = MainActivity.mDb.transactionDao().getMinDate();
        int dayRange = (int) TimeUnit.DAYS.convert(maxTime-minTime,TimeUnit.MILLISECONDS);

        int range = (dayRange+1<PAGE_COUNT) ? PAGE_COUNT : dayRange+1;
//        Log.d("Tag",""+ range);
        return range;
    }
}

