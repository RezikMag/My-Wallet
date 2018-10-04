package com.rezikmag.mywallet;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezikmag.mywallet.Database.Transaction;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment implements PagerFpagmentContract.View {

    static final String ARGUMENT_TIME = "time";

    PagerFpagmentContract.Presenter presenter;
    TextView tvTotalIncome;
    TextView tvTotalExpenses;
    TextView tvListIncome;
    TextView tvListExpenses;
    Button btnBalance;

    long date;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(long time) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        date = getArguments().getLong(ARGUMENT_TIME, 0);

        btnBalance = view.findViewById(R.id.btn_balance);

        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvTotalExpenses = view.findViewById(R.id.tv_total_expenses);
         tvListIncome = view.findViewById(R.id.tv_list_income);
         tvListExpenses = view.findViewById(R.id.tv_list_expenses);

        presenter = new FragmentPresenter(this, MainActivity.mDb.transactionDao());


        final LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        Button btnSportExpenses = view.findViewById(R.id.btn_sport);
        btnSportExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeBalanceActivity.class);
                intent.putExtra(ChangeBalanceActivity.DATE,date);
                intent.putExtra(ChangeBalanceActivity.TRANSACTION_TYPE,getString(R.string.expenses));
                intent.putExtra("category", getString(R.string.sport));
                getActivity().startActivityForResult(intent, ChangeBalanceActivity.ADD_EXPENSES_BUTTON_CODE);
            }
        });

        presenter.getTransactions(date);
        presenter.getIncomeAndExpenses(date);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetach();
    }

    @Override
    public void setIncomeAndExpenses(int totalIncome, int totalExpenses) {
        tvTotalIncome.setText(totalIncome + " Rub.");
        tvTotalExpenses.setText(totalExpenses + " Rub.");
        int balance = totalIncome - totalExpenses;
        btnBalance.setText("Balance: " + balance + " Rub.");
        if (balance < 0) {
            btnBalance.setBackgroundColor(getResources().getColor( R.color.colorRed));
        }
    }

    @Override
    public void setTransactionsList(List<Transaction> listIncome, List<Transaction> listExpenses) {
        StringBuilder builder = new StringBuilder();
        for(Transaction t : listIncome ){
               int amount = t.getAmount();
               String category = t.getCategory();
               builder.append("+" +  amount + getString(R.string.rub)+ category + "\n");
            }
          tvListIncome.setText(builder);
        StringBuilder builder2 = new StringBuilder();
        for(Transaction t : listExpenses ){
            int amount = t.getAmount();
            String category = t.getCategory();
            builder2.append("-" + amount + getString(R.string.rub)+ category + "\n");
        }
        tvListExpenses.setText(builder2);
    }
}
