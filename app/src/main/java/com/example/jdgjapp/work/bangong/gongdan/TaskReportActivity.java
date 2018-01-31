package com.example.jdgjapp.work.bangong.gongdan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jdgjapp.R;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

public class TaskReportActivity extends AppCompatActivity {

    private static final String TAG = "TaskReportActivity";

    private String id;
    private String taskid;
    private ImageView imageView;
    private EditText despEt;
    private Button backBtn;
    private Button sendBtn;
    private Button selectImgBtn;
    private GridView gv;

    private List<String> paths;
    private String desp;
    private ProgressDialog pro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_report);
        sendBtn = (Button) findViewById(R.id.btn_send_task_report);
        backBtn = (Button) findViewById(R.id.btn_back);
        selectImgBtn = (Button) findViewById(R.id.btn_select_photo);
        despEt = (EditText) findViewById(R.id.et_task_desp);
        imageView = (ImageView) findViewById(R.id.image);
        gv = (GridView) findViewById(R.id.gridView);
        paths=new ArrayList<>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageConfig imageConfig = new ImageConfig.Builder(new GlideLoader())
                        .showCamera()
                        .build();
                ImageSelector.open(TaskReportActivity.this, imageConfig);   // 开启图片选择器
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TaskReportActivity.this);
                dialog.setTitle("确认发送简报？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //设置按钮点击事件
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //设置ProgressDialog
                        pro = new ProgressDialog(TaskReportActivity.this);
                        pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                        pro.setCancelable(false);// 设置是否可以通过点击Back键取消
                        pro.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                        pro.setMessage("正在发送简报...");
                        pro.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                            }

                        });
                        pro.show();

                        //发送简报
                        desp = despEt.getText().toString();
                        //读取工号
                        SharedPreferences pref = getSharedPreferences("userinfo",MODE_PRIVATE);
                        id = pref.getString("1","");
                        //接收传过来的工单号
                        Intent intent = getIntent();
                        taskid = intent.getStringExtra("taskid");
                        //将工单号、工号、简报文字拼接到一起
                        String info = taskid.concat(";").concat(id).concat(";").concat(desp);
                        Log.d(TAG, "拼接后的字符串为" + info);

                        Map<String,File> files=null;
                        if (paths.size()!=0){
                            files=new HashMap<>();
                            for(int x=0;x<paths.size();x++){
                                files.put(String.valueOf(x),new File(paths.get(x)));
                            }
                        }

                        FileUploadManager.setUrl("http://106.14.145.208:8080/KQ/ZWLUploadServlet");
                        FileUploadManager.setMylistener(new ReportListener() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("发送简报失败！",e.toString());
                                pro.cancel();
                                Toast.makeText(TaskReportActivity.this, "发送简报失败!"+";exception: "+e.toString(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onSuccess(String response, int id) {
                                String responseData = response.toString();
                                Log.d("发送简报成功！",responseData);
                                pro.cancel();
                                if (responseData.equals("ok")) {
                                    Toast.makeText(TaskReportActivity.this, "发送简报成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TaskReportActivity.this,response, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(TaskReportActivity.this, "发送简报失败！", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        FileUploadManager.uploadReport(files,info);


                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
    }


    //GridView适配器
    class GridViewAdapter extends BaseAdapter {
        private List<String> listUrls;
        private LayoutInflater inflater;

        public GridViewAdapter(List<String> listUrls) {
            this.listUrls = listUrls;
            inflater = LayoutInflater.from(getApplicationContext());
        }


        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public String getItem(int position) {
            return listUrls.get(position);
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
                holder = (ViewHolder)convertView.getTag();
            }
            if (listUrls!=null){
                Glide.with(getApplicationContext()).load(listUrls.get(position)).into(holder.image);
            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Get Image Path List
            paths = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            gv.setAdapter(new GridViewAdapter(paths));
            for (String path : paths) {
                Log.i("ImagePathList", path);
            }

        }
    }

    public class GlideLoader implements com.yancy.imageselector.ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }

    }


}
