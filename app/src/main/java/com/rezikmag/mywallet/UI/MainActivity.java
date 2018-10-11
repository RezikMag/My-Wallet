package com.rezikmag.mywallet.UI;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rezikmag.mywallet.Database.AppDataBase;
import com.rezikmag.mywallet.MainContract;
import com.rezikmag.mywallet.MainPresenter;
import com.rezikmag.mywallet.R;

import java.util.Date;


public class MainActivity extends AppCompatActivity implements MainContract.View, ChooseDateDialogFragment.EditDateListener{
    private static final String TAG = "MainActivity";

    MainContract.Presenter presenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    AppDataBase mDb;

    private ViewPager pager;
    private MyPagerAdapter pagerAdapter;

    long date = new Date().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        pager = (ViewPager) findViewById(R.id.view_pager);
        Button mAddIncomeButton = findViewById(R.id.btn_add_income);
        Button mAddExpensesButton = findViewById(R.id.btn_add_expenses);
        Button mDateChooseButton = findViewById(R.id.btn_nav_date_chose);

        mDb = AppDataBase.getInstance(getApplicationContext());

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        presenter = new MainPresenter(this, mDb.transactionDao());
        presenter.geItemBefore();
        presenter.getItemAfter();

        configureNavigationDrawer();
        configureToolbar();

        pager.setAdapter(pagerAdapter);


        mAddIncomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.income));
            }
        });

        mAddExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.expenses));
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
//        pager.setCurrentItem(pagerAdapter.getMinDate());
    }

    void showDialog(long time) {
        // Create and show the dialog.
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = ChooseDateDialogFragment.newInstance(time);
        newFragment.show(fm, "dialog from nav");
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
        String transactionType = data.getStringExtra(ChangeBalanceActivity.TRANSACTION_TYPE);

        String category = data.getStringExtra(ChangeBalanceActivity.CATEGORY);
        date = data.getLongExtra(ChangeBalanceActivity.DATE, 0);
        int amount = data.getIntExtra(ChangeBalanceActivity.AMOUNT, 0);

        presenter.addTransaction(amount, date, transactionType,category);
        pagerAdapter.notifyDataSetChanged();
        changeAnotherDateBalance(date);
    }

    //choose date button logic
    @Override
    public void onFinishDialogSetDate(long date) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        }
        int position;
        int maxDate = pagerAdapter.getMaxDate();
        int minDate = pagerAdapter.getMinDate();
        long currentDate = new Date().getTime();

        int position1 = (int) ((date - currentDate) / (24 * 60 * 60 * 1000));
        position = minDate + position1;
        if ((position < 0) || (position > maxDate + minDate)) {
            position = -1;
        }
        if (position == -1) {
            showErrorToast();
        } else {
            pager.setCurrentItem(position);
        }
    }

    private void showErrorToast() {
        Toast.makeText(getApplicationContext(), "Невозможно перейти на выбранную дату." +
                " записей не существует", Toast.LENGTH_LONG).show();
    }

    //show last add notes date
    private void changeAnotherDateBalance(long time) {
        int minDate = pagerAdapter.getMinDate();
        long currentDate = pagerAdapter.getDayTime(0);
        int position1 = (int) ((time - currentDate) / (24 * 60 * 60 * 1000));
        int position = minDate + position1;
        pager.setCurrentItem(position);
        pagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void setDayAfter(Integer daysAfter) {
        pagerAdapter.setMaxDate(daysAfter);
        pagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void setDayBefore(Integer daysBefore) {
        pagerAdapter.setMinDate(daysBefore);
        pagerAdapter.notifyDataSetChanged();
    }

    //start activity for change balance
    private void startForResult( String transactionType ){
        Intent intent = new Intent(getApplicationContext(), ChangeBalanceActivity.class);
        intent.putExtra(ChangeBalanceActivity.DATE,pagerAdapter
                .getDayTime(pager.getCurrentItem() - pagerAdapter.getMinDate()));
        intent.putExtra(ChangeBalanceActivity.TRANSACTION_TYPE, transactionType);
        startActivityForResult(intent, ChangeBalanceActivity.ADD_CODE);
    }

}

