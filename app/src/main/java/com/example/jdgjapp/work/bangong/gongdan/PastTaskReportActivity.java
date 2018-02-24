package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.TaskMaterial;
import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class PastTaskReportActivity extends AppCompatActivity {

    private static final String TAG = "PastTaskReportActivity";
    private ListView lv;
    private TextView nodataTv;
    private String taskid;
    private String usemat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_task_report);
        lv = (ListView) findViewById(R.id.lv_task_report);
        nodataTv = (TextView)findViewById(R.id.tv_nodata);

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");

        TongbuTaskReport(taskid);

    }

    public List<Map<String, Object>> getData() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        List<TaskReport> taskReports = DataSupport
                .where("taskid = ?",taskid)
                .order("datetime desc")
                .find(TaskReport.class);

        for(TaskReport taskReport : taskReports)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", taskReport.getDatetime());
            map.put("info", taskReport.getContent());
            list.add(map);
        }
        return list;
    }

    private void TongbuTaskReport(final String workid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.post()
                            .addParams("workid",workid)
                            .url("http://106.14.145.208:80/JDGJ/BackAppOrderReportById")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d(TAG, "同步历史简报失败");
                                    showdatalist();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d(TAG, "同步历史简报成功"+response);
                                    parseJSON(response);
                                    showdatalist();
                                }
                            });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSON(String jsonData) {
        Gson gson = new Gson();
        List<TaskReport> taskReportList = gson.fromJson(jsonData, new TypeToken<List<TaskReport>>() {
        }.getType());
        for (TaskReport taskReport : taskReportList) {
            //解析材料字段
            String matjson = taskReport.getUsematerial();
            if ("[]".equals(matjson) || TextUtils.isEmpty(matjson)){
                usemat = "无";
            } else {
                StringBuilder mat = new StringBuilder();
                List<TaskMaterial> taskMaterialList = gson.fromJson(taskReport.getUsematerial(), new TypeToken<List<TaskMaterial>>() {
                }.getType());
                for (TaskMaterial taskMaterial : taskMaterialList){
                    mat.append(taskMaterial.getMat_name()).append(" * ").append(taskMaterial.getMat_num()).append("\n");
                }
                String usmat = mat.toString();
                usemat = usmat.substring(0,usmat.length()-1);
            }

            //查询本地数据库，如果没有则添加
            List<TaskReport> taskReports = DataSupport.where("taskid = ? and datetime = ?",taskid,taskReport.getDatetime()).find(TaskReport.class);
            if (taskReports.isEmpty()) {
                TaskReport taskReport1 = new TaskReport();
                taskReport1.setTaskid(taskid);
                taskReport1.setDatetime(taskReport.getDatetime());
                taskReport1.setContent(taskReport.getContent());
                taskReport1.setImgpath(taskReport.getImgpath());
                taskReport1.setUsematerial(usemat);
                taskReport1.save();
                Log.d(TAG, "已添加历史简报" + taskReport.getDatetime());
            }
        }

        //将本地数据与接收的数据对比，多的删除
        List<TaskReport> bdtaskreport = DataSupport.where("taskid = ?",taskid).find(TaskReport.class);
        for (TaskReport taskReport : bdtaskreport) {
            int num=0;
            for (TaskReport taskReport2 : taskReportList) {
                if(taskReport2.getDatetime().equals(taskReport.getDatetime())) num++;
            }
            if(num == 0) {
                DataSupport.deleteAll(TaskReport.class,"datetime = ?",taskReport.getDatetime());
                Log.d(TAG, "已删除历史简报" + taskReport.getDatetime());
            }
        }

    }

    private void showdatalist(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> list = getData();
                if (list.size()==0){
                    nodataTv.setText("暂无简报");
                }
                lv.setAdapter(new TaskReport_ListViewAdapter(PastTaskReportActivity.this, list));

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获取控件上的time
                        TextView titleTv = (TextView) view.findViewById(R.id.title);
                        String datetime = titleTv.getText().toString();
                        Intent intent = new Intent(PastTaskReportActivity.this,PastTaskReportInfoActivity.class);
                        intent.putExtra("datetime",datetime);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}
