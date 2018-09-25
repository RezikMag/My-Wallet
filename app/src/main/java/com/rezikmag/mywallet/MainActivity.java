package com.rezikmag.mywallet;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rezikmag.mywallet.Database.AppDataBase;
import com.rezikmag.mywallet.Database.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements ChooseDateDialogFragment.EditDateListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static AppDataBase mDb;

    private ViewPager pager;
    private MyPagerAdapter pagerAdapter;

    long date = new Date().getTime();

    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        pager = (ViewPager) findViewById(R.id.view_pager);
        Button mAddIncomeButon = findViewById(R.id.btn_add_income);
        Button mAddExpensesButton = findViewById(R.id.btn_add_expenses);
        Button mDateChooseButton = findViewById(R.id.btn_nav_date_chose);

        mDb = AppDataBase.getInstance(getApplicationContext());

        configureNavigationDrawer();
        configureToolbar();

        mDisposable = new CompositeDisposable();

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pagerAdapter.getDaysBeforeCurrent());
/*
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/


        // нужно рефакторить? вынести в отдельный метод?
        mAddIncomeButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeBalanceActivity.class);
                intent.putExtra("showDate", pagerAdapter.getDayTime(pager.getCurrentItem()
                        - pagerAdapter.getDaysBeforeCurrent()));
                startActivityForResult(intent, ChangeBalanceActivity.ADD_INCOME_BUTTON_CODE);
            }
        });

        mAddExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeBalanceActivity.class);
                intent.putExtra("showDate", pagerAdapter.getDayTime(pager.getCurrentItem()
                        - pagerAdapter.getDaysBeforeCurrent()));
                startActivityForResult(intent, ChangeBalanceActivity.ADD_EXPENSES_BUTTON_CODE);
            }
        });

        mDateChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(date);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
//        onFinishDialogSetDate(date);
    }

    void showDialog(long time) {
        // Create and show the dialog.
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = ChooseDateDialogFragment.newInstance(time);
        newFragment.show(fm, "dialog from nav");
    }

    public static AppDataBase getDb() {
        return mDb;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (itemId) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Синхронизировать состояние выключателя после onRestoreInstanceState.
        mDrawerToggle.syncState();
    }


    private void configureNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
//                mDrawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String transactionType = "";
        switch (requestCode) {
            case ChangeBalanceActivity.ADD_EXPENSES_BUTTON_CODE:
                transactionType = getString(R.string.expenses);
                break;
            case ChangeBalanceActivity.ADD_INCOME_BUTTON_CODE:
                transactionType = getString(R.string.income);
                break;
        }


        date = data.getLongExtra("time", 0);
        final int amount = data.getIntExtra("amount", 0);
        final String finalTransactionType = transactionType;

        final Completable completable = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Transaction transaction = new Transaction(amount, date, finalTransactionType);
                mDb.transactionDao().insert(transaction);
            }
        });
        mDisposable.add(completable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
//        Log.d("DB_LOG","itemCount"+ pagerAdapter.getCount());

        changeAnotherDateBalance(date);
    }

    @Override
    public void onFinishDialogSetDate(final long date) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        }

        final long[] minDate = new long[1];
        final long[] maxDate = new long[1];

        mDisposable.add(
                Observable.fromCallable(
                        new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                long minDefaultDate = pagerAdapter.getDayTime(-MyPagerAdapter.MIN_DAYS_NUMBER);
                                final int position;

                                Log.d("Tag", "Synchronized block start new Thread");
                                if (mDb.transactionDao().getTransactionsCount() == 0) {
                                    minDate[0] = minDefaultDate;
                                    maxDate[0] = new Date().getTime();
                                } else {
                                    long minDbDate = mDb.transactionDao().getMinDate();
                                    long maxDbDate = mDb.transactionDao().getMaxDate();
                                    if (maxDbDate > new Date().getTime()) {
                                        maxDate[0] = maxDbDate;
                                    } else {
                                        maxDate[0] = new Date().getTime();
                                    }
                                    if (minDbDate < minDefaultDate) {
                                        minDate[0] = minDbDate;
                                    } else {
                                        minDate[0] = minDefaultDate;
                                    }
                                }
                                if ((date < minDate[0]) || (date > maxDate[0])) {
                                    position = -1;
                                } else {
                                    long difference = date - minDate[0];
                                    position = (int) (difference / (24 * 60 * 60 * 1000));
                                }
                                return position;
                            }
                        }
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                {
                                    if (integer == -1) {
                                        Log.d("Tag", "mindate:" + minDate[0] + "maxDate:" + maxDate[0]);
                                        showErrorToast();
                                    } else {
                                        pager.setCurrentItem(integer);
                                    }
                                }
                            }
                        }));

    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Невозможно перейти на выбранную дату." +
                " записей не существует", Toast.LENGTH_LONG).show();
    }

    private void changeAnotherDateBalance(final long date) {
        mDisposable.add(Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                final long minDate;
                final int position;
                long minDefaultDate = pagerAdapter.getDayTime(-MyPagerAdapter.MIN_DAYS_NUMBER);
                long minDbDate = mDb.transactionDao().getMinDate();
                if (minDbDate < minDefaultDate) {
                    minDate = minDbDate;
                } else {
                    minDate = minDefaultDate;
                }
                long difference = date - minDate;
                position = (int) (difference / (24 * 60 * 60 * 1000));
                return position;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        pagerAdapter.notifyDataSetChanged();
                        pager.setCurrentItem(integer);
                    }
                }));
    }
}
