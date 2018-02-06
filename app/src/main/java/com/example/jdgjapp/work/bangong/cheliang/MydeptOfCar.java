package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.cailiao.CaiLiaoWholeOfDept;

import java.util.ArrayList;
import java.util.List;

public class MydeptOfCar extends AppCompatActivity {
    private ListView listView;
    private User u;
    private List<Depart> deptlist;
    private DeptAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydept_of_car);
        listView=(ListView)findViewById(R.id.car_dept);
        new Thread(new Runnable() {
            @Override
            public void run() {
                u= ReturnUsrDep.returnUsr();
                deptlist=ReturnUsrDep.returnDep();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (u.getUsr_bossId()==null){
                            adapter=new DeptAdapter(deptlist, MyApplication.getContext());
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent=new Intent(MyApplication.getContext(),CarWholeOfDept.class);
                                    intent.putExtra("deptid",deptlist.get(i).getDep_id());
                                    intent.putExtra("deptname",deptlist.get(i).getDep_name());
                                    startActivity(intent);
                                }
                            });
                        }else {
                            final List<Depart>list=new ArrayList<Depart>();
                            for (Depart d:deptlist){
                                if (d.getDep_id().equals(u.getUsr_deptId())){
                                    list.add(d);
                                }
                            }
                            adapter=new DeptAdapter(list, MyApplication.getContext());
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent=new Intent(MyApplication.getContext(),CarWholeOfDept.class);
                                    intent.putExtra("deptid",list.get(i).getDep_id());
                                    intent.putExtra("deptname",list.get(i).getDep_name());
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                });

            }
        }).start();
    }
    class DeptAdapter extends BaseAdapter {
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
