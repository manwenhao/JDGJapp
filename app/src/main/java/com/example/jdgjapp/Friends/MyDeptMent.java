package com.example.jdgjapp.Friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.example.jdgjapp.Util.ReturnUsrDep;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MyDeptMent extends AppCompatActivity {
    private ListView listView;
    private User u;
    private List<Depart> deptlist;
    private DeptAdapter adapter;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dept_ment);
        listView=(ListView)findViewById(R.id.my_deptlist);
        back=(ImageView)findViewById(R.id.deptlist_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       new Thread(new Runnable() {
           @Override
           public void run() {
               u= ReturnUsrDep.returnUsr();
               deptlist=ReturnUsrDep.returnDep();
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Intent intent=new Intent(MyDeptMent.this,DeptMember.class);
                       intent.putExtra("deptid",deptlist.get(i).getDep_id());
                       intent.putExtra("deptname",deptlist.get(i).getDep_name());
                       startActivity(intent);
                   }
               });
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (u.getUsr_bossId()==null){
                           adapter=new DeptAdapter(deptlist, MyApplication.getContext());
                       }else {
                           List<Depart>list=new ArrayList<Depart>();
                           for (Depart d:deptlist){
                               if (d.getDep_id().equals(u.getUsr_deptId())){
                                   list.add(d);
                               }
                           }
                           adapter=new DeptAdapter(list, MyApplication.getContext());
                       }
                       listView.setAdapter(adapter);
                   }
               });

           }
       }).start();

    }
    class DeptAdapter extends BaseAdapter{
        private List<Depart> list;
        private LayoutInflater inflater;

        public DeptAdapter(List<Depart> list, Context context) {
            super();
            this.list = list;
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
            ViewHolder viewHolder;
            if (view==null){
                viewHolder=new ViewHolder();
                view=inflater.inflate(R.layout.deptlist_item,viewGroup,false);
                viewHolder.name=(TextView)view.findViewById(R.id.deptitem_name);
                view.setTag(viewHolder);
            }else {
                viewHolder=(ViewHolder)view.getTag();
            }
            viewHolder.name.setText(list.get(i).getDep_name());
            return view;

        }
        class ViewHolder{
            TextView name;
        }
    }
}
