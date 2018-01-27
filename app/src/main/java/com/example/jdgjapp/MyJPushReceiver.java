package com.example.jdgjapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    private static String type;
    public String sender;
    public static String chanel;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
                Bundle bundle=intent.getExtras();
                String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject object=new JSONObject(extras);
                type=object.getString("type");
                switch (type){
                    case "3":
                        sender=object.getString("sender");
                        chanel=object.getString("chanel");
                        Log.d("收到视频推送","sender: "+sender+" chanel: "+chanel);
                        //自定义通知栏
                        JPushLocalNotification ln = new JPushLocalNotification();
                        ln.setBuilderId(0);
                        ln.setTitle("视频邀请");
                        ln.setContent("用户"+sender+"邀请你进行视频");
                        ln.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln);
                        //如果是在前台，就直接运行
                        if (MyApplication.count!=0)   {
                            ((MyApplication)MyApplication.getContext()).initWorkerThread();
                            Intent i=new Intent(MyApplication.getContext(), ChatActivity.class);
                            i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                        Intent i=new Intent(MyApplication.getContext(), ChatActivity.class);
                        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
