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
    private Button backBtn;
    private Button userReportBtn;
    private Button submitBtn;
    private Button pastBtn;
    private TextView titleTv;
    private TextView contentTv;
    private TextView senderTv;
    private TextView cycleTv;
    private String id;
    private String taskid;
    private String responseDate;
    ProgressDialog pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going_task_info);
        backBtn = (Button) findViewById(R.id.btn_back);
        userReportBtn = (Button) findViewById(R.id.task_info_user_report);
        submitBtn = (Button) findViewById(R.id.task_info_submit);
        pastBtn = (Button) findViewById(R.id.task_info_past_report);
        titleTv = (TextView) findViewById(R.id.task_info_title);
        contentTv = (TextView) findViewById(R.id.task_info_info);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnGoingTaskInfoActivity.this,PastTaskReportActivity.class);
                startActivity(intent);
            }
        });

        //读取工号
        SharedPreferences pref = getSharedPreferences("userinfo",MODE_PRIVATE);
        id = pref.getString("1","");

        //获取传递过来的taskid
        final Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        Log.d(TAG, "传递过来的taskid = " + taskid);

        List<Task> tasks = DataSupport.select("taskid","sender","cycle","content")
                .where("taskid = ?", taskid)
                .find(Task.class);

        //将数据显示到界面上
        for (Task task : tasks) {
            titleTv.setText(task.getTaskid());
            senderTv.setText(task.getSender());
            cycleTv.setText(task.getCycle());
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

                TongBuTaskReportRequest(taskid);

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
                        //发送接收状态给服务器
                        sendRequest(taskid,id);

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
                            .url("http://106.14.145.208:8080/KQ/reciveFinishedOrder")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    responseDate = response.body().string();

//                    showResponse(responseDate);

                    if (responseDate.equals("ok")) {  //发送成功
//                    修改数据库中工单状态staus=3已完成
                    Task task = new Task();
                    task.setStatus("3");
                    task.updateAll("taskid = ?", taskid);

                    showResponse("已完成工单" + taskid);

                    Intent intent = new Intent(OnGoingTaskInfoActivity.this, DoneTaskActivity.class);
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

    //发送请求匹配简报数据
    private void TongBuTaskReportRequest(final String workid) {
        //设置ProgressDialog
        final ProgressDialog pro = new ProgressDialog(OnGoingTaskInfoActivity.this);
        pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        pro.setCancelable(false);// 设置是否可以通过点击Back键取消
        pro.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pro.setMessage("正在同步工单简报...");
        pro.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        pro.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("workid",workid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:8080/KQ/sendOrdReportToApp")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

//                    showResponse(responseDate);
                    TongBuTaskReportparseJSON(responseData);

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    pro.dismiss();
                    Intent intent1 = new Intent(OnGoingTaskInfoActivity.this,PastTaskReportActivity.class);
                    intent1.putExtra("taskid",taskid);
//                intent1.putExtra("id",1);
                    startActivity(intent1);
                }
            }
        }).start();
        Log.d(TAG, "已发送");
    }

    private void TongBuTaskReportparseJSON(String jsonData) {
        Gson gson = new Gson();
        List<TaskReport> taskReportList = gson.fromJson(jsonData, new TypeToken<List<TaskReport>>() {
        }.getType());
        for (TaskReport taskReport : taskReportList) {
            Log.d(TAG, "taskid is " + taskReport.getTaskid());
            Log.d(TAG, "sendtime is " + taskReport.getSendtime());
            Log.d(TAG, "content is " + taskReport.getContent());
            Log.d(TAG, "picpath is" + taskReport.getPicpath());

            //查询本地数据库，如果没有则添加
            List<TaskReport> taskReports = DataSupport.where("taskid = ? and sendtime = ?",taskReport.getTaskid(),taskReport.getSendtime()).find(TaskReport.class);
            if (taskReports.isEmpty()) {
                TaskReport taskReport1 = new TaskReport();
                taskReport1.setTaskid(taskReport.getTaskid());
                taskReport1.setSendtime(taskReport.getSendtime());
                taskReport1.setContent(taskReport.getContent());
                taskReport1.setPicpath(taskReport.getPicpath());
                taskReport1.save();
                Log.d(TAG, "已添加工单简报" + taskReport.getTaskid());
            }
        }

        //将本地数据与接收的数据对比，多的删除
        List<TaskReport> bdtaskreport = DataSupport.where("taskid = ?",taskid).find(TaskReport.class);
        for (TaskReport taskReport : bdtaskreport) {
            Log.d(TAG, "bdtaskid is " + taskReport.getTaskid());
            Log.d(TAG, "bdsendtime is " + taskReport.getSendtime());
            Log.d(TAG, "bdcontent is " + taskReport.getContent());
            Log.d(TAG, "bdpicpath is" + taskReport.getPicpath());
            int num=0;
            for (TaskReport taskReport2 : taskReportList) {
                if(taskReport2.getSendtime()==taskReport.getSendtime()) num++;
            }
            if(num!=1) {
                DataSupport.deleteAll(TaskReport.class,"sendtime",taskReport.getSendtime());
            }
        }

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
