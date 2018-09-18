package com.rezikmag.mywallet;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        String a = intent.getStringExtra("addIncome");
        mDateTextView.setText(a);

        mOkButton = (Button) findViewById(R.id.ok_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amount = Integer.parseInt(mAddAmount.getText().toString());
                ViewPager pager = MainActivity.pager;
                MyPagerAdapter adapter = (MyPagerAdapter) pager.getAdapter();

                adapter.incomeList.set(pager.getCurrentItem(), amount);
                adapter.getItem(pager.getCurrentItem());
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
