package com.rezikmag.mywallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    static final int MIN_DAYS_NUMBER = 30;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.US);
        long time = getDayTime(position - getDaysBeforeCurrent());
        Date date = new Date(time);
        return sdf.format(date);
    }

    MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        final long date = getDayTime(position - getDaysBeforeCurrent());

        //set day total expenses

        Fragment fragment = PageFragment.newInstance(MainActivity.getDb().transactionDao().getSumDayIncome(date),
                MainActivity.getDb().transactionDao().getSumDayExpenses(date),
                (ArrayList<Integer>) MainActivity.getDb().transactionDao().getAllDayIncome(date),
                (ArrayList<Integer>) MainActivity.getDb().transactionDao().getAllDayExpenses(date));
        Log.d("RX_Test", "getItem: " + position);

        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.d("RX_Test", "ItemPosition");
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        Log.d("RX_Test", "ItemCount: " + (getDaysBeforeCurrent() + getDaysAfterCurrent() + 1));
        return getDaysBeforeCurrent() + getDaysAfterCurrent() + 1;
    }

    public int getDaysBeforeCurrent() {
        long currentDayTime = getDayTime(0);
        long minShowTime = getDayTime(-MIN_DAYS_NUMBER);
        if (MainActivity.getDb().transactionDao().getTransactionsCount() > 0) {
            long minDbTime = MainActivity.getDb().transactionDao().getMinDate();
            if (minDbTime < minShowTime && minDbTime != 0) {
                minShowTime = minDbTime;
            }
        }
        int dayRange = (int) TimeUnit.DAYS.convert(
                currentDayTime - minShowTime, TimeUnit.MILLISECONDS);
        Log.d("RX_Test", "getDayBefore:" + dayRange);
        return dayRange;
    }

    public int getDaysAfterCurrent() {
        long currentDayTime = getDayTime(0);
                long maxDbTime = MainActivity.getDb().transactionDao().getMaxDate();
                if (maxDbTime < currentDayTime) {
                    return 0;
                } else {
                   int dayRange = (int) TimeUnit.DAYS.convert(maxDbTime - currentDayTime, TimeUnit.MILLISECONDS);
                    Log.d("RX_Test", "getDayAfter:" + dayRange);
                    return dayRange;
                }
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

