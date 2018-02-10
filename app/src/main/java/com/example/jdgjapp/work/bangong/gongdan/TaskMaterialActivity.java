package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.TaskMaterial;
import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.NumberAddSubView;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class TaskMaterialActivity extends AppCompatActivity {

    private static final String TAG = "TaskMaterialActivity";
    public static final String action = "taskmat.action";
    private ListView lv;
    private Button ok;
    private TextView warnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_material);

        lv = (ListView) findViewById(R.id.lv_material);
        ok = (Button) findViewById(R.id.btn_ok);
        warnTv = (TextView) findViewById(R.id.tv_nodata);

        sendMaterialRequest(ReturnUsrDep.returnUsr().getUsr_deptId());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = new JSONArray();
                StringBuilder mat = new StringBuilder();

                for (int i=0;i<lv.getCount();i++){
                    RelativeLayout relativeLayout = (RelativeLayout)lv.getChildAt(i);
                    CheckBox checkBox = (CheckBox)relativeLayout.findViewById(R.id.check);
                    NumberAddSubView numberAddSubView = (NumberAddSubView)relativeLayout.findViewById(R.id.num);
                    Boolean ischeck = checkBox.isChecked();
                    String name = checkBox.getText().toString();
                    String num = numberAddSubView.getValue();

                    if(ischeck){
                        try {
                            List<TaskMaterial> list = DataSupport.where("mat_name = ?",name).find(TaskMaterial.class);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("mat_id",list.get(0).getMat_id());
                            jsonObject.put("mat_num",num);
                            jsonArray.put(jsonObject);

                            mat.append(name).append(" * ").append(num).append("\n");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
                String json = jsonArray.toString();
                Log.d(TAG, "已选工单材料json"+json);

                TaskReportActivity.mat_list_json = json;
                String mymat = mat.toString();
                String myymat = mymat.substring(0,mymat.length()-1);
                TaskReportActivity.mat_list = myymat;
                //通知更新已选材料
                Intent intent = new Intent(action);
                sendBroadcast(intent);
                finish();
            }
        });

    }


    private void sendMaterialRequest(final String DeptId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpUtils.post()
                            .url("http://106.14.145.208:8080/JDGJ/BackDeptMaterialUsesTotal")
                            .addParams("dep_id",DeptId)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    DataSupport.deleteAll(TaskMaterial.class);
                                    Log.d(TAG, "工单材料加载失败"+e.toString());
                                    showWarning("工单材料加载失败");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.d(TAG, "工单材料加载成功"+response);
                                    if ("[]".equals(response) || TextUtils.isEmpty(response)){
                                        showWarning("暂无材料");
                                    }else {
                                    Gson gson = new Gson();
                                    List<TaskMaterial> taskMaterialList = gson.fromJson(response, new TypeToken<List<TaskMaterial>>(){}.getType());
                                    DataSupport.deleteAll(TaskMaterial.class);
                                    for (TaskMaterial taskMaterial : taskMaterialList){
                                        Log.d(TAG, "工单材料mat_id   " + taskMaterial.getMat_id());
                                        Log.d(TAG, "工单材料mat_name " + taskMaterial.getMat_name());
                                        Log.d(TAG, "工单材料mat_num  " + taskMaterial.getMat_num());
                                        Log.d(TAG, "工单材料********************");
                                        //更新材料数据库
                                        TaskMaterial taskMaterial0 = new TaskMaterial();
                                        taskMaterial0.setMat_id(taskMaterial.getMat_id());
                                        taskMaterial0.setMat_name(taskMaterial.getMat_name());
                                        taskMaterial0.setMat_num(taskMaterial.getMat_num());
                                        taskMaterial0.save();
                                    }
                                    showMaterial();
                                    }
                                }
                            });


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<Map<String, Object>> getData() {
        //添加数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        List<TaskMaterial> taskMaterialList = DataSupport.findAll(TaskMaterial.class);

        for (int i=0; i<taskMaterialList.size(); i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", taskMaterialList.get(i).getMat_name());
            map.put("num",Integer.parseInt(taskMaterialList.get(i).getMat_num()));
            list.add(map);
        }
        return list;
    }

    private void showWarning(final String word){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                warnTv.setVisibility(View.VISIBLE);
                ok.setVisibility(View.INVISIBLE);
                warnTv.setText(word);
            }
        });
    }

    private void showMaterial(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> list = getData();
                final TaskMaterial_ListViewAdapter adapter = new TaskMaterial_ListViewAdapter(TaskMaterialActivity.this,list);
                lv.setAdapter(adapter);
            }
        });
    }

}
