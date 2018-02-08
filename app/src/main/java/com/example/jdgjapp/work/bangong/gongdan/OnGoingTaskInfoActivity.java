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
import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OnGoingTaskInfoActivity extends AppCompatActivity {

    private static final String TAG = "OnGoingTaskInfoActivity";
    private Button userReportBtn;
    private Button submitBtn;
    private Button pastBtn;

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
        setContentView(R.layout.activity_on_going_task_info);
        userReportBtn = (Button) findViewById(R.id.task_info_user_report);
        submitBtn = (Button) findViewById(R.id.task_info_submit);
        pastBtn = (Button) findViewById(R.id.task_info_past_report);

        idTv = (TextView) findViewById(R.id.task_info_id);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        createtimeTv = (TextView) findViewById(R.id.task_info_createtime);
        startimeTv = (TextView) findViewById(R.id.task_info_startime);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);
        addrTv = (TextView) findViewById(R.id.task_info_addr);
        statusTv = (TextView) findViewById(R.id.task_info_status);
        contentTv = (TextView) findViewById(R.id.task_info_content);


        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnGoingTaskInfoActivity.this,PastTaskReportActivity.class);
                startActivity(intent);
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
            if (task.getStatus().equals("2")) statusTv.setText("进行中");
            else statusTv.setText("逾期未完成"); //4
            contentTv.setText(task.getContent());
        }

        userReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnGoingTaskInfoActivity.this,TaskReportActivity.class);
                intent.putExtra("taskid",taskid);
                startActivity(intent);
            }
        });

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OnGoingTaskInfoActivity.this,PastTaskReportActivity.class);
                intent1.putExtra("taskid",taskid);
                startActivity(intent1);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(OnGoingTaskInfoActivity.this);
                dialog.setTitle("是否确定完成工单？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendRequest(taskid, ReturnUsrDep.returnUsr().getUsr_id());

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("workid",workid)
                            .add("user_id",userid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/JDGJ/ReceiveOrderForSubmit")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    if (responseDate.equals("ok")) {
                        //修改数据库中工单状态staus=3已完成
                        Task task = new Task();
                        task.setStatus("3");
                        task.updateAll("taskid = ?", taskid);

                        showResponse("已完成工单" + taskid);

                        Intent intent = new Intent(OnGoingTaskInfoActivity.this, GongDanMain.class);
                        startActivity(intent);
                        GongDanMain.status = "3";
                        finish();
                    } else {
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
                Toast.makeText(OnGoingTaskInfoActivity.this,response, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
