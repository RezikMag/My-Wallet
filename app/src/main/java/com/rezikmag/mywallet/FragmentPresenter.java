package com.rezikmag.mywallet;

import android.util.Log;

import com.rezikmag.mywallet.Database.Transaction;
import com.rezikmag.mywallet.Database.TransactionDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentPresenter implements PagerFpagmentContract.Presenter {
    private static final String TAG = "Tag";

    final TransactionDao transactionDao;
    CompositeDisposable mDisposable =new CompositeDisposable();

    final PagerFpagmentContract.View mView;


    public FragmentPresenter(PagerFpagmentContract.View mView, TransactionDao transactionDao) {
        this.mView = mView;
        this.transactionDao = transactionDao;
    }

    @Override
    public void getIncomeAndExpenses(long date) {
        mDisposable.add(Flowable.combineLatest(transactionDao.getSumDayIncome(date),
                transactionDao.getSumDayExpenses(date), new BiFunction<List<Integer>,List <Integer>, List<Integer>>() {
                    @Override
                    public List<Integer> apply(List<Integer> integer, List<Integer> integer2) throws Exception {
                            List<Integer> list = new ArrayList<>();
                        if (integer.get(0)!=null) {
                            list.addAll(integer);
                        }else list.add(0);
                        if (integer2.get(0)!=null) {
                            list.addAll(integer2);
                        }else{
                            list.add(0);
                        }
                        return list;
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Integer>>() {
                            @Override
                            public void accept(List<Integer> integers) throws Exception {

                                   int income = integers.get(0);
                                  int  expenses = integers.get(1);

                                Log.d(TAG, "expense: "+ expenses + ",income: "+ income);
                                mView.setIncomeAndExpenses(income,expenses);
                            }
                        }));
    }

    @Override
    public void getTransactions(long date) {
        mDisposable.add(Flowable.combineLatest(
                transactionDao.getAllDayIncome(date)
                , transactionDao.getAllDayExpenses(date), new BiFunction<List<Transaction>, List<Transaction>,
                        List<List<Transaction>>>() {
                    @Override
                    public List<List<Transaction>> apply(List<Transaction> list, List<Transaction> list2) throws Exception {
                        List<List<Transaction>> lists = new ArrayList<>();
                        lists.add(list);
                        lists.add(list2);
                        return lists;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<List<Transaction>>>() {
                    @Override
                    public void accept(List<List<Transaction>> integers) throws Exception {
                        Log.d(TAG, "income: " + integers);
                        List<Transaction> income = integers.get(0);
                        List<Transaction> expenses = integers.get(1);
                        mView.setTransactionsList(income,expenses);
                    }
                }));
    }

    @Override
    public void onDetach() {
        mDisposable.dispose();
    }
}
