package com.example.jdgjapp.work.kaoqin.daka;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.LoginActivity;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DaKaMain extends AppCompatActivity {

    private static final String TAG = "DaKaMain";
    private TextView nameTv;
    private TextView dateTv;
    private Button dakaBtn;
    private TextView time1Tv;  //上班打卡时间
    private TextView time2Tv;  //下班打卡时间
    private TextView addr1Tv;  //上班打卡位置
    private TextView addr2Tv;  //下班打卡位置

    public LocationClient nLocationClient;
    public static int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_ka_main);

        nameTv = (TextView)findViewById(R.id.tv_name);
        dateTv = (TextView)findViewById(R.id.tv_date);
        dakaBtn = (Button)findViewById(R.id.btn_daka);
        time1Tv = (TextView)findViewById(R.id.time_shangban);
        time2Tv = (TextView)findViewById(R.id.time_xiaban);
        addr1Tv = (TextView)findViewById(R.id.addr_shangban);
        addr2Tv = (TextView)findViewById(R.id.addr_xiaban);

        //显示姓名
        User user = DataSupport.findFirst(User.class);
        nameTv.setText(user.getUsr_name());
        //显示当前日期
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String date = format.format(calendar.getTime());
        dateTv.setText(date);

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        dakaBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_daka:
                    startLocation();
                    break;
            }
        }
    }

    private void startLocation(){
        nLocationClient = new LocationClient(getApplicationContext());
        nLocationClient.registerLocationListener(new MyLocationListener());

        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.
                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions , 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        nLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    Double position1 = location.getLatitude();
                    Double position2 = location.getLongitude();
                    String position3 = position1.toString();
                    String position4 = position2.toString();
                    addr1Tv.setText(position3);
                    addr2Tv.setText(position4);

                    //读取工号
                    User user = DataSupport.findFirst(User.class);
                    //读取当前时间
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String time = format.format(calendar.getTime());
                    //上传数据
                    //sendRequestPosition(user.getUsr_id(),position3,position4,time,"1");
                }
            });
        }
    }

    private void sendRequestPosition(final String id, final String Position1, final String Position2, final String datetime ,final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", id)
                                .add("posy", Position1)
                                .add("posx", Position2)
                                .add("datetime",datetime)
                                .add("type",type)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://106.14.145.208:8080//JDGJ/ReceiveUsrUpDown")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseDate = response.body().string();
                        //showResponse(responseDate);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d(TAG, "已发送");
    }

}
