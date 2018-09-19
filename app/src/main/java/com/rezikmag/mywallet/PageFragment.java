package com.rezikmag.mywallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    static final String ARGUMENT_INCOME = "income";
    static final String ARGUMENT_DATE = "date";

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        date = getArguments().getLong(ARGUMENT_DATE);
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        TextView tvIncome = (TextView) view.findViewById(R.id.tv_income);
        tvIncome.setText("Income: " + getArguments().getInt(ARGUMENT_INCOME));
        return view;
    }

}
