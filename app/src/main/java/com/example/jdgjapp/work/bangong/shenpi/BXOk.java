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

import com.example.jdgjapp.Bean.BaoXiaoPerson;
import com.example.jdgjapp.Bean.SPBXOK;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.bangong.baoxiao.BaoXiaoDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class BXOk extends AppCompatActivity {
    private ListView listView;
    private myadapter adapter;
    private List<SPBXOK> list;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxok);
        listView=(ListView)findViewById(R.id.sp_bx_ok_listview);
        type=new TypeToken<List<SPBXOK>>(){}.getType();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackManagerAccoutReply")
                        .addParams("user_id", MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(BXOk.class.getName(),e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(BXOk.class.getName(),response);
                                list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter=new myadapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                BaoXiaoPerson person=new BaoXiaoPerson();
                                                SPBXOK e=list.get(i);
                                                person.setAcc_answer(e.getAcc_answer());
                                                person.setAcc_answerreason(e.getAcc_answerreason());
                                                person.setAcc_backmoney(e.getAcc_backmoney());
                                                person.setAcc_cont(e.getAcc_cont());
                                                person.setAcc_date(e.getAcc_date());
                                                person.setAcc_id(e.getAcc_id());
                                                person.setAcc_img(e.getAcc_img());
                                                person.setAcc_kind(e.getAcc_kind());
                                                person.setAcc_money(e.getAcc_money());
                                                Intent intent=new Intent(MyApplication.getContext(), BaoXiaoDetail.class);
                                                intent.putExtra("bean",person);
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
    class myadapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<SPBXOK> list;
        public myadapter(List<SPBXOK> list, Context context){
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
            ViewHOlder vh;
            if (view==null){
                view=inflater.inflate(R.layout.sp_bx_item,viewGroup,false);
                vh=new ViewHOlder();
                vh.name=(TextView)view.findViewById(R.id.sp_bx_item_name);
                vh.time=(TextView)view.findViewById(R.id.sp_bx_item_time);
                vh.id=(TextView)view.findViewById(R.id.sp_bx_item_id);
                view.setTag(vh);
            }else {
                vh=(ViewHOlder)view.getTag();
            }
            vh.time.setText("时间："+list.get(i).getAcc_date());
            vh.id.setText("工号："+list.get(i).getAcc_userid());
            vh.name.setText("姓名："+list.get(i).getAcc_username());
            return view;
        }
        class ViewHOlder{
            TextView name;
            TextView time;
            TextView id;
        }
        public void setdate(List<SPBXOK> list){
            this.list=list;
        }
    }

}
