package com.example.jdgjapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.gongdan.OnGoingTaskInfoActivity;
import com.example.jdgjapp.work.bangong.gongdan.TaskReportActivity;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jdgjapp.MyApplication.context;

public class SetIconActivity extends AppCompatActivity {

    private static final String TAG = "SetIconActivity";
    private ImageView iconIv;
    private Button resetBtn;
    private static final int REQUEST_LIST_CODE = 0;
    private static String responseData;
    private static String time; //修改头像的时间
    public static final String action = "jason.broadcast.action";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_icon);

        iconIv = (ImageView) findViewById(R.id.iv_myicon);
        resetBtn = (Button) findViewById(R.id.btn_reset_icon);

        //设置图片宽高
        int width = iconIv.getContext().getResources().getDisplayMetrics().widthPixels;
        int height = width;
        iconIv.setLayoutParams(new LinearLayout.LayoutParams(width , height));

        refreshIcon();

        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iconselect(view);
            }
        });

    }

    public void Iconselect(View view) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(false)
                .titleBgColor(Color.parseColor("#0F0F0F"))
                .cropSize(1, 1, 500, 500)
                .needCrop(true)
                .build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String path = pathList.get(0);
            Log.i("ImagePathList", path);

            sendIconRequest(ReturnUsrDep.returnUsr().getUsr_id(),path);
        }

    }


    private void sendIconRequest(final String id, final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient mOkHttpClent = new OkHttpClient();
                    File file = new File(path);
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_id",id)
                            .addFormDataPart("icon", "icon.jpg",
                                    RequestBody.create(MediaType.parse("image/jpg"), file));

                    RequestBody requestBody = builder.build();

                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:80//JDGJ/UploadServlett")
                            .post(requestBody)
                            .build();
                    Call call = mOkHttpClent.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "头像上传失败"+e );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SetIconActivity.this, "头像上传失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            Log.e(TAG, "头像上传成功"+response);
                            responseData = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (responseData.equals("ok")){
                                        Toast.makeText(SetIconActivity.this, "头像上传成功！", Toast.LENGTH_SHORT).show();
                                        //保存头像路径到本地
                                        User user = new User();
                                        user.setTouxiang(path);
                                        user.updateAll("usr_id = ?",ReturnUsrDep.returnUsr().getUsr_id());

                                        //保存更改时间作为签名
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        time = format.format(calendar.getTime());
                                        SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                                MODE_PRIVATE).edit();
                                        editor.putString("icontime",time);
                                        editor.apply();

                                        //刷新头像
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.with(MyApplication.getContext())
                                                        .load(path)
                                                        .error(R.drawable.me_pic)
                                                        .signature(new StringSignature(time))
                                                        .into(iconIv);
                                            }
                                        });

                                        //通知主页面刷新头像
                                        Intent intent = new Intent(action);
                                        sendBroadcast(intent);

                                    }
                                    else
                                        Toast.makeText(SetIconActivity.this, "头像上传失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void refreshIcon() {

        //读取存放的头像路径
        String iconaddr = ReturnUsrDep.returnUsr().getTouxiang();
        //更新签名
        SharedPreferences pref = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
        String qm = pref.getString("icontime","");
        if (TextUtils.isEmpty(iconaddr)){
            iconIv.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.me_pic));
        }else {
            Glide.with(this)
                    .load(iconaddr)
                    .error(R.drawable.me_pic)
                    .signature(new StringSignature(qm))
                    .into(iconIv);
        }

    }


}
