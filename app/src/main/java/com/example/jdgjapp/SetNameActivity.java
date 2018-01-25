package com.example.jdgjapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetNameActivity extends AppCompatActivity {

    private Button doneBtn;
    private EditText nameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        doneBtn = (Button) findViewById(R.id.btn_done);
        nameEt = (EditText) findViewById(R.id.et_name);

        //记住数据
        User user = DataSupport.findFirst(User.class);
        nameEt.setText(user.getUsr_name());

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        doneBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.btn_done:
                    String name = nameEt.getText().toString();
                    User user = DataSupport.findFirst(User.class);
                    //上传数据
                    sendRequestName(user.getUsr_id(),name);
                    break;
            }

        }
    }

    private void sendRequestName(final String id,final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",id)
                            .add("zdming","usr_name")
                            .add("value",name)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/JDGJ/ModifyAppInfo")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    if (responseDate.equals("ok")){
                        //保存到本地数据库
                        User user = DataSupport.findFirst(User.class);
                        User user0 = new User();
                        user0.setUsr_name(name);
                        user0.updateAll("usr_id = ?",user.getUsr_id());
                        Intent intent = new Intent(SetNameActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                        showResponse("修改姓名成功");
                        finish();
                    }else if (responseDate.equals("error")){
                        showResponse("修改姓名失败");
                    }else{
                        showResponse("修改姓名失败");
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
                Toast.makeText(SetNameActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
