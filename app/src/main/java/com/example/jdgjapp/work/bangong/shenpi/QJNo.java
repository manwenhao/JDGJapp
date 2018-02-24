package com.example.jdgjapp.work.bangong.shenpi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.jdgjapp.Bean.QJSPNO;
import com.example.jdgjapp.Bean.QJSPOK;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class QJNo extends AppCompatActivity {
    private ListView listView;
    private List<QJSPNO> list;
    private Myadapter myadapter;
    private IntentFilter intentFilter;
    private Myreceiver myreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qjno);
        listView=(ListView)findViewById(R.id.sp_qj_no_list);
        ActivityUtils.getInstance().addActivity(QJNo.class.getName(),this);
        intentFilter=new IntentFilter();
        intentFilter.addAction("newqj");
        myreceiver=new Myreceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user= ReturnUsrDep.returnUsr();
                OkHttpUtils.post()
                        .url("http://106.14.145.208:80/JDGJ/BackManagerLeaveNoReply")
                        .addParams("user_id", MyApplication.getid())
                        .addParams("dep_id",user.getUsr_deptId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(QJNo.class.getName(),response);
                                Type type=new TypeToken<List<QJSPNO>>(){}.getType();
                                list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myadapter=new Myadapter(list,MyApplication.getContext());
                                        listView.setAdapter(myadapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),QJSPDetail.class);
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
    class Myadapter extends BaseAdapter {
        private List<QJSPNO> list;
        private LayoutInflater inflater;
        public Myadapter(List<QJSPNO> list, Context context) {
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
                view=inflater.inflate(R.layout.sp_qj_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.sp_qj_item_name);
                vh.id=(TextView)view.findViewById(R.id.sp_qj_item_id);
                vh.time=(TextView)view.findViewById(R.id.sp_qj_item_time);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.time.setText("时间："+list.get(i).getDatime());
            vh.name.setText("姓名："+list.get(i).getUsr_name());
            vh.id.setText("工号："+list.get(i).getMan_id());
            return view;

        }
        public void setdate(List<QJSPNO> list){
            this.list=list;
        }
        class ViewHolder{
            TextView name;
            TextView id;
            TextView time;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myreceiver);
        ActivityUtils.getInstance().delActivity(QJNo.class.getName());
    }
    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user= ReturnUsrDep.returnUsr();
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:80/JDGJ/BackManagerLeaveNoReply")
                            .addParams("user_id", MyApplication.getid())
                            .addParams("dep_id",user.getUsr_deptId())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d(QJNo.class.getName(),response);
                                    Type type=new TypeToken<List<QJSPNO>>(){}.getType();
                                    list=new Gson().fromJson(response,type);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myadapter.setdate(list);
                                            myadapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                }
            }).start();
        }
    }

}
