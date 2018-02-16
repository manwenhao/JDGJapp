package com.example.jdgjapp.work.bangong.baoxiao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.GridViewForScrollView;
import com.example.jdgjapp.work.bangong.gongdan.FileUploadManager;
import com.example.jdgjapp.work.bangong.gongdan.ReportListener;
import com.example.jdgjapp.work.bangong.gongdan.TaskReportActivity;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;

public class BXApply extends AppCompatActivity {
    private EditText money,content;
    private Button select_img,start;
    private Spinner type;
    private GridViewForScrollView scrollView;
    private static final int REQUEST_LIST_CODE = 0;
    private List<String> paths;
    private ProgressDialog pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxapply);
        money=(EditText)findViewById(R.id.bx_whole_money);
        content=(EditText)findViewById(R.id.bx_reason);
        select_img=(Button)findViewById(R.id.bx_btn_select_photo);
        start=(Button)findViewById(R.id.bx_apply_start);
        type=(Spinner)findViewById(R.id.bx_apply_type);
        scrollView=(GridViewForScrollView)findViewById(R.id.bx_gridView);
        paths=new ArrayList<>();
        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImg(view);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moneystring=money.getText().toString();
                String conentstring=content.getText().toString();
                String typestring=type.getSelectedItem().toString();
                String userid= MyApplication.getid();
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=sf.format(new Date());
                String id="bx"+MyApplication.getid()+time;
                if (TextUtils.isEmpty(moneystring)||TextUtils.isEmpty(conentstring)){
                    Toast.makeText(BXApply.this, "请输入文字！", Toast.LENGTH_SHORT).show();
                }else if (!isNUmber(moneystring)){
                    Toast.makeText(BXApply.this, "报销金额请输入数字！", Toast.LENGTH_SHORT).show();
                }else if (paths.size()==0){
                    Toast.makeText(BXApply.this, "请选择图片作为报销凭证！", Toast.LENGTH_SHORT).show();
                }else {
                    pro = new ProgressDialog(BXApply.this);
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
                    String info=id.replaceAll(" ","")+"#&*"+userid+"#&*"+typestring+"#&*"+conentstring+"#&*"+moneystring;
                    Log.d("申请报销info",info);
                    Map<String,File> files=null;
                    if (paths.size()!=0){
                        files=new HashMap<>();
                        for(int x=0;x<paths.size();x++){
                            files.put(String.valueOf(x),new File(paths.get(x)));
                        }
                    }
                    FileUploadManager.setUrl("http://106.14.145.208:8080/JDGJ/UploadForAcout");
                    FileUploadManager.setMylistener(new ReportListener() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("报销申请",e.toString());
                            pro.cancel();
                            Toast.makeText(BXApply.this, "报销失败!"+";exception: "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String response, int id) {
                            Log.d("报销申请",response);
                            pro.cancel();
                            if (response.equals("ok")){
                                Toast.makeText(BXApply.this, "报销申请成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(BXApply.this, "报销申请失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    FileUploadManager.uploadReport(files,info);
                }


            }
        });



    }
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
    public void selectImg(View view) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(true)
                .titleBgColor(Color.parseColor("#0F0F0F"))
                .maxNum(9)
                .build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            paths = data.getStringArrayListExtra("result");
            for (String path : paths){
                Log.i("简报图片", path);
            }
            scrollView.setAdapter(new GridViewAdapter(paths));

        }
    }
    public boolean isNUmber(String str){
        Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
