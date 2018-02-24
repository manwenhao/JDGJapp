package com.example.jdgjapp.work.bangong.cailiao;

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

import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;

public class ApplyIng extends AppCompatActivity {
    private List<CaiLiaoResponse> list;
    private ListView listView;
    private ResponseWholeAdapter adapter;
    private IntentFilter intentFilter;
    private myreceiver myreceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_ing);
        listView=(ListView)findViewById(R.id.apply_cailiao_ing_listview);
        initdate();
        intentFilter=new IntentFilter();
        intentFilter.addAction("clapplypass");
        intentFilter.addAction("clapplyrefuse");
        myreceiver=new myreceiver();
        registerReceiver(myreceiver,intentFilter);
    }
    public void initdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User u= ReturnUsrDep.returnUsr();
                OkHttpUtils.post()
                        .url("http://106.14.145.208:80/JDGJ/BackDeptLeadMaterRecord")
                        .addParams("dep_id",u.getUsr_deptId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("获取部门申请结果",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("获取部门申请结果",response);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                Type type=new TypeToken<List<CaiLiaoResponse>>(){}.getType();
                                List<CaiLiaoResponse> all=new Gson().fromJson(response,type);
                                Log.d("所有的部门材料申请结果",all.toString());
                                List<CaiLiaoResponse> datelist=new ArrayList<CaiLiaoResponse>();
                                for (CaiLiaoResponse c:all){
                                    if (c.getRmat_status().equals("0")){
                                        datelist.add(c);
                                    }
                                }
                                Log.d("材料申请审核中",datelist.toString());
                                String savedate=new Gson().toJson(datelist,type);
                                aCache.put("clapplying",savedate);
                                List<CaiLiaoResponse> l=new ArrayList<CaiLiaoResponse>();
                                for (CaiLiaoResponse c:datelist){
                                    boolean flag=true;
                                    for (CaiLiaoResponse caiLiaoResponse:l){
                                        if (caiLiaoResponse.getSign().equals(c.getSign())){
                                            flag=false;
                                            break;
                                        }
                                    }
                                    if (flag){
                                        l.add(c);
                                    }
                                }
                                list=new ArrayList<CaiLiaoResponse>();
                                list=l;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         adapter=new ResponseWholeAdapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);
                                        final String flag="clapplying";
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String sign=list.get(i).getSign();
                                                Intent intent=new Intent(MyApplication.getContext(),ListOfOneResponse.class);
                                                intent.putExtra("flag",flag);
                                                intent.putExtra("sign",sign);
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
    class myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User u= ReturnUsrDep.returnUsr();
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:80/JDGJ/BackDeptLeadMaterRecord")
                            .addParams("dep_id",u.getUsr_deptId())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("获取部门申请结果",e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("获取部门申请结果",response);
                                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    Type type=new TypeToken<List<CaiLiaoResponse>>(){}.getType();
                                    List<CaiLiaoResponse> all=new Gson().fromJson(response,type);
                                    Log.d("所有的部门材料申请结果",all.toString());
                                    List<CaiLiaoResponse> datelist=new ArrayList<CaiLiaoResponse>();
                                    for (CaiLiaoResponse c:all){
                                        if (c.getRmat_status().equals("0")){
                                            datelist.add(c);
                                        }
                                    }
                                    Log.d("材料申请审核中",datelist.toString());
                                    String savedate=new Gson().toJson(datelist,type);
                                    aCache.put("clapplying",savedate);
                                    final List<CaiLiaoResponse> l=new ArrayList<CaiLiaoResponse>();
                                    for (CaiLiaoResponse c:datelist){
                                        boolean flag=true;
                                        for (CaiLiaoResponse caiLiaoResponse:l){
                                            if (caiLiaoResponse.getSign().equals(c.getSign())){
                                                flag=false;
                                                break;
                                            }
                                        }
                                        if (flag){
                                            l.add(c);
                                        }
                                    }
                                    list=l;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setdata(list);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(MyApplication.getContext(), "材料申请审批结果刷新", Toast.LENGTH_SHORT).show();
                                            final String flag="clapplying";
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    String sign=list.get(i).getSign();
                                                    Intent intent=new Intent(MyApplication.getContext(),ListOfOneResponse.class);
                                                    intent.putExtra("flag",flag);
                                                    intent.putExtra("sign",sign);
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
