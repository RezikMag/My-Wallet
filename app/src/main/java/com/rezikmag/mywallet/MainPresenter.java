package com.rezikmag.mywallet;

import android.util.Log;

import com.rezikmag.mywallet.Database.Transaction;
import com.rezikmag.mywallet.Database.TransactionDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "Tag";
    private static final int MIN_DAYS_NUMBER = 10;

    TransactionDao transactionDao;
    CompositeDisposable mDisposable =new CompositeDisposable();

    MainContract.View mView;;

    public MainPresenter(MainContract.View mView, TransactionDao transactionDao) {
        this.mView = mView;
        this.transactionDao = transactionDao;

    }


    @Override
    public void getFragmentData(long date) {
        mDisposable.add(Flowable.combineLatest(transactionDao.getSumDayIncome(date),
                transactionDao.getSumDayExpenses(date), new BiFunction<Integer, Integer, List<Integer>>() {
                    @Override
                    public List<Integer> apply(Integer integer, Integer integer2) throws Exception {
                    List<Integer> list = new ArrayList<>();
                    list.add(integer);
                    list.add(integer2);
                        return list;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        int totalIncome = integers.get(0);
                        int totalExpenses = integers.get(1);
                        mView.setCurrentFragmentData(totalIncome, totalExpenses);
                    }
                }));
    }

    @Override
    public void geItemBefore() {
        mDisposable.add(transactionDao.getMinDate().map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long min) throws Exception {
                        final int dayRange;
                        long currentDayTime =getDayTime(0);
                        long minShowTime = getDayTime(-MIN_DAYS_NUMBER);
                        if (min < minShowTime && min != 0) {
                            minShowTime = min;
                        }
                        dayRange = (int) TimeUnit.DAYS.convert(
                                currentDayTime - minShowTime, TimeUnit.MILLISECONDS);
                        Log.d("RX_Test", "getDayBefore:" + dayRange);
                        return dayRange;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer daysBefore) throws Exception {
                                mView.setDayBefore(daysBefore);
                                Log.d("RX_test", "set data through the onCreate");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("RX_test", "set data through the onCreate");
                            }
                        })
        );
    }

    @Override
    public void getItemAfter() {
        mDisposable.add( transactionDao.getMaxDate().map(new Function<Long, Integer>() {
            @Override
            public Integer apply(Long max) throws Exception {
                int daysAfter;
                long currentDayTime = getDayTime(0);
                if (max < currentDayTime) {
                    daysAfter = 0;
                } else {
                    daysAfter = (int) TimeUnit.DAYS.convert(max - currentDayTime, TimeUnit.MILLISECONDS);
                }
                return daysAfter;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer maxRange) throws Exception {
                        mView.setDayAfter(maxRange);
                        Log.d("RX_Test", "getDayAfter:" + maxRange);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    public void addTransaction(final int amount, final long date, final String type) {
         mDisposable.add(Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Transaction transaction = new Transaction(amount, date, type);
                transactionDao.insert(transaction);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe());
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

}
