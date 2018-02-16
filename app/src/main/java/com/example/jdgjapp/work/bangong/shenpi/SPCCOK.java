package com.example.jdgjapp.work.bangong.shenpi;

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

import com.example.jdgjapp.Bean.CCApplyRes;
import com.example.jdgjapp.Bean.QJSPOK;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplyDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class SPCCOK extends AppCompatActivity {
    private ListView listView;
    private Myadapter myadapter;
    private List<com.example.jdgjapp.Bean.SPCCOK> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spccok);
        listView=(ListView)findViewById(R.id.sp_cc_ok_listview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackManagerTravelReply")
                        .addParams("user_id", MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(SPCCOK.class.getName(),response);
                                Type type=new TypeToken<List<com.example.jdgjapp.Bean.SPCCOK>>(){}.getType();
                                list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myadapter=new Myadapter(list,MyApplication.getContext());
                                        listView.setAdapter(myadapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                com.example.jdgjapp.Bean.SPCCOK e=list.get(i);
                                                CCApplyRes a=new CCApplyRes();
                                                a.setId(e.getId());
                                                a.setMan_id(e.getMan_id());
                                                a.setAnsreason(e.getAnsreason());
                                                a.setAnswer(e.getAnswer());
                                                a.setDatime(e.getDatime());
                                                a.setEnddate(e.getEnddate());
                                                a.setReason(e.getReason());
                                                a.setStartdate(e.getStartdate());
                                                a.setType(e.getType());
                                                Intent intent=new Intent(MyApplication.getContext(), CCApplyDetail.class);
                                                intent.putExtra("bean",a);
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
    class Myadapter extends BaseAdapter {
        private List<com.example.jdgjapp.Bean.SPCCOK> list;
        private LayoutInflater inflater;
        public Myadapter(List<com.example.jdgjapp.Bean.SPCCOK> list, Context context) {
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
                view=inflater.inflate(R.layout.sp_cc_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.sp_cc_item_name);
                vh.id=(TextView)view.findViewById(R.id.sp_cc_item_id);
                vh.time=(TextView)view.findViewById(R.id.sp_cc_item_time);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.time.setText("时间："+list.get(i).getDatime());
            vh.name.setText("姓名："+list.get(i).getUsr_name());
            vh.id.setText("工号："+list.get(i).getMan_id());
            return view;

        }
        public void setdate(List<com.example.jdgjapp.Bean.SPCCOK> list){
            this.list=list;
        }
        class ViewHolder{
            TextView name;
            TextView id;
            TextView time;
        }
    }
}
