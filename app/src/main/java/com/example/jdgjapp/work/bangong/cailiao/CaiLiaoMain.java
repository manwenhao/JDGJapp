package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.PersonCaiLiao;
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
import java.util.List;

import okhttp3.Call;

public class CaiLiaoMain extends AppCompatActivity {
    private RelativeLayout mydept,apply;
    private ListView listView;
    private Button start;
    private List<PersonCaiLiao> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_liao_main);
        mydept=(RelativeLayout)findViewById(R.id.cailiao_mydept);
        listView=(ListView)findViewById(R.id.person_cailiao_listview);
        start=(Button)findViewById(R.id.person_cailiao_detail);
        apply=(RelativeLayout)findViewById(R.id.cailiao_mydept_apply);
        final User user= ReturnUsrDep.returnUsr();
        mydept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUsr_bossId()==null||user.getUsr_bossId().equals(MyApplication.bossid)){
                    startActivity(new Intent(CaiLiaoMain.this,MyDeptOfCaiLiao.class));
                }else {
                    Toast.makeText(CaiLiaoMain.this, "您的权限不足，无法查看部门材料信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUsr_bossId()==null||user.getUsr_bossId().equals(MyApplication.bossid)){
                    startActivity(new Intent(CaiLiaoMain.this,CaiLiaoApply.class));
                }else {
                    Toast.makeText(CaiLiaoMain.this, "您的权限不足，无法查看部门材料信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoMain.this,SeePersonCailiao.class));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppselfMaterialUses")
                        .addParams("user_id",MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("个人材料使用大致情况",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("个人材料使用大致情况",response);
                                Type type=new TypeToken<List<PersonCaiLiao>>(){}.getType();
                                final List<PersonCaiLiao> liaos=new Gson().fromJson(response,type);
                                Log.d("个人材料使用大致情况",Boolean.toString(liaos==null));
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                           @Override
                                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                               Intent intent=new Intent(CaiLiaoMain.this,SeePersonCailiao.class);
                                               intent.putExtra("flag",liaos.get(i).getMat_name());
                                               startActivity(intent);
                                           }
                                       });
                                       MyAdapter adapter=new MyAdapter(MyApplication.getContext(),liaos);
                                       listView.setAdapter(adapter);
                                   }
                               });
                            }
                        });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User user= ReturnUsrDep.returnUsr();
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppselfMatUseDatails")
                        .addParams("user_id",user.getUsr_id())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("个人详细材料使用",response);
                                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                aCache.put("personcld",response);
                            }
                        });
            }
        }).start();
        if (user.getUsr_bossId()==null||user.getUsr_bossId().equals(MyApplication.bossid)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final User user= ReturnUsrDep.returnUsr();
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackAppMagDeptMatSituat")
                            .addParams("dept_id",user.getUsr_deptId())
                            .addParams("user_id",user.getUsr_id())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("部门总的材料情况",e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("部门总的材料情况",response);
                                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("deptclw",response);
                                }
                            });
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final User user= ReturnUsrDep.returnUsr();
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackAppDeptMatUseDetail")
                            .addParams("dept_id",user.getUsr_deptId())
                            .addParams("user_id",user.getUsr_id())
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("部门每个人详细材料情况",e.toString());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d("部门每个人详细材料情况",response);
                                    ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                                    aCache.put("deptclp",response);
                                }
                            });
                }
            }).start();
        }


    }
    class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<PersonCaiLiao>list;
        public MyAdapter(Context context,List<PersonCaiLiao> list){
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
            ViewHolder wh;
            if (view==null){
                wh=new ViewHolder();
                view=inflater.inflate(R.layout.cailiao_persion_item,viewGroup,false);
                wh.type=view.findViewById(R.id.cailiao_type);
                wh.number=view.findViewById(R.id.cailiao_number);
                view.setTag(wh);
            }else {
                wh=(ViewHolder)view.getTag();
            }
            wh.type.setText("类别:"+list.get(i).getMat_name());
            wh.number.setText("使用数量:"+list.get(i).getMat_num());
            return view;
        }
        class ViewHolder{
            TextView type;
            TextView number;
        }
    }
}
