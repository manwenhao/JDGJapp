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

public class SetPasswordActivity extends AppCompatActivity {

    private Button resetpwBtn;
    private EditText originalEt;
    private EditText newEt;
    private EditText againEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        resetpwBtn = (Button) findViewById(R.id.reset_pw_btn);
        originalEt = (EditText) findViewById(R.id.original_pw_et);
        newEt = (EditText) findViewById(R.id.new_pw_et);
        againEt = (EditText) findViewById(R.id.again_pw_et);

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        resetpwBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.reset_pw_btn:
                    String originalpw = originalEt.getText().toString();
                    String newpw = newEt.getText().toString();
                    String againpw = againEt.getText().toString();

                    User user = DataSupport.findFirst(User.class);
                    String password = user.getUsr_paswprd();

                    if(isEmpty(originalpw) || isEmpty(newpw) || isEmpty(againpw)){
                        showResponse("请填写密码!");
                    }else if (password.equals(originalpw)) {  //本地密码=初始密码
                        if (newpw.equals(againpw)) {    //新密码=再次输入的密码
                            //发送数据
                            sendRequestPassword(user.getUsr_id(),newpw);
                        }else {
                            showResponse("两次输入的密码不一致!");
                        }
                    }else {
                        showResponse("初始密码输入错误!");
                    }
            }
        }
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    private void sendRequestPassword(final String id,final String pw) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",id)
                            .add("zdming","usr_paswprd")
                            .add("value",pw)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/JDGJ/ModifyAppInfo")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    //showResponse(responseDate);

                    if (responseDate.equals("ok")){
                        //保存到本地数据库
                        User user = DataSupport.findFirst(User.class);
                        User user0 = new User();
                        user0.setUsr_paswprd(pw);
                        user0.updateAll("usr_id = ?",user.getUsr_id());
                        Intent intent = new Intent(SetPasswordActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                        showResponse("修改密码成功");
                        finish();
                    }else if (responseDate.equals("error")){
                        showResponse("修改密码失败");
                    }else{
                        showResponse("修改密码失败");
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
                Toast.makeText(SetPasswordActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
