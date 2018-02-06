package com.example.jdgjapp.work.kaoqin.chuchai;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApply;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;

public class CCApply extends AppCompatActivity {
    private EditText start,end,reason;
    private Button ok;
    private Spinner spinner;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;
    private int nYear;
    private int nMonth;
    private int nDay;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int START_DATE2 = 3;
    private static final int END_DATE2 = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccapply);
        start=(EditText)findViewById(R.id.cc_start_day);
        end=(EditText)findViewById(R.id.cc_end_day);
        reason=(EditText)findViewById(R.id.cc_reason);
        ok=(Button)findViewById(R.id.cc_apply_start);
        spinner=(Spinner)findViewById(R.id.cc_apply_type);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDay = calendar.get(Calendar.DAY_OF_MONTH);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(START_DATE);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(START_DATE2);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String starttime=start.getText().toString();
                final String endtime=end.getText().toString();
                final String reasonstring=reason.getText().toString();
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sfd=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                final String typestring=spinner.getSelectedItem().toString();
                final String current=sf.format(new Date());
                if (!starttime.equals("")&&!endtime.equals("")&&!reasonstring.equals("")&&typestring!=null){
                    try {
                        if (sf.parse(starttime).getTime()>=sf.parse(current).getTime()&&sf.parse(endtime).getTime()>=sf.parse(starttime).getTime()){

                            final String currentdt=sfd.format(new Date());
                            final String id="cc"+ MyApplication.getid()+currentdt;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    OkHttpUtils.post()
                                            .url("http://106.14.145.208:8080/JDGJ/ReceiveAppTravelReq")
                                            .addParams("id",id)
                                            .addParams("user_id",MyApplication.getid())
                                            .addParams("startDate",starttime)
                                            .addParams("endDate",endtime)
                                            .addParams("reason",reasonstring)
                                            .addParams("type",typestring)
                                            .addParams("sendtime",current)
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Call call, Exception e, int id) {
                                                    Log.d("出差返回",e.toString());
                                                }

                                                @Override
                                                public void onResponse(String response, int id) {
                                                    Log.d("出差返回",response);
                                                    if (response.equals("ok")){
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(CCApply.this, "出差申请成功", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });
                                                    }else {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(CCApply.this, "出差申请失败,请重试", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }).start();
                        }else {
                            Toast.makeText(CCApply.this, "请选择正确的日期", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(CCApply.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    //日期选择函数
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case END_DATE:
                return new DatePickerDialog(this, nDateSetListener, nYear, nMonth,
                        nDay);
            case START_DATE2:
                return new DatePickerDialog(this, mDateSetListener2, mYear, mMonth,
                        mDay);
            case END_DATE2:
                return new DatePickerDialog(this, nDateSetListener2, nYear, nMonth,
                        nDay);
        }
        return null;
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_DATE:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case END_DATE:
                ((DatePickerDialog) dialog).updateDate(nYear, nMonth, nDay);
                break;
            case START_DATE2:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case END_DATE2:
                ((DatePickerDialog) dialog).updateDate(nYear, nMonth, nDay);
                break;
            default:
                break;
        }
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay();
        }
    };
    private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay2();
        }
    };
    private DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            nYear = year;
            nMonth = monthOfYear;
            nDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay();
        }
    };
    private DatePickerDialog.OnDateSetListener nDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            nYear = year;
            nMonth = monthOfYear;
            nDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay2();
        }
    };
    private void updateDateDisplay() {
        start.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }
    private void updateDateDisplay2() {
        end.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }
}
