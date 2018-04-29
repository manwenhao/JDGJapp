package com.example.jdgjapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.Leave;
import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.Bean.Travel;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.kaoqin.daka.DaKaMain;
import com.example.jdgjapp.work.kaoqin.daka.DingWeiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
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
            if (TextUtils.isEmpty(ReturnUsrDep.returnUsr().getUsr_id())){
                SharedPreferences.Editor editor = getSharedPreferences("rememberpwd",
                        MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                rememberPass.setChecked(false);
            }else {
                useridEdit.setText(ReturnUsrDep.returnUsr().getUsr_id());
                passwordEdit.setText(ReturnUsrDep.returnUsr().getUsr_paswprd());
                rememberPass.setChecked(true);
            }

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

                   final  Request request = new Request.Builder()
                            .url("http://106.14.145.208:80/JDGJ/JudgeAppLogin")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();
                    Log.d(TAG, "logindata"+responseDate);

                    //解析数据
                    parseJSON(responseDate);

                    if (isSuccess.equals("1")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpUtils.post()
                                        .url("http://106.14.145.208:80/JDGJ/GetBackBossInfo")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                if (request.equals("error")){
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(LoginActivity.this, "信息获取出现意外错误，请退出重试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else {
                                                    MyApplication.bossid=response;
                                                }
                                            }
                                        });
                            }
                        }).start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpUtils.post()
                                        .addParams("user_id", MyApplication.getid())
                                        .url("http://106.14.145.208:80/JDGJ/BackAppFriend")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                Log.d("好友列表Error",e.toString());
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                Log.d("好友列表",response);
                                                ACache aCache= ACache.get(MyApplication.getContext(), MyApplication.getid());
                                                aCache.put("friends",response);
                                            }
                                        });
                            }
                        }).start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                User u= ReturnUsrDep.returnUsr();
                                OkHttpUtils.post()
                                        .url("http://106.14.145.208:80/JDGJ/BackMangrUsrInfo")
                                        .addParams("user_id",u.getUsr_id())
                                        .addParams("dept_id",u.getUsr_deptId())
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                Log.d("查看我的部门错误",e.toString());
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                Log.d("我的部门成员",response);
                                                ACache aCache= ACache.get(MyApplication.getContext(), MyApplication.getid());
                                                aCache.put("depterlist",response);
                                            }
                                        });
                            }
                        }).start();

                        showResponse("登录成功");

                        //同步数据
                        TongBuTaskRequest(MyApplication.getid());

                        //仍在上班期间则重新打开实时定位
                        SharedPreferences pref = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
                        Integer flag  = pref.getInt("dkflag",0);
                        if (flag == 1){
                            //开启实时定位服务
                            Intent startIntent = new Intent(LoginActivity.this,DingWeiService.class);
                            startService(startIntent);
                            Log.d(TAG, "实时定位重新开启");
                        }

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
                Log.d(TAG, "userJson"+userJson);
                Gson gson = new Gson();
                List<User> userList = gson.fromJson(userJson, new TypeToken<List<User>>(){}.getType());
                DataSupport.deleteAll(User.class);
                for (User user : userList){
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
                    String addr = "http://106.14.145.208:80";
                    String touxiang = addr + user.getTouxiang();
                    Log.d(TAG, "usrtouxiang"+touxiang);
                    user1.setTouxiang(touxiang);
                    user1.save();

                    JPushInterface.setAlias(getApplicationContext(),1,user.getUsr_id());

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
                    Log.d(TAG, "usr_touxiang is " + user.getTouxiang());
                    Log.d(TAG, "------------------------------------");
                }

                //截取depart字段并解析
                index = jsonData.indexOf("}", index+1); //第二个}的位置
                String depatJson = jsonData.substring(index+2);
                String departJson = "[".concat(depatJson);
                Gson gson1 = new Gson();
                List<Depart> departList = gson1.fromJson(departJson,new TypeToken<List<Depart>>(){}.getType());
                DataSupport.deleteAll(Depart.class);
                for (Depart depart : departList){
                    Depart depart1 = new Depart();
                    depart1.setDep_id(depart.getDep_id());
                    depart1.setDep_name(depart.getDep_name());
                    depart1.setDep_manager(depart.getDep_manager());
                    depart1.save();

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

    //发送请求匹配所有工单
    private void TongBuTaskRequest(final String userid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id",userid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:80/JDGJ/TongBuAppUserOrder")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    TongBuTaskparseJSON(responseData);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d(TAG, "已发送");
    }

    private void TongBuTaskparseJSON(String jsonData){
        Gson gson = new Gson();
        List<Task> taskList = gson.fromJson(jsonData, new TypeToken<List<Task>>(){}.getType());
        if (taskList!=null && !taskList.isEmpty()) {    //如果有工单数据
            for (Task task : taskList) {
                Log.d(TAG, "工单taskid is     " + task.getTaskid());
                Log.d(TAG, "工单sender is     " + task.getSender());
                Log.d(TAG, "工单createtime is " + task.getCreatetime());
                Log.d(TAG, "工单starttime is  " + task.getStartime());
                Log.d(TAG, "工单addr is       " + task.getAddr());
                Log.d(TAG, "工单content is    " + task.getContent());
                Log.d(TAG, "工单cycle is      " + task.getCycle());
                Log.d(TAG, "工单status is     " + task.getStatus());
                Log.d(TAG, "工单**************************************");

                //查询本地数据库，如果没有则添加
                List<Task> tasks = DataSupport.where("taskid = ?", task.getTaskid()).find(Task.class);
                if (tasks == null || tasks.isEmpty()) {
                    Task task1 = new Task();
                    task1.setTaskid(task.getTaskid());
                    task1.setSender(task.getSender());
                    task1.setCreatetime(task.getCreatetime());
                    task1.setStartime(task.getStartime());
                    task1.setAddr(task.getAddr());
                    task1.setContent(task.getContent());
                    task1.setCycle(task.getCycle());
                    task1.setStatus(task.getStatus());
                    task1.save();
                    Log.d(TAG, "已添加工单" + task.getTaskid());
                }

                if (tasks.size()==1){ //如果有，更新状态status
                    Task task0 = new Task();
                    task0.setStatus(task.getStatus());
                    task0.updateAll("taskid = ?",task.getTaskid());
                }

                if (tasks.size()>=2){ //如果有多条，删除本地有重复的工单并重新添加
                    DataSupport.deleteAll(Task.class,"taskid = ?",task.getTaskid());
                    Task task1 = new Task();
                    task1.setTaskid(task.getTaskid());
                    task1.setSender(task.getSender());
                    task1.setCreatetime(task.getCreatetime());
                    task1.setStartime(task.getStartime());
                    task1.setAddr(task.getAddr());
                    task1.setContent(task.getContent());
                    task1.setCycle(task.getCycle());
                    task1.setStatus(task.getStatus());
                    task1.save();
                    int size = tasks.size()-1;
                    Log.d(TAG, "已删除多余工单" + task.getTaskid()+"共"+size+"个");
                }
            }

            //将本地数据与接收的数据对比，多的删除
            List<Task> bdtask = DataSupport.findAll(Task.class);
            for (Task task : bdtask) {
                int num = 0;
                for (Task task2 : taskList) {
                    if (task2.getTaskid().equals(task.getTaskid())) num++;
                }
                if (num == 0) { //如果没有
                    DataSupport.deleteAll(Task.class, "taskid = ?", task.getTaskid());
                    Log.d(TAG, "已删除工单" + task.getTaskid());
                }
            }

        } else {    //如果没有则删除本地所有工单数据
            Log.d(TAG, "没有工单！");
            DataSupport.deleteAll(Task.class);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);

        }
    }
}
