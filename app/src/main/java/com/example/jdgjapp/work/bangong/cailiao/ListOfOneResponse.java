package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ListOfOneResponse extends AppCompatActivity {
    private ListView listView;
    private TextView get;
    private boolean retisget=false;
    private String flag;
    private String sign;
    private String isget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_one_response);
        listView=(ListView)findViewById(R.id.apply_cl_ofone_listview);
        get=(TextView)findViewById(R.id.cl_res_doget);
          flag=getIntent().getStringExtra("flag");
        sign=getIntent().getStringExtra("sign");
         isget=getIntent().getStringExtra("isget");
        if (flag.equals("clapplypass")&&isget!=null){
            if (isget.equals("0")){
                get.setVisibility(View.VISIBLE);
                get.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpUtils.post()
                                        .url("http://106.14.145.208:80/JDGJ/ReceiveDeptLeadedMaterial")
                                        .addParams("sign",sign)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                Log.d("确认领料",e.toString());
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                Log.d("确认领料",response);
                                                if (response.equals("ok")){
                                                    retisget=true;
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(ListOfOneResponse.this, "领料成功!", Toast.LENGTH_SHORT).show();
                                                            get.setVisibility(View.GONE);
                                                            Intent intent=new Intent();
                                                            intent.putExtra("sign",sign);
                                                            intent.putExtra("isget",retisget);
                                                            setResult(RESULT_OK,intent);
                                                        }
                                                    });
                                                }else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(ListOfOneResponse.this, "领料失败，请重试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        }).start();
                    }
                });
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                String datestring=aCache.getAsString(flag);
                Type type=new TypeToken<List<CaiLiaoResponse>>(){}.getType();
                List<CaiLiaoResponse> list=new Gson().fromJson(datestring,type);
                final List<CaiLiaoResponse> datalist=new ArrayList<CaiLiaoResponse>();
                for (CaiLiaoResponse c:list){
                    if (c.getSign().equals(sign)){
                        datalist.add(c);
                    }
                }
                Log.d("listofoneresponse",datalist.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ApplyResponseAdapter applyResponseAdapter=new ApplyResponseAdapter(MyApplication.getContext(),datalist);
                        listView.setAdapter(applyResponseAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(MyApplication.getContext(),DatailOfApply.class);
                                intent.putExtra("bean",datalist.get(i));
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
