package com.example.jdgjapp.work.bangong.baoxiao;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jdgjapp.Bean.BaoXiaoPerson;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.cailiao.CaiLiaoMain;
import com.example.jdgjapp.work.bangong.cailiao.MyDeptOfCaiLiao;
import com.example.jdgjapp.work.bangong.gongdan.TransmitList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class BaoXiaoMain extends AppCompatActivity {
    private RelativeLayout apply,mydept;
    private ListView listView;
    private BaoXiaoItemAdapter adapter;
    private List<BaoXiaoPerson> list;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;
    private String bxid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bso_xiao_main);
        apply=(RelativeLayout)findViewById(R.id.bx_main_apply);
        mydept=(RelativeLayout)findViewById(R.id.bx_main_dept);
        listView=(ListView)findViewById(R.id.bx_main_mylist);
        final User user= ReturnUsrDep.returnUsr();
        bxid=getIntent().getStringExtra("newsid");
        mydept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUsr_bossId()==null){
                    startActivity(new Intent(BaoXiaoMain.this,MyDeptOfBx.class));
                }else if (user.getUsr_bossId().equals(MyApplication.bossid)){
                    startActivity(new Intent(BaoXiaoMain.this,MyDeptOfBx.class));
                }
                else
                    {
                    Toast.makeText(BaoXiaoMain.this, "您的权限不足，无法查看部门材料信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BaoXiaoMain.this,BXApply.class));
            }
        });
        intentFilter=new IntentFilter();
        intentFilter.addAction("baoxiaoapply");
        myreceiver=new Myreceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppAcountByUserId")
                        .addParams("user_id",MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("查看个人报销情况",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("查看个人报销情况",response);
                                Type type=new TypeToken<List<BaoXiaoPerson>>(){}.getType();
                                list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bxid!=null){
                                            Intent intent=new Intent(MyApplication.getContext(),BaoXiaoDetail.class);
                                            BaoXiaoPerson b=new BaoXiaoPerson();
                                            for (BaoXiaoPerson e:list){
                                                if (e.getAcc_id().equals(bxid)){
                                                    b=e;
                                                    break;
                                                }
                                            }
                                            intent.putExtra("bean",b);
                                            startActivity(intent);
                                            finish();
                                        }
                                        adapter=new BaoXiaoItemAdapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),BaoXiaoDetail.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myreceiver);
    }

    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackAppAcountByUserId")
                            .addParams("user_id",MyApplication.getid())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("查看个人报销情况",e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("查看个人报销情况",response);
                                    Type type=new TypeToken<List<BaoXiaoPerson>>(){}.getType();
                                    list=new Gson().fromJson(response,type);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.setDate(list);
                                            adapter.notifyDataSetChanged();
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Intent intent=new Intent(MyApplication.getContext(),BaoXiaoDetail.class);
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
}
