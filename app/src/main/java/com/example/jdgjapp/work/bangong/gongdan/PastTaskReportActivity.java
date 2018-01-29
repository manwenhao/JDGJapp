package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.R;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastTaskReportActivity extends AppCompatActivity {

    private static final String TAG = "PastTaskReportActivity";
    private Button backBtn;
    private ListView lv;
    private String taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_task_report);
        backBtn = (Button) findViewById(R.id.btn_back);
        lv = (ListView) findViewById(R.id.lv_task_report);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = getIntent().getIntExtra("id",0);
//                if (id == 1) {
//                    Intent intent1 = new Intent(PastTaskReportActivity.this,OnGoingTaskInfoActivity.class);
//                    startActivity(intent1);
//                } else if (id == 2) {
//                    Intent intent2 = new Intent(PastTaskReportActivity.this,DoneTaskInfoActivity.class);
//                    startActivity(intent2);
//                }
                finish();
            }
        });

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        Log.d(TAG, "历史简报传递过来的taskid = " + taskid);

        List<Map<String, Object>> list = getData();
        lv.setAdapter(new TaskReport_ListViewAdapter(PastTaskReportActivity.this, list));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取控件上的time
                TextView titleTv = (TextView) view.findViewById(R.id.title);
                String sendtime = titleTv.getText().toString();
                Intent intent = new Intent(PastTaskReportActivity.this,PastTaskReportInfoActivity.class);
                intent.putExtra("sendtime",sendtime);
                startActivity(intent);
            }
        });

    }

    public List<Map<String, Object>> getData() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Log.d(TAG, "开始历史简报查询");
        //从数据库中查询简报数据
        List<TaskReport> taskReports = DataSupport
                .select("sendtime","content")
                .where("taskid = ?",taskid)
                .find(TaskReport.class);
        Log.d(TAG, "历史简报查询成功");


        for(TaskReport taskReport : taskReports)
        {
            Log.d(TAG, "历史简报sendtime id is "+taskReport.getSendtime());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", taskReport.getSendtime());
            map.put("info", taskReport.getContent());
            list.add(map);
        }
        return list;
    }

}
