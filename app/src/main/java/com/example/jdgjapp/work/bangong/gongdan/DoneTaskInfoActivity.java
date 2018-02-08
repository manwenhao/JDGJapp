package com.example.jdgjapp.work.bangong.gongdan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class DoneTaskInfoActivity extends AppCompatActivity {

    private static final String TAG = "DoneTaskInfoActivity";
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
        setContentView(R.layout.activity_done_task_info);
        pastBtn = (Button) findViewById(R.id.task_info_past_report);

        idTv = (TextView) findViewById(R.id.task_info_id);
        senderTv = (TextView) findViewById(R.id.task_info_sender);
        createtimeTv = (TextView) findViewById(R.id.task_info_createtime);
        startimeTv = (TextView) findViewById(R.id.task_info_startime);
        cycleTv = (TextView) findViewById(R.id.task_info_cycle);
        addrTv = (TextView) findViewById(R.id.task_info_addr);
        statusTv = (TextView) findViewById(R.id.task_info_status);
        contentTv = (TextView) findViewById(R.id.task_info_content);

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");

        List<Task> tasks = DataSupport.where("taskid = ?", taskid).find(Task.class);

        //将数据显示到界面上
        for (Task task : tasks) {
            idTv.setText(task.getTaskid());
            senderTv.setText(task.getSender());
            createtimeTv.setText(task.getCreatetime());
            startimeTv.setText(task.getStartime());
            cycleTv.setText(task.getCycle());
            addrTv.setText(task.getAddr());
            if (task.getStatus().equals("3")) statusTv.setText("已完成");
            else if (task.getStatus().equals("5")) statusTv.setText("逾期已完成");
            else statusTv.setText("转发"); //6
            contentTv.setText(task.getContent());
        }

        pastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DoneTaskInfoActivity.this,PastTaskReportActivity.class);
                intent1.putExtra("taskid",taskid);
                startActivity(intent1);
            }
        });

    }



}
