package com.example.jdgjapp.work.bangong.gongdan;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.work.bangong.shipin.ShiPin;
import com.example.jdgjapp.work.bangong.shipin.ShiPinMain;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotReceivedTaskInfoActivity extends AppCompatActivity {

    private static final String TAG = "NotReceivedTaskInfoActi";
    private Button acceptBtn;
    private Button transmitBtn;
    private Button refuseBtn;

    private TextView idTv;
    private TextView senderTv;
    private TextView createtimeTv;
    private TextView startimeTv;
    private TextView cycleTv;
    private TextView addrTv;
    private TextView statusTv;
    private TextView contentTv;
    private String taskid;
    public static final String action = "notreceive.action";
    public static final String action1 = "refuse.action";
    public static NotReceivedTaskInfoActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_received_task_info);
        acceptBtn = (Button) findViewById(R.id.task_accept_btn);
        transmitBtn = (Button) findViewById(R.id.task_transmit_btn);
        refuseBtn = (Button) findViewById(R.id.task_refuse_btn);

        idTv = (TextView) findViewById(R.id.task_info_id);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        createtimeTv = (TextView) findViewById(R.id.task_info_createtime);
        startimeTv = (TextView) findViewById(R.id.task_info_startime);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);
        addrTv = (TextView) findViewById(R.id.task_info_addr);
        statusTv = (TextView) findViewById(R.id.task_info_status);
        contentTv = (TextView) findViewById(R.id.task_info_content);

        instance = this;

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        Log.d(TAG, "传递过来的taskid = " + taskid);

        List<Task> tasks = DataSupport.where("taskid = ?", taskid).find(Task.class);

        for (Task task : tasks) {
            idTv.setText(task.getTaskid());
            senderTv.setText(task.getSender());
            createtimeTv.setText(task.getCreatetime());
            startimeTv.setText(task.getStartime());
            cycleTv.setText(task.getCycle());
            addrTv.setText(task.getAddr());
            statusTv.setText("待接收");
            contentTv.setText(task.getContent());
        }

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NotReceivedTaskInfoActivity.this);
                dialog.setTitle("是否确定接收工单？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendRequest(taskid,"1");
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

        transmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NotReceivedTaskInfoActivity.this,TransmitList.class);
                intent.putExtra("taskid",taskid);
                startActivity(intent);
                //finish();
            }
        });

        refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NotReceivedTaskInfoActivity.this);
                dialog.setTitle("是否确定拒绝工单？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendRequest(taskid,"0");

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

    private void sendRequest(final String workid, final String answer) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("workid",workid)
                            .add("answer",answer)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:80/JDGJ/ReceiveUserRespToDispatchOrd")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    if (responseDate.equals("ok")) {  //接收成功
                        //修改数据库中工单状态staus=0未开始
                        Task task = new Task();
                        task.setStatus("0");
                        task.updateAll("taskid = ?", taskid);

                        showResponse("已接收工单" + taskid);

                        Intent intent = new Intent(NotReceivedTaskInfoActivity.this, GongDanMain.class);
                        startActivity(intent);
                        finish();
                        Intent intent0 = new Intent(action);
                        sendBroadcast(intent0);
                    } else if(responseDate.equals("no")){
                        DataSupport.deleteAll(Task.class,"taskid = ?", taskid);
                        showResponse("已拒绝工单" + taskid);

                        Intent intent = new Intent(NotReceivedTaskInfoActivity.this, GongDanMain.class);
                        startActivity(intent);
                        finish();
                        Intent intent0 = new Intent(action1);
                        sendBroadcast(intent0);

                    } else {  //发送失败
                        showResponse("请求失败，请稍后再试！");
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d(TAG, "已发送");
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotReceivedTaskInfoActivity.this,response, Toast.LENGTH_SHORT).show();
            }
        });
    }





}
