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

public class ApplyRefuse extends AppCompatActivity {
    private List<CaiLiaoResponse> list;
    private ListView listView;
    private ResponseWholeAdapter adapter;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;
    private String newsid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refuse);
        listView=(ListView)findViewById(R.id.apply_cailiao_refuse_listview);
        newsid=getIntent().getStringExtra("newsid");
        initdate();
        intentFilter=new IntentFilter();
        intentFilter.addAction("clapplyrefuse");
        myreceiver=new Myreceiver();
        registerReceiver(myreceiver,intentFilter);
    }
    public void initdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User u= ReturnUsrDep.returnUsr();
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackDeptLeadMaterRecord")
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
                                Type type=new TypeToken<List<CaiLiaoResponse>>(){}.getType();
                                List<CaiLiaoResponse> all=new Gson().fromJson(response,type);
                                Log.d("所有的部门材料申请结果",all.toString());
                                List<CaiLiaoResponse> datelist=new ArrayList<CaiLiaoResponse>();
                                for (CaiLiaoResponse c:all){
                                    if (c.getRmat_status().equals("2")){
                                        datelist.add(c);
                                    }
                                }
                                Log.d("不通过申请结果",datelist.toString());
                                String savedate=new Gson().toJson(datelist,type);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("clapplyrefuse",savedate);
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
                                        final String flag="clapplyrefuse";
                                        if (newsid!=null){
                                            Intent intent=new Intent(MyApplication.getContext(),ListOfOneResponse.class);
                                            intent.putExtra("flag",flag);
                                            intent.putExtra("sign",newsid);
                                            startActivity(intent);
                                           // finish();
                                        }
                                         adapter=new ResponseWholeAdapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);

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
    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User u= ReturnUsrDep.returnUsr();
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackDeptLeadMaterRecord")
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
                                    Type type=new TypeToken<List<CaiLiaoResponse>>(){}.getType();
                                    List<CaiLiaoResponse> all=new Gson().fromJson(response,type);
                                    Log.d("所有的部门材料申请结果",all.toString());
                                    List<CaiLiaoResponse> datelist=new ArrayList<CaiLiaoResponse>();
                                    for (CaiLiaoResponse c:all){
                                        if (c.getRmat_status().equals("2")){
                                            datelist.add(c);
                                        }
                                    }
                                    Log.d("不通过申请结果",datelist.toString());
                                    String savedate=new Gson().toJson(datelist,type);
                                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("clapplyrefuse",savedate);
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
                                            adapter.setdata(l);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(MyApplication.getContext(), "材料申请审批结果刷新，您有申请未通过审批", Toast.LENGTH_SHORT).show();
                                            final String flag="clapplyrefuse";
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
