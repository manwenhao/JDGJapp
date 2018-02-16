package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jdgjapp.Bean.CarOfPerson;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.cailiao.CaiLiaoMain;
import com.example.jdgjapp.work.bangong.cailiao.MyDeptOfCaiLiao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class CheLiangMain extends AppCompatActivity {
    private RelativeLayout mydept;
    private ListView listView;
    private AdapterOfPerson adapterOfPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_che_liang_main);
        mydept=(RelativeLayout)findViewById(R.id.car_mydept);
        listView=(ListView)findViewById(R.id.carofme);
        final User user= ReturnUsrDep.returnUsr();
        mydept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUsr_bossId()==null){
                    startActivity(new Intent(CheLiangMain.this,MydeptOfCar.class));
                }else if (user.getUsr_bossId().equals(MyApplication.bossid)){
                    startActivity(new Intent(CheLiangMain.this,MydeptOfCar.class));
                }
                else {
                    Toast.makeText(CheLiangMain.this, "您的权限不足，无法查看部门材料信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppSelfCar")
                        .addParams("user_id",MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("我的车辆",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("我的车辆",response);
                                Type type=new TypeToken<List<CarOfPerson>>(){}.getType();
                                final List<CarOfPerson> list=new Gson().fromJson(response,type);
                                Log.d("我的车辆",list.toString());
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         adapterOfPerson=new AdapterOfPerson(list,MyApplication.getContext());
                                         listView.setAdapter(adapterOfPerson);
                                         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                 Intent intent=new Intent(MyApplication.getContext(),DetailofCar.class);
                                                 intent.putExtra("bean",list.get(i));
                                                 startActivity(intent);
                                             }
                                         });
                                     }
                                 });
                            }
                        });
            }
        }).start();
    }
}
