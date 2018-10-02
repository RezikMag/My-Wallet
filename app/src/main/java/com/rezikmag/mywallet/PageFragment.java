package com.rezikmag.mywallet;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    static final String ARGUMENT_TOTAL_INCOME = "income";
    static final String ARGUMENT_TRANSACTION_INCOME = "transaction income";
    static final String ARGUMENT_TOTAL_EXPENSES = "expenses";
    static final String ARGUMENT_TRANSACTION_EXPENSES = "transaction expenses";

    public PageFragment() {
        // Required empty public constructor
    }

        public static PageFragment newInstance(int income, ArrayList<Integer> dayIncomeList,
                                               int expenses, ArrayList<Integer> dayExpencesList){
        PageFragment fragment = new PageFragment();
        Bundle args =new Bundle();
        args.putInt(ARGUMENT_TOTAL_INCOME,income);
        args.putInt(ARGUMENT_TOTAL_EXPENSES,expenses);
        args.putIntegerArrayList(ARGUMENT_TRANSACTION_EXPENSES,dayExpencesList);
        args.putIntegerArrayList(ARGUMENT_TRANSACTION_INCOME,dayIncomeList);
        fragment.setArguments(args);
        return fragment;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        TextView tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvTotalIncome.setText("Income: " + getArguments().getInt(ARGUMENT_TOTAL_INCOME) + " Rub.");

        TextView tvTotalExpenses = view.findViewById(R.id.tv_total_expenses);
        tvTotalExpenses.setText("Expenses: " + getArguments().getInt(ARGUMENT_TOTAL_EXPENSES) + " Rub.");

        TextView tvListIncome = view.findViewById(R.id.tv_list_income);
        TextView tvListExpenses = view.findViewById(R.id.tv_list_expenses);

        TextView tvBalance = view.findViewById(R.id.tv_balance);

        int balance = getArguments().getInt(ARGUMENT_TOTAL_INCOME) -
                getArguments().getInt(ARGUMENT_TOTAL_EXPENSES);
        tvBalance.setText("Balance: " + balance + " Rub.");
        if (balance<0){
            tvBalance.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
        }

       ArrayList<Integer> listIncome = getArguments().getIntegerArrayList(ARGUMENT_TRANSACTION_INCOME);
        ArrayList<Integer> listExpenses = getArguments().getIntegerArrayList(ARGUMENT_TRANSACTION_EXPENSES);

        StringBuilder incomeList = new StringBuilder();
        if (listIncome != null && listIncome.size() > 0) {
            for (int a : listIncome) {
                incomeList.append("+").append(a).append(getString(R.string.rub) + "\n");
            }
            tvListIncome.setText(incomeList.toString());
        }
        StringBuilder expensesList = new StringBuilder();

        if (listExpenses!=null && listExpenses.size()>0) {
            for (int a : listExpenses) {
                expensesList.append("-").append(a).append(getString(R.string.rub) + "\n");
            }
            tvListExpenses.setText(expensesList.toString());
        }
        return view;
    }

}
