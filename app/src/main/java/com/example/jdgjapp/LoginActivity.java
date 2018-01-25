package com.example.jdgjapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText useridEdit;
    private EditText passwordEdit;
    private Button login;
    private Button forgetpw;
    private CheckBox rememberPass;

    public static String isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        useridEdit = (EditText) findViewById(R.id.userid);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pw);
        forgetpw = (Button) findViewById(R.id.btn_forget_pw);
        login = (Button) findViewById(R.id.login);

        //记住密码
        SharedPreferences pref = getSharedPreferences("rememberpwd",MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            User user = DataSupport.findFirst(User.class);
            useridEdit.setText(user.getUsr_id());
            passwordEdit.setText(user.getUsr_paswprd());
            rememberPass.setChecked(true);
        }
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        login.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.login:    //点击登录按钮
                    String userid = useridEdit.getText().toString().trim();
                    String password = passwordEdit.getText().toString().trim();
                    //发送请求
                    sendRequestWithOkHttp(userid,password);

                    //记住密码
                    SharedPreferences.Editor editor = getSharedPreferences("rememberpwd",
                            MODE_PRIVATE).edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password",true);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    break;
            }

        }
    }

    //发送请求获取个人信息
    private void sendRequestWithOkHttp(final String id, final String pw) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name",id)
                            .add("password",pw)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/JDGJ/JudgeAppLogin")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    //解析数据
                    parseJSON(responseDate);

                    if (isSuccess.equals("1")){
                        showResponse("登录成功");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (isSuccess.equals("0")){
                        showResponse("登录失败");
                    }else{
                        showResponse("登录失败");
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
                Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseJSON(String jsonData){
        try{
            //截取isSecuss字段并解析
            int index = jsonData.indexOf("}");    //第一个}的位置
            String isSuces = jsonData.substring(0,index+1);
            String isSucss = isSuces.concat("]");
            JSONArray jsonArray = new JSONArray(isSucss);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            isSuccess = jsonObject.getString("isSecuss");
            Log.d(TAG, "@isSuccess is " + isSuccess);
            if (isSuccess.equals("1")){    //登录成功
                //截取user字段并解析
                int index2 = jsonData.indexOf("}", index+1); //第二个}的位置
                String usrJson = jsonData.substring(index+2,index2+1);
                String left = "[";
                String userJson = left.concat(usrJson).concat("]");
                Log.d(TAG, ""+userJson);
                Gson gson = new Gson();
                List<User> userList = gson.fromJson(userJson, new TypeToken<List<User>>(){}.getType());
                for (User user : userList){
                    List<User> users = DataSupport.where("usr_id = ?",user.getUsr_id()).find(User.class);
                    if (users == null || users.size()==0){ //本地没有则添加
                        User user1 = new User();
                        user1.setUsr_id(user.getUsr_id());
                        user1.setUsr_name(user.getUsr_name());
                        user1.setUsr_paswprd(user.getUsr_paswprd());
                        user1.setUsr_sex(user.getUsr_sex());
                        user1.setUsr_addr(user.getUsr_addr());
                        user1.setUsr_phone(user.getUsr_phone());
                        user1.setUsr_birth(user.getUsr_birth());
                        user1.setUsr_deptId(user.getUsr_deptId());
                        user1.setUsr_bossId(user.getUsr_bossId());
                        user1.save();
                    }
                    MyApplication.setid(user.getUsr_id());
                    Log.d(TAG, "usr_id is " + user.getUsr_id());
                    Log.d(TAG, "usr_name is " + user.getUsr_name());
                    Log.d(TAG, "usr_paswprd is " + user.getUsr_paswprd());
                    Log.d(TAG, "usr_sex is " + user.getUsr_sex());
                    Log.d(TAG, "usr_addr is " + user.getUsr_addr());
                    Log.d(TAG, "usr_phone is " + user.getUsr_phone());
                    Log.d(TAG, "usr_birth is " + user.getUsr_birth());
                    Log.d(TAG, "usr_deptId is " + user.getUsr_deptId());
                    Log.d(TAG, "usr_bossId is " + user.getUsr_bossId());
                    Log.d(TAG, "------------------------------------");
                }

                //截取depart字段并解析
                index = jsonData.indexOf("}", index+1); //第二个}的位置
                String depatJson = jsonData.substring(index+2);
                String departJson = "[".concat(depatJson);
                Gson gson1 = new Gson();
                List<Depart> departList = gson1.fromJson(departJson,new TypeToken<List<Depart>>(){}.getType());
                for (Depart depart : departList){
                    List<Depart> departs = DataSupport.where("dep_id = ?",depart.getDep_id()).find(Depart.class);
                    if(departs == null || departs.size()==0){    //本地没有则添加
                        Depart depart1 = new Depart();
                        depart1.setDep_id(depart.getDep_id());
                        depart1.setDep_name(depart.getDep_name());
                        depart1.setDep_manager(depart.getDep_manager());
                        depart1.save();
                    }
                    Log.d(TAG, "dep_id is " + depart.getDep_id());
                    Log.d(TAG, "dep_name is " + depart.getDep_name());
                    Log.d(TAG, "dep_manager is " + depart.getDep_manager());
                    Log.d(TAG, "********************************");
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
