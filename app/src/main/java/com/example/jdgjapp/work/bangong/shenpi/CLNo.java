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

import com.example.jdgjapp.Bean.CLSPNO;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CLNo extends AppCompatActivity {
    private ListView listView;
    private List<CLITEM> list;
    private Myadapter myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clno);
        ActivityUtils.getInstance().addActivity(CLNo.class.getName(),this);
        listView=(ListView)findViewById(R.id.sp_cl_no_listview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user= ReturnUsrDep.returnUsr();
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackManagerMaterialNoReply")
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
                                Log.d(CLNo.class.getName(),response);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("clspnodata",response);
                                Type type=new TypeToken<List<CLSPNO>>(){}.getType();
                                List<CLSPNO> datalist=new Gson().fromJson(response,type);
                               list=new ArrayList<CLITEM>();
                                for (CLSPNO e:datalist){
                                    boolean flag=true;
                                    for (CLITEM a:list){
                                        if (a.getSign().equals(e.getMatid())){
                                            flag=false;
                                            break;
                                        }
                                    }
                                    if (flag){
                                        CLITEM clitem=new CLITEM();
                                        clitem.setTime(e.getDatetime());
                                        clitem.setSign(e.getMatid());
                                        clitem.setName(e.getUsername());
                                        clitem.setId(e.getUserid());
                                        list.add(clitem);
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myadapter=new Myadapter(list,MyApplication.getContext());
                                        listView.setAdapter(myadapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent=new Intent(MyApplication.getContext(),CLNOList.class);
                                                intent.putExtra("sign",list.get(i).getSign());
                                                intent.putExtra("userid",list.get(i).getId());
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
        private List<CLITEM> list;
        private LayoutInflater inflater;
        public Myadapter(List<CLITEM> list, Context context) {
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
                view=inflater.inflate(R.layout.sp_cl_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.sp_cl_item_name);
                vh.id=(TextView)view.findViewById(R.id.sp_cl_item_id);
                vh.time=(TextView)view.findViewById(R.id.sp_cl_item_time);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.time.setText("时间："+list.get(i).getTime());
            vh.name.setText("姓名："+list.get(i).getName());
            vh.id.setText("工号："+list.get(i).getId());
            return view;

        }
        public void setdate(List<CLITEM> list){
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
        ActivityUtils.getInstance().delActivity(CLNo.class.getName());
    }
}
