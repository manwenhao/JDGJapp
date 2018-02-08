package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class TaskInfoOfCL extends AppCompatActivity {

    private TextView idTv;
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
        setContentView(R.layout.activity_task_info_of_cl);

        idTv = (TextView) findViewById(R.id.task_info_id);
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

        List<Task> tasks = DataSupport.where("taskid = ?", taskid).find(Task.class);
        Task task = tasks.get(0);

        //将数据显示到界面上
        idTv.setText(task.getTaskid());
        senderTv.setText(task.getSender());
        createtimeTv.setText(task.getCreatetime());
        startimeTv.setText(task.getStartime());
        cycleTv.setText(task.getCycle());
        addrTv.setText(task.getAddr());
        switch (task.getStatus()){
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
            default:break;
        }
        contentTv.setText(task.getContent());

    }
}
