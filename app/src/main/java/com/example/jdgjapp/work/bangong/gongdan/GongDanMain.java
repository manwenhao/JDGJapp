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
    private Button notaccept2;
    private Button notstart2;
    private Button doing2;
    private Button done2;
    private TextView date;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gong_dan_main);
        listView=(ListView)findViewById(R.id.task_listview);
        head=(LinearLayout)findViewById(R.id.task_listview_head);
        over=(LinearLayout)findViewById(R.id.task_listview_over);
        myhead=(View)this.getLayoutInflater().inflate(R.layout.task_listhead,listView,false);
        date=(TextView)myhead.findViewById(R.id.task_head_date);
        listView.addHeaderView(myhead);
        myover=(View)this.getLayoutInflater().inflate(R.layout.task_list_over,listView,false);
        notaccept=(Button)myover.findViewById(R.id.notaccept);
        notstart=(Button)myover.findViewById(R.id.notstart);
        doing=(Button)myover.findViewById(R.id.doing);
        done=(Button)myover.findViewById(R.id.haddone);
        listView.addHeaderView(myover);
        notaccept2=(Button)over.findViewById(R.id.notaccept);
        notstart2=(Button)over.findViewById(R.id.notstart);
        doing2=(Button)over.findViewById(R.id.doing);
        done2=(Button)over.findViewById(R.id.haddone);
        initlist();
        initbutton();

    }
    public void initbutton(){
        notaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept.setTextColor(Color.RED);
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));
                notaccept2.setTextColor(Color.RED);
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getData1();
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
        notaccept2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept.setTextColor(Color.RED);
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));
                notaccept2.setTextColor(Color.RED);
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getData1();
                listView.setAdapter(new Task_ListViewAdapter(MyApplication.getContext(), list));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的taskid
                        TextView idTv = (TextView) view.findViewById(R.id.title);
                        String taskid = idTv.getText().toString();

                        Intent intent = new Intent(GongDanMain.this,NotReceivedTaskInfoActivity.class);
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
                notstart2.setTextColor(Color.RED);
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getDat2a();
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
        notstart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notstart.setTextColor(Color.RED);
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart2.setTextColor(Color.RED);
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getDat2a();
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
                doing2.setTextColor(Color.RED);
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getData3();
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
        doing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doing.setTextColor(Color.RED);
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(Color.RED);
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(getResources().getColor(R.color.taskbutton));
                List<Map<String, Object>> list = getData3();
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
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(Color.RED);
                List<Map<String, Object>> list = getData4();
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
        done2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notaccept.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart.setTextColor(getResources().getColor(R.color.taskbutton));
                doing.setTextColor(getResources().getColor(R.color.taskbutton));
                done.setTextColor(Color.RED);
                notaccept2.setTextColor(getResources().getColor(R.color.taskbutton));
                notstart2.setTextColor(getResources().getColor(R.color.taskbutton));
                doing2.setTextColor(getResources().getColor(R.color.taskbutton));
                done2.setTextColor(Color.RED);
                List<Map<String, Object>> list = getData4();
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem>=1){
                    over.setVisibility(View.VISIBLE);
                    flag=true;
                }else {
                    flag=false;
                    over.setVisibility(View.GONE);
                }
                if (firstVisibleItem==0&&head.getVisibility()==View.VISIBLE){
                    over.setVisibility(View.VISIBLE);
                }


            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mFirstY=event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY=event.getY();
                        if (mCurrentY-mFirstY>0){//手下滑
                            direction=0;
                        }else if (mCurrentY-mFirstY<0) {
                            direction = 1;//手上滑
                        }
                        if (direction==0){
                            head.setVisibility(View.VISIBLE);
                        }else if (direction==1&&head.getVisibility()==View.VISIBLE){
                            head.setVisibility(View.GONE);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }
    public List<Map<String, Object>> getData1() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=0未接收的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","0")
                .find(Task.class);

        for(Task task:tasks)
        {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }

        return list;
    }
    public List<Map<String, Object>> getDat2a() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=1未开始的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","1")
                .find(Task.class);

        for(Task task:tasks)
        {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }



        return list;
    }
    public List<Map<String, Object>> getData3() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=2进行中的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","2")
                .find(Task.class);

        for(Task task:tasks)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }
        return list;
    }
    public List<Map<String, Object>> getData4() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //从数据库中查询status=3已完成的数据
        List<Task> tasks = DataSupport.select("taskid","createtime","content")
                .where("status = ?","3")
                .find(Task.class);

        for(Task task:tasks)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", task.getTaskid());
            map.put("time", task.getCreatetime());
            map.put("info", task.getContent());
            list.add(map);
        }
        return list;

    }
}
