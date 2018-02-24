package com.example.jdgjapp.work.bangong.baoxiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jdgjapp.Bean.BaoXiaoDepter;
import com.example.jdgjapp.Bean.CarOfDeptList;
import com.example.jdgjapp.Bean.SimplePerson;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.cheliang.AdapterOfDepter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BaoXiaoWholeOfDept extends AppCompatActivity {
    private String deptname;
    private String deptid;
    private ListView listView;
    private AdapterOfDepter adapterOfDepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_xiao_whole_of_dept);
        deptname=getIntent().getStringExtra("deptname");
        deptid=getIntent().getStringExtra("deptid");
        Log.d("部门报销","部门名："+deptname+" 部门id："+deptid);
        listView=(ListView)findViewById(R.id.bx_depter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:80/JDGJ/BackAppDeptAcountById")
                        .addParams("user_id", MyApplication.getid())
                        .addParams("dept_id",deptid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("部门报销",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("部门报销",response);
                                final String flag=deptid+"baoxiao";
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put(flag,response);
                                Type type =new TypeToken<List<BaoXiaoDepter>>(){}.getType();
                                final List<BaoXiaoDepter> list=new Gson().fromJson(response,type);
                                final List<CarOfDeptList> personList=new ArrayList<CarOfDeptList>();
                                for (BaoXiaoDepter e:list){
                                    boolean f=true;
                                    for (CarOfDeptList a:personList){
                                        if (a.getUserid().equals(e.getUser_id())){
                                            f=false;
                                            break;
                                        }
                                    }
                                    if (f){
                                        CarOfDeptList bean=new CarOfDeptList();
                                        bean.setUserid(e.getUser_id());
                                        bean.setUsername(e.getUser_name());
                                        personList.add(bean);
                                    }

                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapterOfDepter=new AdapterOfDepter(personList,MyApplication.getContext());
                                        listView.setAdapter(adapterOfDepter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),DepterBaoXiao.class);
                                                intent.putExtra("flag",flag);
                                                intent.putExtra("user_id",personList.get(i).getUserid());
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
