package com.example.jdgjapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.work.bangong.gongdan.GongDanMain;
import com.example.jdgjapp.work.bangong.shipin.StartShiPin;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ShiPinActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

public class MyJPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyJPushReceiver";
    private static String type;
    public static String sender;
    public static String chanel;

    //工单
    public static String senderr;
    public static String cycle;
    public static String startime;
    public static String createtime;
    public static String addr;


    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
                Bundle bundle=intent.getExtras();
                String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject object=new JSONObject(extras);
                type=object.getString("type");
                switch (type){
                    case "1":  //收到的是一个工单
                        String taskid = bundle.getString(JPushInterface.EXTRA_TITLE);
                        String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                        senderr = object.getString("sender");
                        cycle = object.getString("cycle");
                        startime = object.getString("startime");
                        createtime = object.getString("createtime");
                        addr = object.getString("addr");

                        //将数据存入数据库
                        Task task = new Task();
                        task.setTaskid(taskid);
                        task.setSender(senderr);
                        task.setContent(content);
                        task.setCreatetime(createtime);
                        task.setStartime(startime);
                        task.setAddr(addr);
                        task.setCycle(cycle);
                        task.setStatus("1");
                        task.save();

                        Log.d(TAG, "JPush工单taskid is     " + taskid);
                        Log.d(TAG, "JPush工单sender is     " + senderr);
                        Log.d(TAG, "JPush工单createtime is " + createtime);
                        Log.d(TAG, "JPush工单starttime is  " + startime);
                        Log.d(TAG, "JPush工单addr is       " + addr);
                        Log.d(TAG, "JPush工单content is    " + content);
                        Log.d(TAG, "JPush工单cycle is      " + cycle);

                        //自定义通知栏
                        JPushLocalNotification ln0 = new JPushLocalNotification();
                        ln0.setBuilderId(0);
                        ln0.setTitle("您有一个新的工单");
                        ln0.setContent("注意查收");
                        ln0.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln0);

                        break;

                    case "3":
                        sender=object.getString("sender");
                        chanel=object.getString("chanel");
                        Log.d("收到视频推送","sender: "+sender+" chanel: "+chanel);
                        //自定义通知栏
                        JPushLocalNotification ln = new JPushLocalNotification();
                        ln.setBuilderId(0);
                        ln.setTitle("视频邀请");
                        String sendername=sender;
                        ln.setContent("用户"+sendername+"邀请你进行视频");
                        ln.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln);
                        //如果是在前台，就直接运行
                        if (MyApplication.count!=0)   {
                            ((MyApplication)MyApplication.getContext()).initWorkerThread();
                            Intent i=new Intent(MyApplication.getContext(), StartShiPin.class);
                            i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("sender",sendername);
                            context.startActivity(i);
                        }
                        break;
                    default:
                        break;

                }
            }

            if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
                if (type==null){
                    Log.d("极光推送自定义动作","null");
                }else if (type.equals("3")){
                    //如果在后台，就设计notification点击时间
                    if (MyApplication.count==0)  {
                        ((MyApplication)MyApplication.getContext()).initWorkerThread();
                    Log.d("极光推送自定义动作","用户点击了视频notification");
                        Intent i=new Intent(MyApplication.getContext(), StartShiPin.class);
                        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String sendername=sender.split(";")[1];
                        i.putExtra("sender",sendername);
                        context.startActivity(i);
                    }
                }else if (type.equals("1")){
                    Log.d(TAG, "用户点击打开了工单通知");
                    Intent i = new Intent(context, GongDanMain.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
