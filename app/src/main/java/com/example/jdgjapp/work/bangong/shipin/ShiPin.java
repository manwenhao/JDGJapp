package com.example.jdgjapp.work.bangong.shipin;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.FromShiPinItem;
import com.example.jdgjapp.Friends.Contact;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class ShiPin extends AppCompatActivity {
    private ListView listView;
    private Button button;
    private ImageView back;
    private FormShiPinListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi_pin);
        listView=(ListView)findViewById(R.id.form_shipin);
        button=(Button)findViewById(R.id.start_shipin_button);
        back=(ImageView)findViewById(R.id.readyshipin_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShiPin.this,ShiPinMain.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackVideoRecord")
                        .addParams("user_id", MyApplication.getid())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("查看视频历史错误",e.toString());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("查看视频历史,解析前",response);
                                Type type =new TypeToken<List<FromShiPinItem>>(){}.getType();
                                final List<FromShiPinItem> list=new Gson().fromJson(response,type);
                                Log.d("查看视频历史,解析后",response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter=new FormShiPinListAdapter(list,MyApplication.getContext());
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                ((MyApplication)MyApplication.getContext()).initWorkerThread();
                                                String chanel=list.get(i).getVid_chanel();
                                                Intent intent=new Intent(MyApplication.getContext(), ChatActivity.class);
                                                intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
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
    class FormShiPinListAdapter extends BaseAdapter{
        private List<FromShiPinItem> list;
        private LayoutInflater inflater;
        public FormShiPinListAdapter() {
        }

        public FormShiPinListAdapter(List<FromShiPinItem> list, Context context) {
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
                view=inflater.inflate(R.layout.form_shipin_item,viewGroup,false);
                viewHolder.sender=(TextView) view.findViewById(R.id.from_shipin_item_name);
                viewHolder.time=(TextView)view.findViewById(R.id.form_shipin_item_time);
                view.setTag(viewHolder);
            }else {
                viewHolder=(ViewHolder)view.getTag();
            }
            viewHolder.sender.setText("发起者:"+list.get(i).getVid_sender());
            viewHolder.time.setText(list.get(i).getVid_date());
            return view;
        }
        class ViewHolder{
            TextView sender;
            TextView time;
        }
    }

}
