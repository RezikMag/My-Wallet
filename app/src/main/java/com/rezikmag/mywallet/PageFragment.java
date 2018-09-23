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
    static final String ARGUMENT_TRANSACTION_INCOME = "transaction income";
    static final String ARGUMENT_TOTAL_EXPENSES = "expenses";
    static final String ARGUMENT_TRANSACTION_EXPENSES ="transaction expenses";

    public PageFragment() {
        // Required empty public constructor
    }

    TextView tvListIncome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        TextView tvTotalIncome = (TextView) view.findViewById(R.id.tv_total_income);
        tvTotalIncome.setText("Income: " + getArguments().getInt(ARGUMENT_TOTAL_INCOME)+ " Rub.");

        TextView tvTotalExpenses = view.findViewById(R.id.tv_total_expenses);
        tvTotalExpenses.setText("Expenses: "+ getArguments().getInt(ARGUMENT_TOTAL_EXPENSES) + " Rub.");


         tvListIncome = (TextView) view.findViewById(R.id.tv_list_income);
        TextView tvListExpenses = view.findViewById(R.id.tv_list_expenses);

//        ArrayList<Integer> listIncome = getArguments().getIntegerArrayList(ARGUMENT_TRANSACTION_INCOME);
        ArrayList<Integer> listExpenses = getArguments().getIntegerArrayList(ARGUMENT_TRANSACTION_EXPENSES);

        StringBuffer incomeList = new StringBuffer();
  /*      if (listIncome!=null && listIncome.size()>0) {
            for (int a : listIncome) {
    incomeList.append("+").append(a).append(getString(R.string.rub) +"\n");
            }
            tvListIncome.setText(incomeList.toString());
        }
*/
        StringBuffer expensesList = new StringBuffer();
        if (listExpenses!=null && listExpenses.size()>0) {
            for (int a : listExpenses) {
                expensesList.append("-").append(a).append(getString(R.string.rub) + "\n");
            }
            tvListExpenses.setText(expensesList.toString());
        }
            return view;
    }

}
