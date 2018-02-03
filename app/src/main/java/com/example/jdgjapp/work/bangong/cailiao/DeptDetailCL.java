package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.DeptCaiLiaoDetail;
import com.example.jdgjapp.Bean.PersonCaiLiaoDetail;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeptDetailCL extends AppCompatActivity {
    private String deptname;
    private String use_name;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_detail_cl);
        deptname=getIntent().getStringExtra("deptname");
        use_name=getIntent().getStringExtra("cailiao");
        listView=(ListView)findViewById(R.id.dept_cl_simple);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                String data=aCache.getAsString("deptclp");
                if(data==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DeptDetailCL.this, "您网络状况不佳，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Type type=new TypeToken<List<DeptCaiLiaoDetail>>(){}.getType();
                    List<DeptCaiLiaoDetail> datalist=new Gson().fromJson(data,type);
                    if (use_name==null){
                       final List<PersonCaiLiaoDetail>list=new ArrayList<PersonCaiLiaoDetail>();
                        for (DeptCaiLiaoDetail p:datalist){
                            if (p.getDep_name().equals(deptname)){
                                PersonCaiLiaoDetail caiLiaoDetail=new PersonCaiLiaoDetail();
                                caiLiaoDetail.setUse_name(p.getUse_name());
                                caiLiaoDetail.setUse_num(p.getUse_num());
                                caiLiaoDetail.setUse_cont(p.getUse_cont());
                                caiLiaoDetail.setUse_kind(p.getUse_kind());
                                caiLiaoDetail.setUse_time(p.getUse_time());
                                list.add(caiLiaoDetail);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SeePersonCailiao cailiao=new SeePersonCailiao();
                                SeePersonCailiao.MyAdapter adapter=cailiao.new MyAdapter(MyApplication.getContext(),list);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        PersonCaiLiaoDetail caiLiaoDetail=list.get(i);
                                        if (caiLiaoDetail.getUse_kind().equals("0")){
                                            //日常使用
                                            Intent intent=new Intent(MyApplication.getContext(),PersonDetailCaiLiao.class);
                                            intent.putExtra("person",caiLiaoDetail);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        final List<PersonCaiLiaoDetail>list=new ArrayList<PersonCaiLiaoDetail>();
                        for (DeptCaiLiaoDetail p:datalist){
                            if (p.getDep_name().equals(deptname)&&p.getUse_name().equals(use_name)){
                                PersonCaiLiaoDetail caiLiaoDetail=new PersonCaiLiaoDetail();
                                caiLiaoDetail.setUse_name(p.getUse_name());
                                caiLiaoDetail.setUse_num(p.getUse_num());
                                caiLiaoDetail.setUse_cont(p.getUse_cont());
                                caiLiaoDetail.setUse_kind(p.getUse_kind());
                                caiLiaoDetail.setUse_time(p.getUse_time());
                                list.add(caiLiaoDetail);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SeePersonCailiao cailiao=new SeePersonCailiao();
                                SeePersonCailiao.MyAdapter adapter=cailiao.new MyAdapter(MyApplication.getContext(),list);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        PersonCaiLiaoDetail caiLiaoDetail=list.get(i);
                                        if (caiLiaoDetail.getUse_kind().equals("0")){
                                            //日常使用
                                            Intent intent=new Intent(MyApplication.getContext(),PersonDetailCaiLiao.class);
                                            intent.putExtra("person",caiLiaoDetail);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });

                    }

                }

            }
        }).start();
    }
}
