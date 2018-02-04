package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.DeptWholeCaiLiao;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CaiLiaoWholeOfDept extends AppCompatActivity {
    private String deptname;
    private String deptid;
    private ListView listView;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_liao_whole_of_dept);
        listView=(ListView)findViewById(R.id.WholeCLOfDept);
        start=(Button)findViewById(R.id.dept_cailiao_detail);
        deptname=getIntent().getStringExtra("deptname");
        deptid=getIntent().getStringExtra("deptid");
        Log.d("部门材料概况","deptname: "+deptname+" deptid: "+deptid);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoWholeOfDept.this, DeptDetailCL.class).putExtra("deptname",deptname));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                String data=aCache.getAsString("deptclw");
                if (data==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CaiLiaoWholeOfDept.this, "您网络情况不佳，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Type type=new TypeToken<List<DeptWholeCaiLiao>>(){}.getType();
                    List<DeptWholeCaiLiao> datalist=new Gson().fromJson(data,type);
                    final List<DeptWholeCaiLiao> list=new ArrayList<DeptWholeCaiLiao>();
                    for (DeptWholeCaiLiao caiLiao:datalist){
                        if (caiLiao.getDep_name().equals(deptname)){
                            list.add(caiLiao);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyAdapter adapter=new MyAdapter(MyApplication.getContext(),list);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent=new Intent(MyApplication.getContext(),DeptDetailCL.class);
                                    intent.putExtra("deptname",deptname);
                                    intent.putExtra("cailiao",list.get(i).getUse_name());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        }).start();




    }
    class MyAdapter extends BaseAdapter{
        private List<DeptWholeCaiLiao>list;
        private LayoutInflater inflater;

        public MyAdapter(Context context,List<DeptWholeCaiLiao> list) {
            this.list=list;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;
            if (view==null){
                vh=new ViewHolder();
                view=inflater.inflate(R.layout.cl_wholeofdept_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.cl_wholeofdept_item_name);
                vh.used=(TextView)view.findViewById(R.id.cl_wholeofdept_item_used);
                vh.bar=(HorizontalProgressBarWithNumber)view.findViewById(R.id.cl_wholeofdept_item_bar);
                vh.left=(TextView)view.findViewById(R.id.cl_wholeofdept_item_left);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.name.setText("材料名:"+list.get(i).getUse_name());
            vh.used.setText("已使用:"+list.get(i).getUse_num());
            vh.left.setText("未使用:"+list.get(i).getUse_left());
            int progress=(Integer.parseInt(list.get(i).getUse_num())*100)/(Integer.parseInt(list.get(i).getUse_num())+Integer.parseInt(list.get(i).getUse_left()));
            vh.bar.setProgress(progress);
            return view;
        }
        class ViewHolder{
            TextView name;
            TextView used;
            TextView left;
            HorizontalProgressBarWithNumber bar;
        }
    }
}
