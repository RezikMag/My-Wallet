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
        String a = intent.getStringExtra("showDate");
        mDateTextView.setText(a);

        mOkButton = (Button) findViewById(R.id.ok_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyPagerAdapter adapter = (MyPagerAdapter) MainActivity.pager.getAdapter();

                Calendar calendar = new GregorianCalendar();

                calendar.add(Calendar.DAY_OF_YEAR, MainActivity.pager.getCurrentItem()-
                        adapter.getDayRange() +1);
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                long date = calendar.getTimeInMillis();


                int amount = Integer.parseInt(mAddAmount.getText().toString());

                Transaction transaction = new Transaction(amount,date);
                MainActivity.mDb.transactionDao().insert(transaction);
                Log.d("DB_LOG", "amount: " + transaction.amount +
                "date: "+ transaction.date);

                adapter.notifyDataSetChanged();

                /*
                Intent intent1 = new Intent();
                intent1.putExtra("income", Integer.parseInt(mAddAmount.getText().toString()));
                setResult(RESULT_OK, intent1);
                 */
                finish();
            }
        });
    }
}
