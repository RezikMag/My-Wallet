package com.rezikmag.mywallet.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.rezikmag.mywallet.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChooseDateDialogFragment extends DialogFragment  {

    public interface EditDateListener {
        public void onFinishDialogSetDate(long time);
    }

    static ChooseDateDialogFragment newInstance(long time) {
        ChooseDateDialogFragment fragment = new ChooseDateDialogFragment();
        Bundle args = new Bundle();
        args.putLong("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_calendar, container, false);

        final CalendarView calendarView = v.findViewById(R.id.calendar_view);
        Button chooseDateButton = v.findViewById(R.id.btn_ok);
        long time = getArguments().getLong("time");
        calendarView.setDate(time);

        final long[] date = new long[1];
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = new GregorianCalendar();
                c.set(year, month, dayOfMonth);
               date[0] = c.getTimeInMillis();
            }
        });

        final EditDateListener listener = (EditDateListener) getActivity();

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(date[0]);
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.HOUR,0);
                Log.d("Tag_frag", "Date:" + date[0]);
                listener.onFinishDialogSetDate(calendar.getTimeInMillis());
                dismiss();
            }
        });
        return v;
    }
}