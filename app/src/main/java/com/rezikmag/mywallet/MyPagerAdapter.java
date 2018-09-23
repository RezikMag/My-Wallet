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
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        final PageFragment fragment = new PageFragment();

        long date = getDayTime(position - getDaysBeforeCurrent());

        final Bundle args = new Bundle();


        Disposable getDayIncome =
                MainActivity.getDb().transactionDao().getAllDayIncome(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Integer>>() {
                            @Override
                            public void accept(List<Integer> integers) throws Exception {
                               StringBuffer sb = new StringBuffer();
                                if (integers != null && integers.size() > 0) {
                                    for (int a : integers) {
                                        sb.append("+").append(a).append("Rub" +"\n");
                                    }
                                    Log.d("DB_LOG", "size" + integers.size());
                                    fragment.tvListIncome.setText(sb.toString());
                                }
                            }
                        });

        disposable.add(getDayIncome);

        ArrayList<Integer> dayExpenses = (ArrayList<Integer>) MainActivity.getDb()
                .transactionDao().getAllDayExpenses(date);


//        PageFragment.ARGUMENT_TRANSACTION_INCOME, (ArrayList<Integer>) integers);
        args.putIntegerArrayList(PageFragment.ARGUMENT_TRANSACTION_EXPENSES, dayExpenses);

        int income = MainActivity.getDb().transactionDao().getSumDayIncome(date);
        int expenses = MainActivity.getDb().transactionDao().getSumDayExpenses(date);

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
        return dayRange;
    }

    public int getDaysAfterCurrent() {
        long currentDayTime = getDayTime(0);
        long maxDbTime = MainActivity.getDb().transactionDao().getMaxDate();
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

