package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PastTaskReportInfoActivity extends AppCompatActivity {

    private static final String TAG = "PastTaskReportInfoActiv";
    private Button backBtn;
    private TextView taskidTv;
    private TextView sendtimeTv;
    private TextView contentTv;
    private String sendtime;
    private GridView gv;
    private String imgpath; //存放服务器发过来的url
    private String a[]; //存放每个图片真正的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_task_report_info);
        backBtn = (Button) findViewById(R.id.btn_back);
        taskidTv = (TextView) findViewById(R.id.past_task_taskid);
        sendtimeTv = (TextView) findViewById(R.id.past_task_sendtime);
        contentTv = (TextView) findViewById(R.id.past_task_content);
        gv = (GridView) findViewById(R.id.gridView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PastTaskReportInfoActivity.this,PastTaskReportActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //获取传递过来的sendtime
        Intent intent = getIntent();
        sendtime = intent.getStringExtra("sendtime");
        Log.d(TAG, "传递过来的sendtime = " + sendtime);

        //根据sendtime查询数据库数据并显示
        List<TaskReport> taskReports = DataSupport.where("sendtime = ?",sendtime).find(TaskReport.class);

        for (TaskReport taskReport : taskReports) {
            taskidTv.setText(taskReport.getTaskid());
            sendtimeTv.setText(taskReport.getSendtime());
            contentTv.setText(taskReport.getContent());
            imgpath = taskReport.getPicpath();
        }

        //处理图片url
        if (imgpath == null || imgpath.isEmpty()){
            Log.d(TAG, "该简报没有图片信息！");
        }else
        {
            //将图片url分割后放入a[]
            a = imgpath.split(";");
            for (int i = 0; i < a.length; i++) {
                String http = "http://106.14.145.208:8080";
                a[i] = http + a[i];
                Log.d(TAG, "图片url【" +i+ "】=" + a[i]);
            }
            gv.setAdapter(new PastTaskReportInfoActivity.GridViewAdapter(a));

        }




    }


    //GridView适配器
    class GridViewAdapter extends BaseAdapter {
        private String listUrls[];
        private LayoutInflater inflater;

        public GridViewAdapter(String listUrls[]) {
            this.listUrls = listUrls;
            inflater = LayoutInflater.from(getApplicationContext());
        }

        public int getCount(){
            return listUrls.length;
        }
        @Override
        public String getItem(int position) {
            return listUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                //convertView=inflater.inflate(R.layout.item)
                convertView = inflater.inflate(R.layout.selectitem_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (PastTaskReportInfoActivity.GridViewAdapter.ViewHolder)convertView.getTag();
            }
            if (listUrls!=null){

                Glide.with(getApplicationContext())
                        .load(listUrls[position])
                        .crossFade()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .animate(android.R.anim.slide_in_left)
                        .error(R.drawable.imgerror)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);

            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }


}
