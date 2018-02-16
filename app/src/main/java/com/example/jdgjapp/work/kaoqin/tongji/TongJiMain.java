package com.example.jdgjapp.work.kaoqin.tongji;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.jdgjapp.R;

import java.util.Calendar;
import java.util.Locale;

public class TongJiMain extends AppCompatActivity {

    private static final String TAG = "TongJiMain";
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button dateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_ji_main);
        dateBtn = (Button) findViewById(R.id.btn_select_date);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
    }


    protected Dialog onCreateDialog(int id) {
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        mMonth = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case 0:
                return new MonPickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            String mm;
            String dd;
            if (monthOfYear <= 9) {
                mMonth = monthOfYear + 1;
                mm = "" + mMonth;
            } else {
                mMonth = monthOfYear + 1;
                mm = String.valueOf(mMonth);
            }
            if (dayOfMonth <= 9) {
                mDay = dayOfMonth;
                dd = "0" + mDay;
            } else {
                mDay = dayOfMonth;
                dd = String.valueOf(mDay);
            }
            mDay = dayOfMonth;
            dateBtn.setText(String.valueOf(mYear) +"年"+ mm+"月");

        }
    };

    public class MonPickerDialog extends DatePickerDialog {
        @SuppressLint("NewApi")
        public MonPickerDialog(Context context, OnDateSetListener callBack,
                               int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            this.setTitle(year + "年" + (monthOfYear + 1) + "月");

            ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
                    .getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            this.setTitle(year + "年" + (month + 1) + "月");
        }
    }


}
