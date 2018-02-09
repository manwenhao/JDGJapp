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

public class CCApplyok extends AppCompatActivity {
    private ListView listView;
    private CCAdapter adapter;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;
    private String newsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccapplyok);
        listView=(ListView)findViewById(R.id.cc_applyok_listview);
        intentFilter=new IntentFilter();
        intentFilter.addAction("ccapplyok");
        myreceiver=new Myreceiver();
        newsid=getIntent().getStringExtra("newsid");
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
                                    if (e.getAnswer()!=null){
                                        if (e.getAnswer().equals("1")){
                                            list.add(e);
                                        }
                                    }
                                }
                                Log.d("同步出差信息",list.toString());
                                String datestring=new Gson().toJson(list,type);
                                final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("ccapplyok",datestring);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (newsid!=null){
                                            CCApplyRes res=new CCApplyRes();
                                            for (CCApplyRes e :list){
                                                if (e.getId().equals(newsid)){
                                                    res=e;
                                                    break;
                                                }
                                            }
                                            Intent intent=new Intent(MyApplication.getContext(),CCApplyDetail.class);
                                            intent.putExtra("bean",res);
                                            startActivity(intent);
                                            //finish();
                                        }
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
        registerReceiver(myreceiver,intentFilter);
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
                                        if (e.getAnswer()!=null){
                                            if (e.getAnswer().equals("1")){
                                                list.add(e);
                                            }
                                        }
                                    }
                                    Log.d("同步出差信息",list.toString());
                                    String datestring=new Gson().toJson(list,type);
                                    final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("ccapplyok",datestring);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setdate(list);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(MyApplication.getContext(), "出差审批结果刷新，你有出差申请通过", Toast.LENGTH_SHORT).show();
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
