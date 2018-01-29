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


import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.MainActivity;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotStartTaskActivity extends AppCompatActivity {

    private static final String TAG = "NotReceivedTaskActivity";
    private Button backBtn;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_start_task);
        backBtn = (Button) findViewById(R.id.btn_back);
        lv = (ListView) findViewById(R.id.lv_not_start_task);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotStartTaskActivity.this, MainActivity.class);
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });
        List<Map<String, Object>> list = getData();
        lv.setAdapter(new Task_ListViewAdapter(NotStartTaskActivity.this, list));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取控件上的taskid
                TextView idTv = (TextView) view.findViewById(R.id.title);
                String taskid = idTv.getText().toString();
                Log.d(TAG, taskid);
                Intent intent = new Intent(NotStartTaskActivity.this,NotStartTaskInfoActivity.class);
                intent.putExtra("taskid",taskid);
                startActivity(intent);
            }
        });

    }

    public List<Map<String, Object>> getData() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=1未开始的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","1")
                .find(Task.class);

        for(Task task:tasks)
        {
            Log.d(TAG, "task id is "+task.getTaskid());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }



        return list;
    }

}
