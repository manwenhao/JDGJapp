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

public class ApplyPass extends AppCompatActivity {
    private List<CaiLiaoResponse> list;
    private ListView listView;
    private ResponseWholeAdapter adapter;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;
    private String newsid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_pass);
        listView=(ListView)findViewById(R.id.apply_cailiao_pass_listview);
        newsid=getIntent().getStringExtra("newsid");
        intentFilter=new IntentFilter();
        intentFilter.addAction("clapplypass");
        myreceiver=new Myreceiver();
        initdate();
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
                                    if (c.getRmat_status().equals("1")){
                                        datelist.add(c);
                                    }
                                }
                                Log.d("通过申请结果",datelist.toString());
                                String savedate=new Gson().toJson(datelist,type);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("clapplypass",savedate);
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
                                        final String flag="clapplypass";
                                        String newsisget="0";
                                        if (newsid!=null){
                                            for (CaiLiaoResponse e:list){
                                                if (e.getSign().equals(newsid)){
                                                    newsisget=e.getLeadstatus();
                                                    break;

                                                }
                                            }
                                            Intent intent=new Intent(MyApplication.getContext(),ListOfOneResponse.class);
                                            intent.putExtra("flag",flag);
                                            intent.putExtra("sign",newsid);
                                            intent.putExtra("isget",newsisget);
                                            startActivity(intent);
                                            //finish();
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
                                                intent.putExtra("isget",list.get(i).getLeadstatus());
                                                startActivityForResult(intent,1);
                                            }
                                        });
                                    }
                                });

                            }
                        });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    String sign=data.getStringExtra("sign");
                    boolean isget=data.getBooleanExtra("isget",false);
                    Log.d("领料返回","sign: "+sign+" isget: "+isget);
                   for (int i=0;i<list.size();i++){
                       if (list.get(i).getSign().equals(sign)){
                           if (isget){
                               CaiLiaoResponse caiLiaoResponse=list.get(i);
                               list.remove(i);
                               caiLiaoResponse.setLeadstatus("1");
                               list.add(i,caiLiaoResponse);

                           }
                       }

                   }
                   adapter.setdata(list);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("广播接收器","通过审核刷新");
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
                                        if (c.getRmat_status().equals("1")){
                                            datelist.add(c);
                                        }
                                    }
                                    Log.d("通过申请结果",datelist.toString());
                                    String savedate=new Gson().toJson(datelist,type);
                                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("clapplypass",savedate);
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
                                            Toast.makeText(MyApplication.getContext(), "材料申请审批结果刷新，您有申请通过审批", Toast.LENGTH_SHORT).show();
                                            final String flag="clapplypass";
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    String sign=list.get(i).getSign();
                                                    Intent intent=new Intent(MyApplication.getContext(),ListOfOneResponse.class);
                                                    intent.putExtra("flag",flag);
                                                    intent.putExtra("sign",sign);
                                                    intent.putExtra("isget",list.get(i).getLeadstatus());
                                                    startActivityForResult(intent,1);
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
