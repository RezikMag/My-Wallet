package com.rezikmag.mywallet;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rezikmag.mywallet.Database.AppDataBase;
import com.rezikmag.mywallet.Database.Transaction;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity
        extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private static AppDataBase mDb;
    ViewPager pager;

    Button mAddIncomeButon;
    Button mAddExpensesButton;

    MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        mAddIncomeButon = findViewById(R.id.add_balance);
        mAddExpensesButton = findViewById(R.id.minus_balance);

        mDb = AppDataBase.getInstanse(getApplicationContext());

        configureNavigationDrawer();
        configureToolbar();

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setCurrentItem(pagerAdapter.getDaysBeforeCurrent()+1);

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
        });

        mAddIncomeButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeBalanceActivity.class);
                intent.putExtra("showDate", pagerAdapter.getPageTitle(pager.getCurrentItem()));
                startActivityForResult(intent, ChangeBalanceActivity.ADD_INCOME_BUTTON_CODE);
            }
        });

        mAddExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangeBalanceActivity.class);
                intent.putExtra("showDate", pagerAdapter.getPageTitle(pager.getCurrentItem()));
                startActivityForResult(intent, ChangeBalanceActivity.ADD_EXPENSES_BUTTON_CODE);
            }
        });

    }

    public static AppDataBase getmDb() {
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
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

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

        long date = pagerAdapter.getDayTime(pager.getCurrentItem() -
                pagerAdapter.getDaysBeforeCurrent());

        int amount = data.getIntExtra("amount", 0);

        Transaction transaction = new Transaction(amount, date, transactionType);
        mDb.transactionDao().insert(transaction);
        Log.d("DB_LOG", "amount: " + transaction.amount +
                "date: " + transaction.date + "type: " + transaction.transactionType);

        pagerAdapter.notifyDataSetChanged();
    }
}
