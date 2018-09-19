package com.rezikmag.mywallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    static final String ARGUMENT_TOTAL_INCOME = "income";
    static final String ARGUMENT_INCOME = "one income";

    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        date = getArguments().getLong(ARGUMENT_DATE);
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        TextView tvTotalIncome = (TextView) view.findViewById(R.id.tv_total_income);
        tvTotalIncome.setText("Income: " + getArguments().getInt(ARGUMENT_TOTAL_INCOME)+ " Rub.");

        TextView tvListIncome = (TextView) view.findViewById(R.id.tv_list_income);
        ArrayList<Integer> listIncome = getArguments().getIntegerArrayList(ARGUMENT_INCOME);
        StringBuffer incomeList = new StringBuffer();
        if (listIncome!=null && listIncome.size()>0) {
            for (int a : listIncome) {
    incomeList.append("+").append(a).append(getString(R.string.rub) +"\n");
            }
            tvListIncome.setText(incomeList.toString());
        }
        return view;
    }

}
