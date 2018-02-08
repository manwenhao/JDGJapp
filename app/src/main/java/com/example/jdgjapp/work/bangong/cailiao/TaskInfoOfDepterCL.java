package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;

public class TaskInfoOfDepterCL extends AppCompatActivity {

    private static final String TAG = "TaskInfoOfDepterCL";
    private TextView idTv;
    private TextView usridTv;
    private TextView usrnameTv;
    private TextView senderTv;
    private TextView createtimeTv;
    private TextView startimeTv;
    private TextView cycleTv;
    private TextView addrTv;
    private TextView statusTv;
    private TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_of_depter_cl);

        idTv = (TextView) findViewById(R.id.task_info_id);
        usridTv = (TextView) findViewById(R.id.task_info_usr_id);
        usrnameTv = (TextView) findViewById(R.id.task_info_usr_name);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        createtimeTv = (TextView) findViewById(R.id.task_info_createtime);
        startimeTv = (TextView) findViewById(R.id.task_info_startime);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);
        addrTv = (TextView) findViewById(R.id.task_info_addr);
        statusTv = (TextView) findViewById(R.id.task_info_status);
        contentTv = (TextView) findViewById(R.id.task_info_content);

        //接收intent
        Intent intent = getIntent();
        String taskid = intent.getStringExtra("task_id");

        sendRequest(taskid);

    }

    private void sendRequest(final String taskid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackAppOrderInfoById")
                            .addParams("workid",taskid)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d(TAG, "查询工单失败"+e.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(TaskInfoOfDepterCL.this,"查询工单失败！",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(final String response, int id) {
                                    Log.d(TAG, "查询工单成功"+response);
                                    parseJson(response);
                                }
                            });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseJson(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String taskId = jsonObject.getString("taskid");
                final String sender = jsonObject.getString("sender");
                final String createtime = jsonObject.getString("createtime");
                final String usrid = jsonObject.getString("wk_doper");
                final String usrname = jsonObject.getString("usr_name");
                final String content = jsonObject.getString("content");
                final String addr = jsonObject.getString("addr");
                final String startime = jsonObject.getString("startime");
                final String cycle = jsonObject.getString("cycle");
                final String status = jsonObject.getString("status");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        idTv.setText(taskId);
                        senderTv.setText(sender);
                        createtimeTv.setText(createtime);
                        usridTv.setText(usrid);
                        usrnameTv.setText(usrname);
                        contentTv.setText(content);
                        addrTv.setText(addr);
                        startimeTv.setText(startime);
                        cycleTv.setText(cycle);
                        switch (status){
                            case "0":
                                statusTv.setText("未开始");
                                break;
                            case "1":
                                statusTv.setText("待接收");
                                break;
                            case "2":
                                statusTv.setText("进行中");
                                break;
                            case "3":
                                statusTv.setText("已完成");
                                break;
                            case "4":
                                statusTv.setText("逾期未完成");
                                break;
                            case "5":
                                statusTv.setText("逾期已完成");
                                break;
                            case "6":
                                statusTv.setText("转发工单");
                                break;
                            case "8":
                                statusTv.setText("待派发");
                                break;
                            case "9":
                                statusTv.setText("已拒绝");
                            default:break;
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
