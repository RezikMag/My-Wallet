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

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String ARGUMENT_INCOME = "income";
//    int pageNumber;


    public PageFragment() {
        // Required empty public constructor
    }



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page,container ,false);

        TextView tvPage = (TextView) view.findViewById(R.id.tv_page);
        tvPage.setText("Page"+ getArguments().getInt(ARGUMENT_PAGE_NUMBER));

        TextView tvIncome = (TextView) view.findViewById(R.id.tv_income);
        tvIncome.setText("Income: " + getArguments().getInt(ARGUMENT_INCOME));
        // Inflate the layout for this fragment
        return view;
    }

}
