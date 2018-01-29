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

public class DoneTaskActivity extends AppCompatActivity {

    private static final String TAG = "DoneTaskActivity";
    private Button backBtn;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_task);
        backBtn = (Button) findViewById(R.id.btn_back);
        lv = (ListView) findViewById(R.id.lv_done_task);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoneTaskActivity.this, MainActivity.class);
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });
        Log.d(TAG, "卡顿11111111111111111111");
        List<Map<String, Object>> list = getData();
        lv.setAdapter(new Task_ListViewAdapter(DoneTaskActivity.this, list));
        Log.d(TAG, "卡顿22222222222222222222");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取控件上的taskid
                TextView idTv = (TextView) view.findViewById(R.id.title);
                String taskid = idTv.getText().toString();
                Intent intent = new Intent(DoneTaskActivity.this,DoneTaskInfoActivity.class);
                intent.putExtra("taskid",taskid);
                startActivity(intent);
            }
        });

    }


    public List<Map<String, Object>> getData() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Log.d(TAG, "卡顿3333333333333333333");
        //从数据库中查询status=3已完成的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","3")
                .find(Task.class);

        Log.d(TAG, "卡顿4444444444444444444");
        for(Task task:tasks)
        {
            Log.d(TAG, "task id is "+task.getTaskid());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }
        Log.d(TAG, "卡顿55555555555555555555");
        return list;

    }

}
