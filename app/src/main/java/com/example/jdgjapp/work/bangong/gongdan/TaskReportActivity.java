package com.example.jdgjapp.work.bangong.gongdan;

import android.app.ProgressDialog;
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
import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class TaskReportActivity extends AppCompatActivity {

    private static final String TAG = "TaskReportActivity";
    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private String id;
    private String taskid;
    private ImageView imageView;
    private EditText despEt;
    private Button backBtn;
    private Button sendBtn;
    private GridView gv;
    List<String> paths;
    private String desp;

    ProgressDialog pro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_report);
        sendBtn = (Button) findViewById(R.id.btn_send_task_report);
        backBtn = (Button) findViewById(R.id.btn_back);
        despEt = (EditText) findViewById(R.id.et_task_desp);
        imageView = (ImageView) findViewById(R.id.image);
        gv = (GridView) findViewById(R.id.gridView);
        paths=new ArrayList<>();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TaskReportActivity.this,OnGoingTaskActivity.class);
//                startActivity(intent);
                finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showContent(data);
            gv.setAdapter(new GridViewAdapter(paths));
            return;
        }
    }

    private void showContent(Intent data) {
        if (ImageSelector.getImagePaths(data)==null)
            return;
        paths =  ImageSelector.getImagePaths(data);
        if (paths.isEmpty()) {
            return;
        }
        Log.d(TAG, "已找到所选图片路径");

    }


    public void selectImg(View v) {
        ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
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

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


}
