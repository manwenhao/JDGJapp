package com.example.jdgjapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetBirthActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;

    private int nYear;
    private int nMonth;
    private int nDay;

    private Calendar calendar;
    private TextView brithtv;
    private Button doneBtn;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_birth);

        doneBtn = (Button) findViewById(R.id.btn_done);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDay = calendar.get(Calendar.DAY_OF_MONTH);

        brithtv = (TextView) findViewById(R.id.tv_brith);

        //记住数据
        User user = DataSupport.findFirst(User.class);
        brithtv.setText(user.getUsr_birth());

        setListeners();

    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        doneBtn.setOnClickListener(onClick);
        brithtv.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_brith:
                    showDialog(START_DATE);
                    break;
                case R.id.btn_done:
                    String brith = brithtv.getText().toString();
                    User user = DataSupport.findFirst(User.class);
                    //上传数据
                    sendRequestBrith(user.getUsr_id(),brith);
                    break;
            }

        }
    }

    private void sendRequestBrith(final String id,final String brith) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",id)
                            .add("zdming","usr_birth")
                            .add("value",brith)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:80/JDGJ/ModifyAppInfo")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    //showResponse(responseDate);

                    if (responseDate.equals("ok")){
                        //保存到本地数据库
                        User user = DataSupport.findFirst(User.class);
                        User user0 = new User();
                        user0.setUsr_birth(brith);
                        user0.updateAll("usr_id = ?",user.getUsr_id());
                        Intent intent = new Intent(SetBirthActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                        showResponse("修改生日成功");
                        finish();
                    }else if (responseDate.equals("error")){
                        showResponse("修改生日失败");
                    }else{
                        showResponse("修改生日失败");
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetBirthActivity.this,response,Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }

    private void setDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay =c.get(Calendar.DAY_OF_MONTH);

        nYear = c.get(Calendar.YEAR);
        nMonth = c.get(Calendar.MONTH);
        nDay =c.get(Calendar.DAY_OF_MONTH);
        //更新Button上显示的日期
        updateDateDisplay();
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

    private void updateDateDisplay() {
        brithtv.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }
}
