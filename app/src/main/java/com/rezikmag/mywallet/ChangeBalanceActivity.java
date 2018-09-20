package com.rezikmag.mywallet;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rezikmag.mywallet.Database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChangeBalanceActivity extends AppCompatActivity {

    public static final String TRANSACTION_TYPE = "transactionType";
    public static final int ADD_INCOME_BUTTON_CODE =1234;
    public static final int ADD_EXPENSES_BUTTON_CODE =1334;

    EditText mAddAmount;
    TextView mDateTextView;
    Button mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_balance);

        mDateTextView = (TextView) findViewById(R.id.date_textview);
        mAddAmount = (EditText) findViewById(R.id.edit_change_balance);
        final Intent intent = getIntent();
        String date = intent.getStringExtra("showDate");
        final String transactionType = intent.getStringExtra(TRANSACTION_TYPE);

        mDateTextView.setText(date);

        mOkButton = (Button) findViewById(R.id.ok_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backIntent = new Intent();
                backIntent.putExtra("amount", Integer.parseInt(mAddAmount.getText().toString()));
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
    }
}
