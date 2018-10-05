package com.rezikmag.mywallet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import java.util.Date;

public class ChangeBalanceActivity extends AppCompatActivity implements ChooseDateDialogFragment.EditDateListener {

    public static final String AMOUNT = "amount";
    public static final String CATEGORY = "category";
    public static final String DATE = "showDate";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final int ADD_CODE = 1334;

    EditText mAddAmount;
    TextView mDateButton;
    Button mOkButton;

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_balance);

        mDateButton = findViewById(R.id.btn_date);
        mAddAmount = (EditText) findViewById(R.id.edit_change_balance);

        Intent intent = getIntent();
        time = intent.getLongExtra(DATE,0);
        setDate(time);
        final String transactionType = intent.getStringExtra(TRANSACTION_TYPE);
        final String category = intent.getStringExtra(CATEGORY);

        mOkButton = (Button) findViewById(R.id.ok_button);

        if(category!=null){
            mOkButton.setText("add '" +category + "'");
        }
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(time);
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddAmount.getText().toString().isEmpty()){
                    return;
                }
                String amount = mAddAmount.getText().toString();
                Intent backIntent = new Intent();
                backIntent.putExtra(AMOUNT, Integer.parseInt(amount));
                backIntent.putExtra(DATE, time);
                backIntent.putExtra(TRANSACTION_TYPE,transactionType);
                backIntent.putExtra(CATEGORY,category);
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
    }

     void showDialog(long time) {
        // Create and show the dialog.
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = ChooseDateDialogFragment.newInstance(time);
        newFragment.show(fm, "dialog");
    }


    @Override
    public void onFinishDialogSetDate(long time) {
        setDate(time);
        Log.d("Tag_frag","Time: " + time);
        this.time = time;
    }

    void setDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM");
        String stringDate = format.format(date);
        mDateButton.setText(stringDate);
    }
}
