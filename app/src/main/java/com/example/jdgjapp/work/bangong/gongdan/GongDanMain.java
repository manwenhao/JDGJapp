package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GongDanMain extends AppCompatActivity {
    private ListView listView;
    private LinearLayout over;
    private LinearLayout head;
    private View myhead;
    private View myover;
    private View view;
    private Button notaccept;
    private Button notstart;
    private Button doing;
    private Button done;
    private TextView nodata;
    private TextView date;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean flag=false;
    public static String status = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gong_dan_main);
        listView=(ListView)findViewById(R.id.task_listview);
        head=(LinearLayout)findViewById(R.id.task_listview_head);
        over=(LinearLayout)findViewById(R.id.task_listview_over);
        date=(TextView)head.findViewById(R.id.task_head_date);
        notaccept=(Button)over.findViewById(R.id.notaccept);
        notstart=(Button)over.findViewById(R.id.notstart);
        doing=(Button)over.findViewById(R.id.doing);
        done=(Button)over.findViewById(R.id.haddone);
        nodata = (TextView)findViewById(R.id.tv_nodata);

        initlist();
        initbutton();
        refreshButton();

    }
    public void initbutton(){

        notaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept.setTextColor(Color.RED);
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));

                List<Map<String, Object>> list = getData1();
                if (list==null || list.isEmpty()) nodata.setText("暂无工单");
                else nodata.setText("");
                listView.setAdapter(new Task_ListViewAdapter(MyApplication.getContext(), list));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的taskid
                        TextView idTv = (TextView) view.findViewById(R.id.title);
                        String taskid = idTv.getText().toString();

                        Intent intent = new Intent(MyApplication.getContext(),NotReceivedTaskInfoActivity.class);
                        intent.putExtra("taskid",taskid);
                        startActivity(intent);
                    }
                });


            }
        });

        notstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notstart.setTextColor(Color.RED);
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));

                List<Map<String, Object>> list = getDat2a();
                if (list==null || list.isEmpty()) nodata.setText("暂无工单");
                else nodata.setText("");

                listView.setAdapter(new Task_ListViewAdapter(MyApplication.getContext(), list));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的taskid
                        TextView idTv = (TextView) view.findViewById(R.id.title);
                        String taskid = idTv.getText().toString();

                        Intent intent = new Intent(GongDanMain.this,NotStartTaskInfoActivity.class);
                        intent.putExtra("taskid",taskid);
                        startActivity(intent);
                    }
                });

            }
        });

        doing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doing.setTextColor(Color.RED);
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getData3();
                if (list==null || list.isEmpty()) nodata.setText("暂无工单");
                else nodata.setText("");
                listView.setAdapter(new Task_ListViewAdapter(MyApplication.getContext(), list));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的taskid
                        TextView idTv = (TextView) view.findViewById(R.id.title);
                        String taskid = idTv.getText().toString();
                        Intent intent = new Intent(GongDanMain.this,OnGoingTaskInfoActivity.class);
                        intent.putExtra("taskid",taskid);
                        startActivity(intent);
                    }
                });

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(Color.RED);
                List<Map<String, Object>> list = getData4();
                if (list==null || list.isEmpty()) nodata.setText("暂无工单");
                else nodata.setText("");
                listView.setAdapter(new Task_ListViewAdapter(MyApplication.getContext(), list));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的taskid
                        TextView idTv = (TextView) view.findViewById(R.id.title);
                        String taskid = idTv.getText().toString();
                        Intent intent = new Intent(GongDanMain.this,DoneTaskInfoActivity.class);
                        intent.putExtra("taskid",taskid);
                        startActivity(intent);
                    }
                });

            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notaccept.performClick();
            }
        });
    }
    public void initlist(){

    }
    public List<Map<String, Object>> getData1() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=1待接收的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","1")
                .find(Task.class);
        if (tasks!=null && !tasks.isEmpty()){
            for(Task task:tasks)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", task.getTaskid());
                map.put("time", task.getCreatetime());
                map.put("info", task.getContent());
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getDat2a() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=0未开始的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","0")
                .find(Task.class);
        if (tasks!=null && !tasks.isEmpty()){
            for(Task task:tasks)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", task.getTaskid());
                map.put("time", task.getCreatetime());
                map.put("info", task.getContent());
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getData3() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=2进行中的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ? or status = ?","2","4")
                .find(Task.class);
        if (tasks!=null && !tasks.isEmpty()){
            for(Task task:tasks)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", task.getTaskid());
                map.put("time", task.getCreatetime());
                map.put("info", task.getContent());
                list.add(map);
            }
        }
        return list;
    }
    public List<Map<String, Object>> getData4() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=3已完成的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ? or status = ? or status = ?","3","5","6")
                .find(Task.class);
        if (tasks!=null && !tasks.isEmpty()){
            for(Task task:tasks)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", task.getTaskid());
                map.put("time", task.getCreatetime());
                map.put("info", task.getContent());
                list.add(map);
            }
        }
        return list;

    }

    private void refreshButton(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (status){
                    case "1":  //待接收
                        notaccept.performClick();
                        break;
                    case "0":  //未开始
                        notstart.performClick();
                        break;
                    case "2":  //进行中
                        doing.performClick();
                        break;
                    case "3":  //已完成
                        done.performClick();
                        break;
                    default:break;
                }
            }
        });

    }

}
