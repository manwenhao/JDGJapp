package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jdgjapp.Bean.CarOfDeptList;
import com.example.jdgjapp.Bean.CarOfDepter;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CarWholeOfDept extends AppCompatActivity {
    private String deptname;
    private String deptid;
    private AdapterOfDepter adapterOfDepter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_whole_of_dept);
        listView=(ListView)findViewById(R.id.car_whole_depter);
        deptname=getIntent().getStringExtra("deptname");
        deptid=getIntent().getStringExtra("deptid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppDeptCar")
                        .addParams("dep_id",deptid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("部门车辆信息",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("部门车辆信息",response);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("deptcar"+deptid,response);
                                Type type=new TypeToken<List<CarOfDepter>>(){}.getType();
                                List<CarOfDepter> datelist=new Gson().fromJson(response,type);
                                final List<CarOfDeptList> list=new ArrayList<CarOfDeptList>();
                                for (CarOfDepter e:datelist){
                                    boolean flag=true;
                                    for (CarOfDeptList a:list){
                                        if (a.getUserid().equals(e.getUsr_id())){
                                            flag=false;
                                            break;
                                        }
                                    }
                                    if (flag){
                                        CarOfDeptList bean=new CarOfDeptList();
                                        bean.setUserid(e.getUsr_id());
                                        bean.setUsername(e.getUsr_name());
                                        list.add(bean);
                                    }

                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapterOfDepter=new AdapterOfDepter(list,MyApplication.getContext());
                                        listView.setAdapter(adapterOfDepter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(), com.example.jdgjapp.work.bangong.cheliang.CarOfDepter.class);
                                                intent.putExtra("flag","deptcar"+deptid);
                                                intent.putExtra("userid",list.get(i).getUserid());
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
