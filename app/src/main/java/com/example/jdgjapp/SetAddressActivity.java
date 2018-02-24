package com.example.jdgjapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetAddressActivity extends AppCompatActivity {

    private Button doneBtn;
    private EditText addressEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        doneBtn = (Button) findViewById(R.id.btn_done);
        addressEt = (EditText) findViewById(R.id.et_address);

        //记住数据
        User user = DataSupport.findFirst(User.class);
        addressEt.setText(user.getUsr_addr());

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        doneBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_done:
                    String address = addressEt.getText().toString();
                    User user = DataSupport.findFirst(User.class);
                    //上传数据
                    sendRequestAddress(user.getUsr_id(),address);
                    break;
            }
        }
    }

    private void sendRequestAddress(final String id,final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",id)
                            .add("zdming","usr_addr")
                            .add("value",address)
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
                        user0.setUsr_addr(address);
                        user0.updateAll("usr_id = ?",user.getUsr_id());
                        Intent intent = new Intent(SetAddressActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                        showResponse("修改地址成功");
                        finish();
                    }else if (responseDate.equals("error")){
                        showResponse("修改地址失败");
                    }else{
                        showResponse("修改地址失败");
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
                Toast.makeText(SetAddressActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
