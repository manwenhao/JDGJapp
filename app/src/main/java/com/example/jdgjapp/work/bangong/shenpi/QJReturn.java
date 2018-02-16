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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class QJReturn extends AppCompatActivity {
    private TextView ok;
    private RadioGroup select;
    private EditText reason;
    private String answer;
    private String ansreason;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qjreturn);
       final  String id=getIntent().getStringExtra("id");
        final String req_id=getIntent().getStringExtra("req_id");
        ok=(TextView)findViewById(R.id.sp_qj_return);
        select=(RadioGroup)findViewById(R.id.bx_qj_select);
        reason=(EditText)findViewById(R.id.sp_qj_return_reason);
        answer="1";
        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.qj_ok){
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
                    Toast.makeText(QJReturn.this, "请输入审批理由", Toast.LENGTH_SHORT).show();
                }else {
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           OkHttpUtils.post()
                                   .url("http://106.14.145.208:8080/JDGJ/ReceiveAppRespToLeaveReq")
                                   .addParams("id",id)
                                   .addParams("user_id", MyApplication.getid())
                                   .addParams("req_id",req_id)
                                   .addParams("answer",answer)
                                   .addParams("answerreason",ansreason)
                                   .build()
                                   .execute(new StringCallback() {
                                       @Override
                                       public void onError(Call call, Exception e, int id) {
                                           e.printStackTrace();
                                       }

                                       @Override
                                       public void onResponse(String response, int id) {
                                           Log.d(QJReturn.class.getName(),response);
                                           if (response.equals("1")){
                                               Toast.makeText(QJReturn.this, "批复成功", Toast.LENGTH_SHORT).show();
                                               ActivityUtils.getInstance().delActivity(QJNo.class.getName());
                                               ActivityUtils.getInstance().delActivity(QJSPDetail.class.getName());
                                               finish();
                                           }else if (response.equals("0")){
                                               Toast.makeText(QJReturn.this, "已被批复", Toast.LENGTH_SHORT).show();
                                               ActivityUtils.getInstance().delActivity(QJNo.class.getName());
                                               ActivityUtils.getInstance().delActivity(QJSPDetail.class.getName());
                                               finish();
                                           }else if (response.equals("2")){
                                               Toast.makeText(QJReturn.this, "无此记录", Toast.LENGTH_SHORT).show();
                                               ActivityUtils.getInstance().delActivity(QJNo.class.getName());
                                               ActivityUtils.getInstance().delActivity(QJSPDetail.class.getName());
                                               finish();
                                           }else if (response.equals("error")){
                                               Toast.makeText(QJReturn.this, "批复失败，请重试", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                       }
                   }).start();
                }
            }
        });

    }
}
