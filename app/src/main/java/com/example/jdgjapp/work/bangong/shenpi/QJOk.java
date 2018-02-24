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

import com.example.jdgjapp.Bean.QJApplyRes;
import com.example.jdgjapp.Bean.QJSPOK;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class QJOk extends AppCompatActivity {
    private ListView listView;
    private Myadapter myadapter;
    private List<QJSPOK> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qjok);
        listView=(ListView)findViewById(R.id.sp_qj_ok_listview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:80/JDGJ/BackManagerLeaveReply")
                        .addParams("user_id", MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(QJOk.class.getName(),e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(QJOk.class.getName(),response);
                                Type type=new TypeToken<List<QJSPOK>>(){}.getType();
                                list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myadapter=new Myadapter(list,MyApplication.getContext());
                                        listView.setAdapter(myadapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                QJSPOK e=list.get(i);
                                                QJApplyRes res=new QJApplyRes();
                                                res.setAnsreason(e.getAnsreason());
                                                res.setAnswer(e.getAnswer());
                                                res.setDatime(e.getDatime());
                                                res.setStartdate(e.getStartdate());
                                                res.setEnddate(e.getEnddate());
                                                res.setId(e.getId());
                                                res.setMan_id(e.getMan_id());
                                                res.setType(e.getType());
                                                res.setReason(e.getReason());
                                                Intent intent=new Intent(MyApplication.getContext(), QJApplyDetail.class);
                                                intent.putExtra("bean",res);
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
        private List<QJSPOK> list;
        private LayoutInflater inflater;
        public Myadapter(List<QJSPOK> list, Context context) {
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
        public void setdate(List<QJSPOK> list){
            this.list=list;
        }
        class ViewHolder{
            TextView name;
            TextView id;
            TextView time;
        }
    }
}
