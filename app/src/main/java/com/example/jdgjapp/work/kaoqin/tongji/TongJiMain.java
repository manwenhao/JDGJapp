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
import android.widget.Toast;

import com.example.jdgjapp.Bean.BaiduGJInfo;
import com.example.jdgjapp.Bean.TongjiDays;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.kaoqin.qiandao.QianDaoMain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

public class TongJiMain extends AppCompatActivity {

    private static final String TAG = "TongJiMain";
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button dateBtn;
    private Button cqBth;
    private Button ccBtn;
    private Button qjBtn;
    private Button cdBtn;
    private Button ztBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_ji_main);
        dateBtn = (Button) findViewById(R.id.btn_select_date);
        cqBth = (Button) findViewById(R.id.chuqin);
        ccBtn = (Button) findViewById(R.id.chuchai);
        qjBtn = (Button) findViewById(R.id.qingjia);
        cdBtn = (Button) findViewById(R.id.chidao);
        ztBtn = (Button) findViewById(R.id.zaotui);
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
            sendRequest(ReturnUsrDep.returnUsr().getUsr_id(), String.valueOf(mYear), mm);
            dateBtn.setText(String.valueOf(mYear) + "年" + mm + "月");

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

    private void sendRequest(final String userid, final String year, final String month) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .addParams("user_id", userid)
                        .addParams("year", year)
                        .addParams("month", month)
                        .url("http://106.14.145.208:80/JDGJ/BackAppKqCount")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(TAG, "出勤统计获取失败" + e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TongJiMain.this, "请求失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "出勤统计获取成功" + response);
                                parseJSon(response);
                                showDays();
                            }
                        });
            }
        }).start();
    }

    private void parseJSon(final String response) {
        Gson gson = new Gson();
        List<TongjiDays> list = gson.fromJson(response, new TypeToken<List<TongjiDays>>() {
        }.getType());
        DataSupport.deleteAll(TongjiDays.class);
        for (TongjiDays tongjiDays : list) {
            TongjiDays tongjiDays1 = new TongjiDays();
            tongjiDays1.setCq(tongjiDays.getCq());
            tongjiDays1.setCc(tongjiDays.getCc());
            tongjiDays1.setCd(tongjiDays.getCd());
            tongjiDays1.setQj(tongjiDays.getQj());
            tongjiDays1.setZt(tongjiDays.getZt());
            tongjiDays1.save();
        }
    }

    private void showDays() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<TongjiDays> tongjiDays = DataSupport.findAll(TongjiDays.class);
                for (TongjiDays tongjiDay : tongjiDays) {
                    cqBth.setText(tongjiDay.getCq());
                    ccBtn.setText(tongjiDay.getCc());
                    qjBtn.setText(tongjiDay.getQj());
                    cdBtn.setText(tongjiDay.getCd());
                    ztBtn.setText(tongjiDay.getZt());
                }
            }
        });
    }

}
