package com.example.jdgjapp.work.bangong.baoxiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jdgjapp.Bean.BaoXiaoDepter;
import com.example.jdgjapp.Bean.BaoXiaoPerson;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DepterBaoXiao extends AppCompatActivity {
    private String user_id,flag;
    private ListView listView;
    private BaoXiaoItemAdapter adapter;
    private List<BaoXiaoPerson> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depter_bao_xiao);
        user_id=getIntent().getStringExtra("user_id");
        flag=getIntent().getStringExtra("flag");
        listView=(ListView)findViewById(R.id.bx_depter_list);
        Log.d("====",user_id+flag);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                Type type=new TypeToken<List<BaoXiaoDepter>>(){}.getType();
                List<BaoXiaoDepter> datalist=new Gson().fromJson(aCache.getAsString(flag),type);
                list=new ArrayList<BaoXiaoPerson>();
                for (BaoXiaoDepter e :datalist){
                   if (e.getUser_id().equals(user_id)){
                       BaoXiaoPerson person=new BaoXiaoPerson(e);
                       list.add(person);
                   }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("====",list.toString());
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
        }).start();
    }
}
