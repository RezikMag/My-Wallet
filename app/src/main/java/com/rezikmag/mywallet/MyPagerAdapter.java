package com.rezikmag.mywallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    static final int PAGE_COUNT = 10;

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.US);
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR , position - PAGE_COUNT+1);
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

        Bundle args = new Bundle();
        args.putInt(PageFragment.ARGUMENT_PAGE_NUMBER,position+1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

