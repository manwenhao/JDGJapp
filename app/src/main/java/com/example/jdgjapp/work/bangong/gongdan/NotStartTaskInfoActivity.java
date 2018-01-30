package com.example.jdgjapp.work.bangong.gongdan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotStartTaskInfoActivity extends AppCompatActivity {

    private static final String TAG = "NotStartTaskInfoActivit";
    private Button backBtn;
    private Button startBtn;

    private TextView idTv;
    private TextView senderTv;
    private TextView createtimeTv;
    private TextView startimeTv;
    private TextView cycleTv;
    private TextView addrTv;
    private TextView statusTv;
    private TextView contentTv;

    private String taskid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_start_task_info);
        backBtn = (Button) findViewById(R.id.btn_back);
        startBtn = (Button) findViewById(R.id.task_start_btn);

        idTv = (TextView) findViewById(R.id.task_info_id);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        createtimeTv = (TextView) findViewById(R.id.task_info_createtime);
        startimeTv = (TextView) findViewById(R.id.task_info_startime);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);
        addrTv = (TextView) findViewById(R.id.task_info_addr);
        statusTv = (TextView) findViewById(R.id.task_info_status);
        contentTv = (TextView) findViewById(R.id.task_info_content);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        Log.d(TAG, "传递过来的taskid = " + taskid);

        List<Task> tasks = DataSupport.where("taskid = ?", taskid).find(Task.class);

        //将数据显示到界面上
        for (Task task : tasks) {
            idTv.setText(task.getTaskid());
            senderTv.setText(task.getSender());
            createtimeTv.setText(task.getCreatetime());
            startimeTv.setText(task.getStartime());
            cycleTv.setText(task.getCycle());
            addrTv.setText(task.getAddr());
            statusTv.setText("待开始");
            contentTv.setText(task.getContent());
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NotStartTaskInfoActivity.this);
                dialog.setTitle("是否确定开始工单？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //sendRequest(taskid,id);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        
    }


    private void sendRequest(final String workid, final String userid) {
        //设置ProgressDialog
        final ProgressDialog pro = new ProgressDialog(this);
        pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        pro.setCancelable(true);// 设置是否可以通过点击Back键取消
        pro.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pro.setMessage("正在发送请求...");
        pro.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("workid",workid)
                            .add("userid",userid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/KQ/workOrderStartSign")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

//                    showResponse(responseDate);

                    if (responseDate.equals("ok")) {  //发送成功
                        //修改数据库中工单状态staus=2进行中
                        Task task = new Task();
                        task.setStatus("2");
                        task.updateAll("taskid = ?", taskid);

                        showResponse("已开始工单" + taskid);

                        Intent intent = new Intent(NotStartTaskInfoActivity.this, GongDanMain.class);
                        startActivity(intent);
                    } else {  //发送失败
                        showResponse("请求失败，请稍后再试！");
                    }

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    pro.dismiss();
                }
            }
        }).start();
        Log.d(TAG, "已发送");

    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotStartTaskInfoActivity.this,response, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
