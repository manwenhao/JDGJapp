package com.example.jdgjapp.work.bangong.shenpi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.regex.Pattern;

import okhttp3.Call;

public class BXSPReturn extends AppCompatActivity {
    private TextView ok;
    private EditText money,reason;
    private RadioGroup radioGroup;
    private String answer;
    private String answerreason;
    private String backmoney;
    private RadioButton okb,nob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxspreturn);
        final String acc_id=getIntent().getStringExtra("acc_id");
        final String acc_userid=getIntent().getStringExtra("acc_userid");
        ok=(TextView)findViewById(R.id.sp_bx_return);
        okb=(RadioButton)findViewById(R.id.bx_ok);
        nob=(RadioButton)findViewById(R.id.bx_no);
        money=(EditText) findViewById(R.id.sp_bx_return_backmoney);
        reason=(EditText)findViewById(R.id.sp_bx_return_reason);
        radioGroup=(RadioGroup)findViewById(R.id.bx_sp_select);
        answer="1";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.bx_ok){
                    answer="1";
                    Log.d("======",answer);
                    money.setFocusable(true);
                    money.setFocusableInTouchMode(true);
                }else {
                    answer="2";
                    Log.d("======",answer);
                    backmoney="";
                    money.setText("");
                    money.setFocusable(false);
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerreason=reason.getText().toString();
                backmoney=money.getText().toString();
                if (TextUtils.isEmpty(backmoney)||TextUtils.isEmpty(answerreason)){
                    Toast.makeText(BXSPReturn.this, "请完善审批信息", Toast.LENGTH_SHORT).show();
                }else if (!isNUmber(backmoney)){
                    Toast.makeText(BXSPReturn.this, "金额请输入数字", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("======",answer);
                            Log.d("======",answerreason);
                            Log.d("======",backmoney);
                            OkHttpUtils.post()
                                    .url("http://106.14.145.208:80/JDGJ/ReceiveAppRsepToAccoutReq")
                                    .addParams("acc_id",acc_id)
                                    .addParams("req_id",acc_userid)
                                    .addParams("user_id", MyApplication.getid())
                                    .addParams("acc_answer",answer)
                                    .addParams("acc_answerreason",answerreason)
                                    .addParams("backmoney",backmoney)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Log.d(BXSPReturn.class.getName(),e.toString());
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Log.d(BXSPReturn.class.getName(),response);
                                            if (response.equals("1")){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(BXSPReturn.this, "批复成功", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(BXDetail.class.getName());
                                                        ActivityUtils.getInstance().delActivity(BXNo.class.getName());
                                                        finish();
                                                    }
                                                });
                                            }else if (response.equals("0")){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(BXSPReturn.this, "该请求已被批复", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(BXDetail.class.getName());
                                                        ActivityUtils.getInstance().delActivity(BXNo.class.getName());
                                                        finish();
                                                    }
                                                });
                                            }else if (response.equals("2")){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(BXSPReturn.this, "无此记录", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(BXDetail.class.getName());
                                                        ActivityUtils.getInstance().delActivity(BXNo.class.getName());
                                                        finish();
                                                    }
                                                });
                                            }else if (response.equals("error")){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(BXSPReturn.this, "批复失败，请重试", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }).start();
                }
            }
        });

    }
    public boolean isNUmber(String str){
        Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
