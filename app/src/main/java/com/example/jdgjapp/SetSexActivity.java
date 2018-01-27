package com.example.jdgjapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetSexActivity extends AppCompatActivity {

    private Button doneBtn;
    private RadioGroup sexRg;
    private RadioButton sexRbtn1;
    private RadioButton sexRbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sex);

        doneBtn = (Button) findViewById(R.id.btn_done);
        sexRg = (RadioGroup) findViewById(R.id.rg_sex);
        sexRbtn1 = (RadioButton) findViewById(R.id.rb_male);
        sexRbtn2 = (RadioButton) findViewById(R.id.rb_female);

        //保存数据
        User user = DataSupport.findFirst(User.class);
        if(user.getUsr_sex().equals("男")){
            sexRbtn1.setChecked(true);
        }else {
            sexRbtn2.setChecked(true);
        }

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
                    RadioButton rb = (RadioButton)SetSexActivity.this.findViewById(sexRg.getCheckedRadioButtonId());
                    String sex = rb.getText().toString();
                    User user = DataSupport.findFirst(User.class);
                    //上传数据
                    sendRequestSex(user.getUsr_id(),sex);
                    break;
            }
        }
    }

    private void sendRequestSex(final String id,final String sex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",id)
                            .add("zdming","usr_sex")
                            .add("value",sex)
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
                        user0.setUsr_sex(sex);
                        user0.updateAll("usr_id = ?",user.getUsr_id());
                        Intent intent = new Intent(SetSexActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                        showResponse("修改性别成功");
                        finish();
                    }else if (responseDate.equals("error")){
                        showResponse("修改性别失败");
                    }else{
                        showResponse("修改性别失败");
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
                Toast.makeText(SetSexActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
