package com.example.jdgjapp.work.kaoqin.chuchai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.CCApplyRes;
import com.example.jdgjapp.Bean.QJApplyRes;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.work.kaoqin.qingjia.QJAdapter;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CCApplying extends AppCompatActivity {
    private ListView listView;
    private CCAdapter adapter;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccapplying);
        listView=(ListView)findViewById(R.id.cc_applying_listview);
        intentFilter=new IntentFilter();
        intentFilter.addAction("ccapplyok");
        intentFilter.addAction("ccapplyrefuse");
        myreceiver=new Myreceiver();
        registerReceiver(myreceiver,intentFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/TongBuTravelReq")
                        .addParams("user_id", MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("同步出差信息",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("同步出差信息",response);
                                Type type=new TypeToken<List<CCApplyRes>>(){}.getType();
                                List<CCApplyRes>datelist=new Gson().fromJson(response,type);
                                final List<CCApplyRes> list=new ArrayList<CCApplyRes>();
                                for (CCApplyRes e:datelist){
                                    if (e.getAnswer()==null){
                                        list.add(e);
                                    }
                                }
                                Log.d("同步出差信息",list.toString());
                                String datestring=new Gson().toJson(list,type);
                                final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("ccapplying",datestring);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter=new CCAdapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),CCApplyDetail.class);
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
    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/TongBuTravelReq")
                            .addParams("user_id", MyApplication.getid())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("同步出差信息",e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("同步出差信息",response);
                                    Type type=new TypeToken<List<CCApplyRes>>(){}.getType();
                                    List<CCApplyRes>datelist=new Gson().fromJson(response,type);
                                    final List<CCApplyRes> list=new ArrayList<CCApplyRes>();
                                    for (CCApplyRes e:datelist){
                                        if (e.getAnswer()==null){
                                            list.add(e);
                                        }
                                    }
                                    Log.d("同步出差信息",list.toString());
                                    String datestring=new Gson().toJson(list,type);
                                    final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("ccapplying",datestring);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setdate(list);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(MyApplication.getContext(), "出差申请结果刷新", Toast.LENGTH_SHORT).show();
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Intent intent=new Intent(MyApplication.getContext(),CCApplyDetail.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myreceiver);
    }
}