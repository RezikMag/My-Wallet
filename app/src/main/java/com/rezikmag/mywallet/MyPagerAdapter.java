package com.rezikmag.mywallet;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.rezikmag.mywallet.Data.Info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    static final int MIN_DAYS_NUMBER = 30;
    CompositeDisposable disposable = new CompositeDisposable();


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

        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        Log.d("RX_Test", "ItemCount: " + (getDaysBeforeCurrent() + getDaysAfterCurrent() + 1));
        return getDaysBeforeCurrent() + getDaysAfterCurrent() + 1;
    }

    public int getDaysBeforeCurrent() {
        final Object lock = new Object();
        final int[] dayRange = new int[1];

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    long currentDayTime = getDayTime(0);
                    long minShowTime = getDayTime(-MIN_DAYS_NUMBER);
                    if (MainActivity.getDb().transactionDao().getTransactionsCount() > 0) {
                        long minDbTime = MainActivity.getDb().transactionDao().getMinDate();
                        if (minDbTime < minShowTime && minDbTime != 0) {
                            minShowTime = minDbTime;
                        }
                    }
                    dayRange[0] = (int) TimeUnit.DAYS.convert(
                            currentDayTime - minShowTime, TimeUnit.MILLISECONDS);
                    Log.d("RX_Test", "getDayBefore:" + dayRange[0]);
                    lock.notify();
                }
            }
        }).start();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return dayRange[0];
        }
    }

    public int getDaysAfterCurrent() {
        final Info info = new Info();
        int dayRange;
       disposable.add(MainActivity.getDb().transactionDao().getMaxDate()
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<Long>() {
                   @Override
                   public void accept(Long aLong) throws Exception {
                       Log.d("RX_Test", "accept " +aLong );
                       info.setMaxDate(aLong);
                       Log.d("RX_Test", "getnfo:" + info.getMaxDate());
                   }
               }));


        long currentDayTime = getDayTime(0);
        long maxDbTime = info.getMaxDate();
        Log.d("RX_Test", "getDayAfter:" + info.getMaxDate());
        if (maxDbTime < currentDayTime) {
            dayRange = 0;
        } else {
            dayRange = (int) TimeUnit.DAYS.convert(maxDbTime - currentDayTime, TimeUnit.MILLISECONDS);
            Log.d("RX_Test", "getDayAfter:" + dayRange);
        }
        /*
        final Object lock = new Object();
        final int[] dayRange = new int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {

                    long maxDbTime = MainActivity.getDb().transactionDao().getMaxDate();
                    if (maxDbTime < currentDayTime) {
                        dayRange[0] = 0;
                    } else {
                        dayRange[0] = (int) TimeUnit.DAYS.convert(maxDbTime - currentDayTime, TimeUnit.MILLISECONDS);
                        Log.d("RX_Test", "getDayAfter:" + dayRange[0]);
                    }
                    lock.notify();
                }
            }
        }).start();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return dayRange[0];
        }*/
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

    private void setFragment(Fragment fragment) {
//        this.fragment = fragment;
    }

    private void setDayAfter(int day){
//        dayAfter =day;
    }

}

