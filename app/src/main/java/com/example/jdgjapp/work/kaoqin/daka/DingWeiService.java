package com.example.jdgjapp.work.kaoqin.daka;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DingWeiService extends Service {

    private static final String TAG = "DingWeiService";
    public LocationClient nLocationClient;
    private Object  objLock = new Object();
    DaKaMain.MyLocationListener mylisten = null;

    public DingWeiService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nLocationClient = new LocationClient(getApplicationContext());
                nLocationClient.registerLocationListener(new MyLocationListener());

                requestLocation();

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestLocation() {
        initLocation();
        nLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(300000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        nLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.d(TAG, "实时定位Listener开启");
            //获取经纬度
            Double position1 = bdLocation.getLatitude();
            Double position2 = bdLocation.getLongitude();
            String position3 = position1.toString();
            String position4 = position2.toString();

            Log.d(TAG, "实时定位经度" + position4);
            Log.d(TAG, "实时定位纬度" + position3);

            //读取工号
            User user = DataSupport.findFirst(User.class);
            //读取当前时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currenttime = format.format(calendar.getTime());
            Log.d(TAG, "定位111111111");
            //上传数据
            sendRequestPosition(user.getUsr_id(),position3,position4,currenttime);
            Log.d(TAG, "定位222222222");
        }
    }

    private void sendRequestPosition(final String id, final String Position1, final String Position2, final String datetime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id", id)
                                .add("posy", Position1)
                                .add("posx", Position2)
                                .add("datetime", datetime)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://106.14.145.208:8080//JDGJ/ReceiveUsrSsLocate")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseDate = response.body().string();
                    Log.d(TAG, "定位已发送111");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }).start();
        Log.d(TAG, "定位已发送");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "定位服务onDestroy执行");
        stop();
        unregisterListener(mylisten);
        super.onDestroy();
    }

    public void stop(){
        synchronized (objLock) {
            if(nLocationClient != null && nLocationClient.isStarted()){
                nLocationClient.stop();
                Log.d(TAG, "实时定位Listener关闭");
            }
        }
    }

    public void unregisterListener(DaKaMain.MyLocationListener listener){
        if(listener != null){
            nLocationClient.unRegisterLocationListener(listener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
