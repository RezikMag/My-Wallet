package com.rezikmag.mywallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rezikmag.mywallet.Database.AppDataBase;
import com.rezikmag.mywallet.Database.Transaction;
import com.rezikmag.mywallet.FragmentPresenter;
import com.rezikmag.mywallet.PagerFpagmentContract;
import com.rezikmag.mywallet.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class PageFragment extends Fragment implements PagerFpagmentContract.View {

    static final String ARGUMENT_TIME = "time";
    private static final String ARGUMENT_STATE ="state" ;

    PagerFpagmentContract.Presenter presenter;
    TextView tvTotalIncome;
    TextView tvTotalExpenses;
    TextView tvListIncome;
    TextView tvListExpenses;
    Button btnBalance;
    BottomSheetBehavior bottomSheetBehavior;
    long date;


//тест
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

        presenter = new FragmentPresenter(this, AppDataBase.getInstance(getContext()).transactionDao());

        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior  = BottomSheetBehavior.from(bottomSheet);

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

        initButtons(view);
        presenter.getTransactions(date);
        presenter.getIncomeAndExpenses(date);
        return view;
    }

    //отцепить слушателей
    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetach();
    }

    //полные расходы и доходы
    @Override
    public void setIncomeAndExpenses(int totalIncome, int totalExpenses) {
        tvTotalIncome.setText(totalIncome + " Rub.");
        tvTotalExpenses.setText(totalExpenses + " Rub.");
        int balance = totalIncome - totalExpenses;
        btnBalance.setText("Balance: " + balance + " Rub.");
        if (balance < 0) {
            btnBalance.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }
    }

    //заполнение списка расходов и доходов
    @Override
    public void setTransactionsList(List<Transaction> listIncome, List<Transaction> listExpenses) {
        StringBuilder builder = new StringBuilder();
        for (Transaction t : listIncome) {
            int amount = t.getAmount();
            String category = t.getCategory();
            builder.append("+" + amount + getString(R.string.rub) + category + "\n");
        }
        tvListIncome.setText(builder);

        StringBuilder builder2 = new StringBuilder();
        for (Transaction t : listExpenses) {
            int amount = t.getAmount();
            String category = t.getCategory();
            builder2.append("-" + amount + getString(R.string.rub) + category + "\n");
        }
        tvListExpenses.setText(builder2);
    }

    //запуск активити для заполнения расходов
    private void startForResult(String category) {
        Intent intent = new Intent(getActivity(), ChangeBalanceActivity.class);
        intent.putExtra(ChangeBalanceActivity.DATE, date);
        intent.putExtra(ChangeBalanceActivity.TRANSACTION_TYPE, getString(R.string.expenses));
        intent.putExtra(ChangeBalanceActivity.CATEGORY, category);
        getActivity().startActivityForResult(intent, ChangeBalanceActivity.ADD_CODE);
    }

    // кнопки для добавления расходов
    public void initButtons(View view) {


        ImageButton btnSportExpenses = view.findViewById(R.id.btn_sport);
        btnSportExpenses.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.sport));
            }
        });

        ImageButton btnEatExpenses = view.findViewById(R.id.btn_eat);
        btnEatExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.eat));
            }
        });


        ImageButton btnTransportExpenses = view.findViewById(R.id.btn_transport);
        btnTransportExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.transport));
            }
        });

        ImageButton btnCarExpenses = view.findViewById(R.id.btn_car);
        btnCarExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.car));
            }
        });

        ImageButton btnClothesExpenses = view.findViewById(R.id.btn_clothes);
        btnClothesExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.clothes));
            }
        });

        ImageButton btnHealthExpenses = view.findViewById(R.id.btn_health);
        btnHealthExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.health));
            }
        });

        ImageButton btnHomeExpenses = view.findViewById(R.id.btn_home);
        btnHomeExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.home));
            }
        });

        ImageButton btnPhoneExpenses = view.findViewById(R.id.btn_phone);
        btnPhoneExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.phone));
            }
        });

        ImageButton btnCafeExpenses = view.findViewById(R.id.btn_cafe);
        btnCafeExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.cafe));
            }
        });

        ImageButton btnFunExpenses = view.findViewById(R.id.btn_fun);
        btnFunExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.fun));
            }
        });

        ImageButton btnPetsExpenses = view.findViewById(R.id.btn_pets);
        btnPetsExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.pets));
            }
        });

        ImageButton btnHygieneExpenses = view.findViewById(R.id.btn_hygiene);
        btnHygieneExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(getString(R.string.hygiene));
            }
        });
    }
}
