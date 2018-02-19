package com.example.jdgjapp.work.bangong.shenpi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.work.bangong.cailiao.DatailOfApply;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class CLReturn extends AppCompatActivity {
    private TextView ok;
    private RadioGroup select;
    private EditText reason;
    private String answer;
    private String ansreason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clreturn);
        final String sign=getIntent().getStringExtra("sign");
        final String userid=getIntent().getStringExtra("userid");
        ok=(TextView)findViewById(R.id.sp_cl_return);
        select=(RadioGroup)findViewById(R.id.bx_cl_select);
        reason=(EditText)findViewById(R.id.sp_cl_return_reason);
        answer="1";
        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.cl_ok){
                    answer="1";
                }else {
                    answer="2";
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ansreason=reason.getText().toString();
                if (TextUtils.isEmpty(ansreason)){
                    Toast.makeText(CLReturn.this, "请输入批复理由", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils.post()
                                    .url("http://106.14.145.208:8080/JDGJ/ReceiveAppRespToMaterialReq")
                                    .addParams("sign",sign)
                                    .addParams("req_id",userid)
                                    .addParams("user_id", MyApplication.getid())
                                    .addParams("answer",answer)
                                    .addParams("answerreason",ansreason)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(final String response, int id) {
                                            Log.d(CLReturn.class.getName(),response);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (response.equals("1")){
                                                        Toast.makeText(CLReturn.this, "批复成功", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(CLNo.class.getName());
                                                        ActivityUtils.getInstance().delActivity(CLNOList.class.getName());
                                                        ActivityUtils.getInstance().delActivity(DatailOfApply.class.getName());
                                                        finish();
                                                    }else if (response.equals("0")){
                                                        Toast.makeText(CLReturn.this, "已被批复", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(CLNo.class.getName());
                                                        ActivityUtils.getInstance().delActivity(CLNOList.class.getName());
                                                        ActivityUtils.getInstance().delActivity(DatailOfApply.class.getName());
                                                        finish();
                                                    }else if (response.equals("2")){
                                                        Toast.makeText(CLReturn.this, "无此记录", Toast.LENGTH_SHORT).show();
                                                        ActivityUtils.getInstance().delActivity(CLNo.class.getName());
                                                        ActivityUtils.getInstance().delActivity(CLNOList.class.getName());
                                                        ActivityUtils.getInstance().delActivity(DatailOfApply.class.getName());
                                                        finish();
                                                    }else if (response.equals("error")){
                                                        Toast.makeText(CLReturn.this, "批复失败，请重试", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                        }
                    }).start();
                }
            }
        });
    }
}
