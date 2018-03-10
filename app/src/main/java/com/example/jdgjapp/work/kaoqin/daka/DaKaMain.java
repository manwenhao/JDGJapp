package com.example.jdgjapp.work.kaoqin.daka;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
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
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.gongdan.TaskReportActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
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
    public static final int ADD_INFO_siaban_old = 5;
    public static final int ADD_INFO_sxban_old = 6;
    private String str[];
    ProgressDialog pro;

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
        nameTv.setText(ReturnUsrDep.returnUsr().getUsr_name());
        //显示当前日期
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentdate = format.format(calendar.getTime());
        dateTv.setText(currentdate);

        //更新打卡按钮
        SharedPreferences pref = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
        flag  = pref.getInt("dkflag",0);
        if (flag == 1)
            dakaBtn.setText("下班打卡");
        else dakaBtn.setText("上班打卡");

        //更新打卡具体信息 新的一天清空
        String date = pref.getString("date","");
        String sbtime = pref.getString("sbtime","");
        String sbaddr = pref.getString("sbaddr","");
        String xbtime = pref.getString("xbtime","");
        String xbaddr = pref.getString("xbaddr","");
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.d(TAG, "已执行自动下班打卡");
                    //读取当前flag
                    SharedPreferences pref = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
                    flag  = pref.getInt("dkflag",0);
                    if (flag == 1){
                        startLocation();
                    }
                    break;
            }
        }
    };

    /**
     * string类型时间转换为date
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        dakaBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            //设置ProgressDialog
            pro = new ProgressDialog(DaKaMain.this);
            pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            pro.setCancelable(true);// 设置是否可以通过点击Back键取消
            pro.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            pro.setMessage("打卡中，请稍后...");
            pro.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }

            });
            pro.show();
            switch (view.getId()){
                case R.id.btn_daka:
                    //读取当前flag
                    SharedPreferences pref = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
                    flag  = pref.getInt("dkflag",0);
                    //读取上一次点击打卡的日期
                    String date = pref.getString("date","");

                    //读取当前日期
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String currentdate = format.format(calendar.getTime());

                    if (!currentdate.equals(date)){
                        //清空之前的数据
                        time1Tv.setText("");
                        addr1Tv.setText("");
                        time2Tv.setText("");
                        addr2Tv.setText("");
                        flag = 0;
                        SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                MODE_PRIVATE).edit();
                        editor.putInt("dkflag",0);
                        editor.apply();
                        //发送定位
                        startLocation();
                    }else {
                        if (flag==2) {
                            pro.cancel();
                            showResponse("请勿重复打卡！");
                        } else {
                            startLocation();
                        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.
                permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
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
        option.setCoorType("bd09ll");
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

                    //关闭定位
                    stop();
                    unregisterListener(mylisten);

                    //读取当前时间
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String currenttime = format.format(calendar.getTime());
                    //上传数据
                    if (!iscontain1&&!iscontain2) {
                        if (flag == 0) {   //上班打卡
                            sendRequestPosition(ReturnUsrDep.returnUsr().getUsr_id(), position3, position4, currenttime, "1");
                        } else if (flag == 1) {  //下班打卡
                            sendRequestPosition(ReturnUsrDep.returnUsr().getUsr_id(), position3, position4, currenttime, "2");
                        }
                    }else{
                        pro.cancel();
                        showResponse("无法定位，打卡失败！");
                    }

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
                                .url("http://106.14.145.208:80//JDGJ/ReceiveUsrUpDown")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseDate = response.body().string();
                        Log.d(TAG, "dakaold"+responseDate);

                        if (responseDate.equals("ok")){
                            if (type.equals("1")){        //上班打卡成功
                                //更新按钮上的内容
                                updateUI(UPDATE_BUTTON_xiaban);
                                //显示打卡信息
                                updateUI(ADD_INFO_shangban);

                                //开启实时定位服务
                                Intent startIntent = new Intent(DaKaMain.this,DingWeiService.class);
                                startService(startIntent);

                                showResponse("上班打卡成功！");
                                //flag=1
                                SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                        MODE_PRIVATE).edit();
                                editor.putInt("dkflag",1);
                                editor.apply();

                                //获取今日下班时间
                                getXBtime();

                            }else if (type.equals("2")){  //下班打卡成功
                                //更新按钮上的内容
                                updateUI(UPDATE_BUTTON_shangban);
                                //显示打卡信息
                                updateUI(ADD_INFO_xiaban);

                                //关闭实时定位服务
                                Intent stopIntent = new Intent(DaKaMain.this,DingWeiService.class);
                                stopService(stopIntent);

                                showResponse("下班打卡成功！");
                                //flag=2
                                SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                        MODE_PRIVATE).edit();
                                editor.putInt("dkflag",2);
                                editor.apply();
                            }
                        }else if(responseDate.equals("error") || responseDate.equals("[]") || TextUtils.isEmpty(responseDate)){
                            if (type.equals("1")){
                                showResponse("上班打卡失败！");
                            }else if(type.equals("2")){
                                showResponse("下班打卡失败！");
                            }
                        }else {   //打过卡无缓存
                            str = responseDate.split("[;]");
                            if (str.length==2){  //上班打卡已完成
                                //更新按钮上的内容
                                updateUI(UPDATE_BUTTON_xiaban);
                                //显示已打完的上班信息
                                updateUI(ADD_INFO_siaban_old);
                                //flag=1
                                SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                        MODE_PRIVATE).edit();
                                editor.putInt("dkflag",1);
                                editor.apply();
                                showResponse("更新打卡成功，请继续打卡！");
                            }else if (str.length==4){  //上下班打卡均完成
                                //更新按钮上的内容
                                updateUI(UPDATE_BUTTON_shangban);
                                //显示已打完的上下班信息
                                updateUI(ADD_INFO_sxban_old);
                                //flag=2
                                SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                                        MODE_PRIVATE).edit();
                                editor.putInt("dkflag",2);
                                editor.apply();
                                showResponse("更新打卡成功！");
                            }
                        }


                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    pro.cancel();
                    //读取当前日期并保存
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(calendar.getTime());
                    SharedPreferences.Editor editor = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                            MODE_PRIVATE).edit();
                    editor.putString("date",date);
                    editor.apply();
                }
            }
        }).start();
        Log.d(TAG, "*********打卡定位已发送*********");
    }

    private void getXBtime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208/JDGJ/BackAppTimeForDown")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(TAG, "获取下班时间失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "获取下班时间成功"+response);
                                if (response!=null){
                                    //到下班时间自动下班打卡
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.sendEmptyMessage(1);
                                        }
                                    };
                                    Timer timer = new Timer(true);
                                    timer.schedule(task,strToDateLong(response));
                                }
                            }
                        });
            }
        }).start();
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

    private void updateUI(final int type){

    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            //读取当前时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            String time = format.format(calendar.getTime());
            switch (type){
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
                    SharedPreferences.Editor editor1 = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                            MODE_PRIVATE).edit();
                    editor1.putString("sbtime",currenttime1);
                    editor1.putString("sbaddr",currentaddr);
                    editor1.apply();
                    break;
                case ADD_INFO_xiaban:
                    String current2 = "下班打卡时间";
                    String currenttime2 = current2.concat(time);
                    time2Tv.setText(currenttime2);
                    addr2Tv.setText(currentaddr);
                    //保存打卡数据
                    SharedPreferences.Editor editor2 = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                            MODE_PRIVATE).edit();
                    editor2.putString("xbtime",currenttime2);
                    editor2.putString("xbaddr",currentaddr);
                    editor2.apply();
                    break;
                case ADD_INFO_siaban_old:
                    String current3 = "上班打卡时间";
                    String currenttime3 = current3.concat(str[0]);
                    time1Tv.setText(currenttime3);
                    addr1Tv.setText(str[1]);
                    //保存打卡数据
                    SharedPreferences.Editor editor3 = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                            MODE_PRIVATE).edit();
                    editor3.putString("sbtime",currenttime3);
                    editor3.putString("sbaddr",str[1]);
                    editor3.apply();
                    break;
                case ADD_INFO_sxban_old:
                    String current4 = "上班打卡时间";
                    String current5 = "下班打卡时间";
                    String currenttime4 = current4.concat(str[0]);
                    String currenttime5 = current5.concat(str[2]);
                    time1Tv.setText(currenttime4);
                    addr1Tv.setText(str[1]);
                    time2Tv.setText(currenttime5);
                    addr2Tv.setText(str[3]);
                    //保存打卡数据
                    SharedPreferences.Editor editor4 = getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),
                            MODE_PRIVATE).edit();
                    editor4.putString("sbtime",currenttime4);
                    editor4.putString("sbaddr",str[1]);
                    editor4.putString("xbtime",currenttime5);
                    editor4.putString("xbaddr",str[3]);
                    editor4.apply();

                default:
                    break;
            }
        }
    });
}

}
