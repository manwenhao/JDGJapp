package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class CarRepair extends AppCompatActivity {
    private ListView listView;
    private AdapterOfCarRepair adapterOfCarRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_repair);
        listView=(ListView)findViewById(R.id.car_repair_listview);
        final String car_id=getIntent().getStringExtra("car_id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:80/JDGJ/BackAppCarInfoById")
                        .addParams("car_id",car_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("车辆保养信息",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("车辆保养信息",response);
                                Type type=new TypeToken<List<com.example.jdgjapp.Bean.CarRepair>>(){}.getType();
                                final List<com.example.jdgjapp.Bean.CarRepair> list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapterOfCarRepair=new AdapterOfCarRepair(MyApplication.getContext(),list);
                                        listView.setAdapter(adapterOfCarRepair);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),DetailOfCarRepair.class);
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
