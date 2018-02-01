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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.PersonCaiLiaoDetail;
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
import java.util.List;

import okhttp3.Call;

public class  SeePersonCailiao extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_person_cailiao);
        listView=(ListView)findViewById(R.id.simple_person_cailiao_listview);
        final String flag=getIntent().getStringExtra("flag");
        if (flag==null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                    String data=aCache.getAsString("personcld");
                    if (data==null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SeePersonCailiao.this, "您当前网络情况不佳，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Type type=new TypeToken<List<PersonCaiLiaoDetail>>(){}.getType();
                        final List<PersonCaiLiaoDetail> liaoDetails=new Gson().fromJson(data,type);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyAdapter adapter=new MyAdapter(MyApplication.getContext(),liaoDetails);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (liaoDetails.get(i).getUse_kind().equals("0")){
                                            Intent intent=new Intent(MyApplication.getContext(),PersonDetailCaiLiao.class);
                                            intent.putExtra("person",liaoDetails.get(i));
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }).start();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                    String data=aCache.getAsString("personcld");
                    if (data==null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SeePersonCailiao.this, "您当前网络情况不佳，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Type type=new TypeToken<List<PersonCaiLiaoDetail>>(){}.getType();
                        final List<PersonCaiLiaoDetail> liaoDetails=new Gson().fromJson(data,type);
                        final List<PersonCaiLiaoDetail>list=new ArrayList<PersonCaiLiaoDetail>();
                        for (PersonCaiLiaoDetail detail:liaoDetails){
                            if (detail.getUse_name().equals(flag)){
                                list.add(detail);
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
                                        if (list.get(i).getUse_kind().equals("0")){
                                            //日常使用
                                            Intent intent=new Intent(MyApplication.getContext(),PersonDetailCaiLiao.class);
                                            intent.putExtra("person",list.get(i));
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }
                        });

                    }
                }
            }).start();

        }
    }
    class MyAdapter extends BaseAdapter{
        private List<PersonCaiLiaoDetail> list;
        private LayoutInflater inflater;
        public MyAdapter(Context context,List<PersonCaiLiaoDetail> list) {
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
                view=inflater.inflate(R.layout.person_simple_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.person_simplecailiao_name);
                vh.date=(TextView)view.findViewById(R.id.person_simplecailiao_date);
                vh.kind=(TextView)view.findViewById(R.id.person_simplecailiao_kind);
                vh.num=(TextView)view.findViewById(R.id.person_simplecailiao_num);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.name.setText("材料:"+list.get(i).getUse_name());
            vh.date.setText("使用时间:"+list.get(i).getUse_time());
            vh.num.setText("使用数量:"+list.get(i).getUse_num());
            if (list.get(i).getUse_kind().equals("0")){
                vh.kind.setText("使用去处:日常使用");
            }else if (list.get(i).getUse_kind().equals("1")){
                vh.kind.setText("使用去处:用于工单");
            }
            return view;
        }
        class ViewHolder{
            TextView name;
            TextView date;
            TextView num;
            TextView kind;
        }
    }
}
