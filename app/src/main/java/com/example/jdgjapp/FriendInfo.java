package com.example.jdgjapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jdgjapp.Util.RoundImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class FriendInfo extends AppCompatActivity {
    private RoundImageView photo;
    private TextView manid,name,sex,addr,birth,dept,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        photo=(RoundImageView)findViewById(R.id.friend_photo);
        manid=(TextView)findViewById(R.id.friend_id);
        name=(TextView)findViewById(R.id.friend_name);
        sex=(TextView)findViewById(R.id.friend_sex);
        addr=(TextView)findViewById(R.id.friend_addr);
        birth=(TextView)findViewById(R.id.friend_birth);
        dept=(TextView)findViewById(R.id.friend_dept);
        phone=(TextView)findViewById(R.id.friend_phone);
        final String user_id=getIntent().getStringExtra("user_id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208/JDGJ/BackAppInfoByUserId")
                        .addParams("user_id",user_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, final int id) {
                                Log.d("联系人信息",response);
                                Type type=new TypeToken<List<infobewan>>(){}.getType();
                                final List<infobewan> list=new Gson().fromJson(response,type);
                                final infobewan e=list.get(0);
                                Log.d("联系人信息",e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (TextUtils.isEmpty(e.getUsr_photo())){
                                            Glide.with(MyApplication.getContext())
                                                    .load(R.mipmap.canelshipin)
                                                    .error(R.mipmap.nothing)
                                                    .into(photo);

                                        }else {
                                            Glide.with(MyApplication.getContext())
                                                    .load("http://106.14.145.208"+e.getUsr_photo())
                                                    .error(R.mipmap.nothing)
                                                    .into(photo);
                                        }
                                        if (TextUtils.isEmpty(e.getId())){
                                            manid.setText("工号：暂无");
                                        }else {
                                            manid.setText("工号："+e.getId());
                                        }
                                        if (TextUtils.isEmpty(e.getName())){
                                            name.setText("姓名：暂无");
                                        }else {
                                            name.setText("姓名："+e.getName());
                                        }
                                        if (TextUtils.isEmpty(e.getUsr_sex())){
                                            sex.setText("性别：暂无");
                                        }else {
                                            sex.setText("性别："+e.getUsr_sex());
                                        }
                                        if (TextUtils.isEmpty(e.getUsr_addr())){
                                            addr.setText("住址：暂无");
                                        }else {
                                            addr.setText("住址："+e.getUsr_addr());
                                        }
                                        if (TextUtils.isEmpty(e.getUsr_phone())){
                                            phone.setText("手机号码：暂无");
                                        }else {
                                            phone.setText("手机号码："+e.getUsr_phone());
                                        }
                                        if (TextUtils.isEmpty(e.getUsr_birth())){
                                            birth.setText("出生日期：暂无");
                                        }else {
                                            birth.setText("出生日期："+e.getUsr_birth());
                                        }
                                        if (TextUtils.isEmpty(e.getUsr_dept())){
                                            dept.setText("所属部门：暂无");
                                        }else {
                                            dept.setText("所属部门："+e.getUsr_dept());
                                        }
                                    }
                                });

                            }
                        });
            }
        }).start();
    }
    class infobewan{
        private String id;
        private String name;
        private String usr_sex;
        private String usr_addr;
        private String usr_phone;
        private String usr_birth;
        private String usr_dept;
        private String usr_photo;

        public infobewan() {
        }

        public infobewan(String id, String name, String usr_sex, String usr_addr, String usr_phone, String usr_birth, String usr_dept, String usr_photo) {
            this.id = id;
            this.name = name;
            this.usr_sex = usr_sex;
            this.usr_addr = usr_addr;
            this.usr_phone = usr_phone;
            this.usr_birth = usr_birth;
            this.usr_dept = usr_dept;
            this.usr_photo = usr_photo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsr_sex() {
            return usr_sex;
        }

        public void setUsr_sex(String usr_sex) {
            this.usr_sex = usr_sex;
        }

        public String getUsr_addr() {
            return usr_addr;
        }

        public void setUsr_addr(String usr_addr) {
            this.usr_addr = usr_addr;
        }

        public String getUsr_phone() {
            return usr_phone;
        }

        public void setUsr_phone(String usr_phone) {
            this.usr_phone = usr_phone;
        }

        public String getUsr_birth() {
            return usr_birth;
        }

        public void setUsr_birth(String usr_birth) {
            this.usr_birth = usr_birth;
        }

        public String getUsr_dept() {
            return usr_dept;
        }

        public void setUsr_dept(String usr_dept) {
            this.usr_dept = usr_dept;
        }

        public String getUsr_photo() {
            return usr_photo;
        }

        public void setUsr_photo(String usr_photo) {
            this.usr_photo = usr_photo;
        }

        @Override
        public String toString() {
            return "infobewan{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", usr_sex='" + usr_sex + '\'' +
                    ", usr_addr='" + usr_addr + '\'' +
                    ", usr_phone='" + usr_phone + '\'' +
                    ", usr_birth='" + usr_birth + '\'' +
                    ", usr_dept='" + usr_dept + '\'' +
                    ", usr_photo='" + usr_photo + '\'' +
                    '}';
        }
    }
}
