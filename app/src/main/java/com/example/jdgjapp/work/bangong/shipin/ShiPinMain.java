package com.example.jdgjapp.work.bangong.shipin;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.AddressListFragment;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.Friends.DeptMember;
import com.example.jdgjapp.Friends.MyDeptMent;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

public class ShiPinMain extends AppCompatActivity {
    private ImageView back;
    private AddressListFragment fragment;
    public static List<String> useridList=new ArrayList<String>();
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi_pin_main);
        ActivityUtils.getInstance().addActivity(ShiPinMain.class.getName(),this);
        ok=(TextView)findViewById(R.id.shipin_ok);
        DeptMember.flag=1;
        fragment=(AddressListFragment) getFragmentManager().findFragmentById(R.id.select_friend);
        back=(ImageView)findViewById(R.id.shipin_back);
        fragment.setChecked();
        Toast.makeText(this, "最多选择9人进行会议", Toast.LENGTH_LONG).show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.serunChecked();
                useridList=new ArrayList<String>();
                DeptMember.flag=0;
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShiPinMain.useridList.size()==0){
                    Toast.makeText(ShiPinMain.this, "请选择视频人员", Toast.LENGTH_SHORT).show();
                }else if(ShiPinMain.useridList.size()>9){
                    Toast.makeText(ShiPinMain.this, "邀请视频人员不得超过9人，请重试", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("邀请人员id",ShiPinMain.useridList.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder builder=new StringBuilder();
                            builder.append(";");
                            for (String name:ShiPinMain.useridList){
                                builder.append(name);
                                builder.append(";");
                            }
                            String sendlist=builder.toString();
                            final String chanel= MyApplication.getid()+";"+new Date().toString();
                            User u=ReturnUsrDep.returnUsr();
                            String sender=MyApplication.getid()+";"+u.getUsr_name();
                            Log.d("list===",sendlist);
                            Log.d("=====","sender: "+sender+" usr_ids:"+sendlist+" chanel:"+chanel);
                            OkHttpUtils.post()
                                    .url("http://106.14.145.208:8080//JDGJ/SendVideoPush")
                                    .addParams("usr_sender",sender)
                                    .addParams("usr_ids",sendlist)
                                    .addParams("chanel",chanel)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Log.d("视频会议出错",e.toString());
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ActivityUtils.getInstance().delActivity(ShiPinMain.class.getName());
                                                    ActivityUtils.getInstance().delActivity(MyDeptMent.class.getName());
                                                    ActivityUtils.getInstance().delActivity(DeptMember.class.getName());
                                                    ((MyApplication)MyApplication.getContext()).initWorkerThread();
                                                    Intent intent=new Intent(MyApplication.getContext(), ChatActivity.class);
                                                    intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });

                        }
                    }).start();
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment.serunChecked();
        useridList=new ArrayList<String>();
        DeptMember.flag=0;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            fragment.serunChecked();
            useridList=new ArrayList<String>();
            DeptMember.flag=0;
        }

        return super.onKeyDown(keyCode, event);
    }
}
