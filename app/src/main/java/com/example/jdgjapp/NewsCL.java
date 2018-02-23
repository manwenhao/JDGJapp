package com.example.jdgjapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.SimpleCl;
import com.example.jdgjapp.Bean.SimplePerson;
import com.example.jdgjapp.Bean.SystemNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NewsCL extends AppCompatActivity {
    private TextView id,time,reason;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_cl);
        SystemNews e=(SystemNews) getIntent().getSerializableExtra("bean");
        id=(TextView)findViewById(R.id.cl_news_id);
        time=(TextView)findViewById(R.id.cl_news_time);
        reason=(TextView)findViewById(R.id.cl_news_reason);
        listView=(ListView)findViewById(R.id.cl_news_list);
        Log.d("===",e.toString());
        String[]strs=e.getContent().split("@#\\$");
        id.setText("申请者工号："+strs[1]);
        time.setText("申请时间："+strs[2]);
        reason.setText("申请理由："+strs[3]);
        String jsonstr=strs[4];
        Type type=new TypeToken<List<SimpleCl>>(){}.getType();
        List<SimpleCl> list=new Gson().fromJson(jsonstr,type);
        Myadapter myadapter=new Myadapter(list,this);
        listView.setAdapter(myadapter);

    }
    class Myadapter extends BaseAdapter{
        private List<SimpleCl> list;
        private LayoutInflater inflater;

        public Myadapter(List<SimpleCl> list, Context context) {
            this.list=list;
            this.inflater=LayoutInflater.from(context);
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
                view=inflater.inflate(R.layout.news_cl_item,viewGroup,false);
                vh=new ViewHolder();
                vh.name=(TextView)view.findViewById(R.id.cl_news_item_name);
                vh.num=(TextView)view.findViewById(R.id.cl_news_item_num);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.name.setText("材料名："+list.get(i).getMat_name());
            vh.num.setText("申请数量："+list.get(i).getMat_num());
            return view;
        }
        class ViewHolder{
            TextView name;
            TextView num;
        }
    }
}
