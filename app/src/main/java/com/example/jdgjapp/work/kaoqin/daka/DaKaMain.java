package com.example.jdgjapp.work.kaoqin.daka;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.LoginActivity;
import com.example.jdgjapp.MainActivity;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private Object  objLock = new Object();
    MyLocationListener mylisten = null;

    public static int flag;
    public static String currentaddr;
    public static final int UPDATE_BUTTON_shangban = 1;
    public static final int UPDATE_BUTTON_xiaban = 2;
    public static final int ADD_INFO_shangban = 3;
    public static final int ADD_INFO_xiaban = 4;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentdate = format.format(calendar.getTime());
        dateTv.setText(currentdate);

        //更新打卡按钮
        SharedPreferences pref = getSharedPreferences("flag",MODE_PRIVATE);
        flag  = pref.getInt("flag",0);
        if (flag == 1)
            dakaBtn.setText("下班打卡");
        else dakaBtn.setText("上班打卡");

        //更新打卡具体信息 新的一天清空
        SharedPreferences pref0 = getSharedPreferences("date",MODE_PRIVATE);
        String date = pref0.getString("date","");
        Log.d(TAG, "date:"+date);
        SharedPreferences pref1 = getSharedPreferences("shangban",MODE_PRIVATE);
        String sbtime = pref1.getString("time","");
        String sbaddr = pref1.getString("address","");
        SharedPreferences pref2 = getSharedPreferences("xiaban",MODE_PRIVATE);
        String xbtime = pref2.getString("time","");
        String xbaddr = pref2.getString("address","");
        if (currentdate.equals(date)){
            time1Tv.setText(sbtime);
            addr1Tv.setText(sbaddr);
            time2Tv.setText(xbtime);
            addr2Tv.setText(xbaddr);
        }else {
            dakaBtn.setText("上班打卡");
        }

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
                    //读取当前flag
                    SharedPreferences pref = getSharedPreferences("flag",MODE_PRIVATE);
                    flag  = pref.getInt("flag",0);

                    //读取当前日期
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String currentdate = format.format(calendar.getTime());
                    //读取本地存储的日期
                    SharedPreferences pref0 = getSharedPreferences("date",MODE_PRIVATE);
                    String date = pref0.getString("date","");

                    if (!currentdate.equals(date)){
                        //清空之前的数据
                        time1Tv.setText("");
                        addr1Tv.setText("");
                        time2Tv.setText("");
                        addr2Tv.setText("");
                        flag = 0;
                        SharedPreferences.Editor editor = getSharedPreferences("flag",
                                MODE_PRIVATE).edit();
                        editor.putInt("flag",0);
                        editor.apply();
                        //发送定位
                        startLocation();
                    }else {
                        if (flag==2) showResponse("请勿重复打卡！");
                        else startLocation();
                    }

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
        initLocation();
        nLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        nLocationClient.setLocOption(option);
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
                    Log.d(TAG, "打卡定位Listener开启");
                    //获取经纬度
                    Double position1 = location.getLatitude();
                    Double position2 = location.getLongitude();
                    String position3 = position1.toString();
                    String position4 = position2.toString();
                    //校验地址
                    boolean iscontain1 = position3.contains("E");
                    boolean iscontain2 = position4.contains("E");

                    Log.d(TAG, "打卡定位经度" + position4);
                    Log.d(TAG, "打卡定位纬度" + position3);

                    //获取具体地址
                    currentaddr = location.getAddrStr();

                    //读取工号
                    User user = DataSupport.findFirst(User.class);
                    //读取当前时间
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String currenttime = format.format(calendar.getTime());
                    //上传数据
                    if (!iscontain1&&!iscontain2) {
                        if (flag == 0) {   //上班打卡
                            sendRequestPosition(user.getUsr_id(), position3, position4, currenttime, "1");
                        } else if (flag == 1) {  //下班打卡
                            sendRequestPosition(user.getUsr_id(), position3, position4, currenttime, "2");
                        }
                    }else showResponse("无法定位，打卡失败！");

                    //关闭定位
                    stop();
                    unregisterListener(mylisten);

                }
            });
        }
    }

    private void sendRequestPosition(final String id, final String Position1, final String Position2, final String datetime, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", id)
                                .add("posy", Position1)
                                .add("posx", Position2)
                                .add("datetime", datetime)
                                .add("type", type)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://106.14.145.208:8080//JDGJ/ReceiveUsrUpDown")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseDate = response.body().string();
                        //showResponse(responseDate);

                        if (responseDate.equals("ok")){
                            if (type.equals("1")){        //上班打卡成功
                                //更新按钮上的内容
                                Message message = new Message();
                                message.what = UPDATE_BUTTON_xiaban;
                                handler.sendMessage(message);
                                //显示打卡信息
                                Message message1 = new Message();
                                message1.what = ADD_INFO_shangban;
                                handler.sendMessage(message1);

                                //开启实时定位服务
                                Intent startIntent = new Intent(DaKaMain.this,DingWeiService.class);
                                startService(startIntent);

                                showResponse("上班打卡成功！");
                                //flag=1
                                SharedPreferences.Editor editor = getSharedPreferences("flag",
                                        MODE_PRIVATE).edit();
                                editor.putInt("flag",1);
                                editor.apply();

                            }else if (type.equals("2")){  //下班打卡成功
                                //更新按钮上的内容
                                Message message = new Message();
                                message.what = UPDATE_BUTTON_shangban;
                                handler.sendMessage(message);
                                //显示打卡信息
                                Message message2 = new Message();
                                message2.what = ADD_INFO_xiaban;
                                handler.sendMessage(message2);

                                //关闭实时定位服务
                                Intent stopIntent = new Intent(DaKaMain.this,DingWeiService.class);
                                stopService(stopIntent);

                                showResponse("下班打卡成功！");
                                //flag=2
                                SharedPreferences.Editor editor = getSharedPreferences("flag",
                                        MODE_PRIVATE).edit();
                                editor.putInt("flag",2);
                                editor.apply();
                            }
                        }else {
                            if (type.equals("1")){
                                showResponse("上班打卡失败！");
                            }else if(type.equals("2")){
                                showResponse("下班打卡失败！");
                            }
                        }

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    //读取当前日期并保存
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(calendar.getTime());
                    SharedPreferences.Editor editor = getSharedPreferences("date",
                            MODE_PRIVATE).edit();
                    editor.putString("date",date);
                    editor.apply();
                }
            }
        }).start();
        Log.d(TAG, "已发送");
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DaKaMain.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void stop(){
        synchronized (objLock) {
            if(nLocationClient != null && nLocationClient.isStarted()){
                nLocationClient.stop();
                Log.d(TAG, "打卡定位Listener关闭");
            }
        }
    }

    public void unregisterListener(MyLocationListener listener){
        if(listener != null){
            nLocationClient.unRegisterLocationListener(listener);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //读取当前时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            String time = format.format(calendar.getTime());

            switch (msg.what){
                case UPDATE_BUTTON_shangban:
                    dakaBtn.setText("上班打卡");
                    break;
                case UPDATE_BUTTON_xiaban:
                    dakaBtn.setText("下班打卡");
                    break;
                case ADD_INFO_shangban:
                    //清空之前的数据
                    time1Tv.setText("");
                    addr1Tv.setText("");
                    time2Tv.setText("");
                    addr2Tv.setText("");
                    String current1 = "上班打卡时间";
                    String currenttime1 = current1.concat(time);
                    time1Tv.setText(currenttime1);
                    addr1Tv.setText(currentaddr);
                    //保存打卡数据
                    SharedPreferences.Editor editor1 = getSharedPreferences("shangban",
                            MODE_PRIVATE).edit();
                    editor1.putString("time",currenttime1);
                    editor1.putString("address",currentaddr);
                    editor1.apply();
                    break;
                case ADD_INFO_xiaban:
                    String current2 = "下班打卡时间";
                    String currenttime2 = current2.concat(time);
                    time2Tv.setText(currenttime2);
                    addr2Tv.setText(currentaddr);
                    //保存打卡数据
                    SharedPreferences.Editor editor2 = getSharedPreferences("xiaban",
                            MODE_PRIVATE).edit();
                    editor2.putString("time",currenttime2);
                    editor2.putString("address",currentaddr);
                    editor2.apply();
                    break;

                default:
                    break;
            }
        }
    };



}
