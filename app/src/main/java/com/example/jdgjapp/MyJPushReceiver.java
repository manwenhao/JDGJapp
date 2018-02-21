package com.example.jdgjapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jdgjapp.Bean.SystemNews;
import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.work.bangong.baoxiao.BaoXiaoMain;
import com.example.jdgjapp.work.bangong.cailiao.ApplyIng;
import com.example.jdgjapp.work.bangong.cailiao.ApplyPass;
import com.example.jdgjapp.work.bangong.cailiao.ApplyRefuse;
import com.example.jdgjapp.work.bangong.gongdan.GongDanMain;
import com.example.jdgjapp.work.bangong.shenpi.BXNo;
import com.example.jdgjapp.work.bangong.shenpi.CLNo;
import com.example.jdgjapp.work.bangong.shenpi.QJNo;
import com.example.jdgjapp.work.bangong.shenpi.SPCCNO;
import com.example.jdgjapp.work.bangong.shipin.StartShiPin;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ShiPinActivity;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplying;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplyok;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplyrefuse;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplying;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyok;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyrefuse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

import static android.content.Context.ACTIVITY_SERVICE;

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
    //材料申请审批回复
    public static String answer;
    public static String answercont;
    public static String sign;
    //请假结果
    public static String qjanswer;
    public static String qjid;
    //出差结果
    public static String ccanswer;
    public static String ccid;
    //报销结果
    public static String bxid;
    public static String bxanswer;
    //审批报销请求
    public static String acc_id;
    public static String acc_kind;
    public static String acc_userid;
    public static String acc_cont;
    public static String acc_date;
    public static String acc_backmoney;
    public static String acc_img;

    //请假请求
    public static String qjapplykind;
    public static String qjapplyid;
    public static String qjman_id;
    public static String qjdatime;
    public static String qjstart;
    public static String qjreason;
    public static String qjend;
    //出差请求
    public static String ccapplyid;
    public static String ccapplykind;
    public static String ccman_id;
    public static String ccdatime;
    public static String ccstart;
    public static String ccreason;
    public static String ccend;

    //材料请求
    public static String clapplysign;
    public static String clman_id;
    public static String cldatime;
    public static String clreason;
    public static String clmaterials;





    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
                Bundle bundle=intent.getExtras();
                String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject object=new JSONObject(extras);
                type=object.getString("type");
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datastring=sf.format(new Date());
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                String newsstring=aCache.getAsString("systemnews");
                Type newstype=new TypeToken<List<SystemNews>>(){}.getType();
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
                        SystemNews news=new SystemNews();
                        news.setType("1");
                        news.setContent(taskid);
                        news.setTime(datastring);
                        news.setStatus("0");
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent1news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent1news);
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
                        SystemNews news1=new SystemNews();
                        news1.setType("3");
                        news1.setTime(datastring);
                        news1.setTitle(sendername);
                        news1.setStatus("0");
                        news1.setContent(chanel);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news1);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news1);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent3news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent3news);
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
                    case "2":
                       answer=object.getString("answer");
                       answercont=object.getString("answercont");
                       sign=object.getString("sign");
                       Log.d("接收到材料申请审批结果","answer: "+answer+" answercont: "+answercont+" sign: "+sign);
                        JPushLocalNotification ln2 = new JPushLocalNotification();
                        ln2.setBuilderId(0);
                        ln2.setTitle("材料审批结果");
                        if (answer.equals("1")){
                            ln2.setContent("您有一个部门材料申请通过审核");
                        }else {
                            ln2.setContent("您有一个部门材料申请未通过审核");
                        }
                        ln2.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln2);
                        SystemNews news2=new SystemNews();
                        news2.setType("2");
                        news2.setTime(datastring);
                        news2.setContent(answer);
                        news2.setStatus("0");
                        news2.setTitle(sign);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news2);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news2);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent2news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent2news);
                        ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                        String classname=componentName.getClassName();
                        if (answer.equals("1")){
                            Log.d(TAG,"准备发送材料审核通过广播1=="+classname);
                            if (classname.equals(ApplyIng.class.getName())||classname.equals(ApplyPass.class.getName())){
                                Log.d(TAG,"准备发送材料审核通过广播2");
                                Intent intent1=new Intent("clapplypass");
                                MyApplication.getContext().sendBroadcast(intent1);
                                Log.d(TAG,"准备发送材料审核通过广播3");
                            }
                        }
                        if (answer.equals("2")){
                            if (classname.equals(ApplyIng.class.getName())||classname.equals(ApplyRefuse.class.getName())){
                                Intent intent1=new Intent("clapplyrefuse");
                                MyApplication.getContext().sendBroadcast(intent1);
                            }
                        }
                        break;
                    case "4":
                        qjanswer=object.getString("answer");
                        qjid=object.getString("id");
                        JPushLocalNotification ln4 = new JPushLocalNotification();
                        ln4.setBuilderId(0);
                        ln4.setTitle("请假审批结果");
                        if (qjanswer.equals("1")){
                            ln4.setContent("您有一个请假申请通过审核");
                        }else {
                            ln4.setContent("您有一个请假申请未通过审核");
                        }
                        ln4.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln4);
                        SystemNews news3=new SystemNews();
                        news3.setType("4");
                        news3.setTime(datastring);
                        news3.setStatus("0");
                        news3.setTitle(qjid);
                        news3.setContent(qjanswer);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news3);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news3);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent4news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent4news);
                        ActivityManager activityManager4 = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName4 = activityManager4.getRunningTasks(1).get(0).topActivity;
                        String classname4=componentName4.getClassName();
                        if (qjanswer.equals("1")){
                            if (classname4.equals(QJApplying.class.getName())||classname4.equals(QJApplyok.class.getName())){
                                Intent intent1=new Intent("qjapplyok");
                                MyApplication.getContext().sendBroadcast(intent1);
                            }
                        }
                        if (qjanswer.equals("2")){
                            if (classname4.equals(QJApplying.class.getName())||classname4.equals(QJApplyrefuse.class.getName())){
                                Intent intent1=new Intent("qjapplyrefuse");
                                MyApplication.getContext().sendBroadcast(intent1);
                            }
                        }

                        break;
                    case "5":
                        ccanswer=object.getString("answer");
                        ccid=object.getString("id");
                        JPushLocalNotification ln5 = new JPushLocalNotification();
                        ln5.setBuilderId(0);
                        ln5.setTitle("出差申请审批结果");
                        if (ccanswer.equals("1")){
                            ln5.setContent("您有一个出差申请通过审核");
                        }else {
                            ln5.setContent("您有一个出差申请未通过审核");
                        }
                        ln5.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln5);
                        SystemNews news4=new SystemNews();
                        news4.setType("5");
                        news4.setTime(datastring);
                        news4.setTitle(ccid);
                        news4.setStatus("0");
                        news4.setContent(ccanswer);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news4);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news4);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent5news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent5news);
                        ActivityManager activityManager5 = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName5 = activityManager5.getRunningTasks(1).get(0).topActivity;
                        String classname5=componentName5.getClassName();
                        if (ccanswer.equals("1")){
                            if (classname5.equals(CCApplying.class.getName())||classname5.equals(CCApplyok.class.getName())){
                                Intent intent1=new Intent("ccapplyok");
                                MyApplication.getContext().sendBroadcast(intent1);
                            }
                        }
                        if (ccanswer.equals("2")){
                            if (classname5.equals(CCApplying.class.getName())||classname5.equals(CCApplyrefuse.class.getName())){
                                Intent intent1=new Intent("ccapplyrefuse");
                                MyApplication.getContext().sendBroadcast(intent1);
                            }
                        }

                        break;
                    case "6":
                        bxanswer=object.getString("answer");
                        bxid=object.getString("id");
                        JPushLocalNotification ln6 = new JPushLocalNotification();
                        ln6.setBuilderId(0);
                        ln6.setTitle("报销申请审批结果");
                        if (bxanswer.equals("1")){
                            ln6.setContent("您有一个报销申请通过审核");
                        }else {
                            ln6.setContent("您有一个报销申请未通过审核");
                        }
                        ln6.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln6);
                        SystemNews news5=new SystemNews();
                        news5.setType("6");
                        news5.setContent(bxanswer);
                        news5.setTitle(bxid);
                        news5.setStatus("0");
                        news5.setTime(datastring);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news5);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news5);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent6news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent6news);
                        ActivityManager activityManager6 = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName6 = activityManager6.getRunningTasks(1).get(0).topActivity;
                        String classname6=componentName6.getClassName();
                        Log.d("当前活动名",classname6);
                        if (classname6.equals(BaoXiaoMain.class.getName())){
                            Intent intent1=new Intent("baoxiaoapply");
                            MyApplication.getContext().sendBroadcast(intent1);
                        }
                        break;
                    case "7":
                        acc_id=object.getString("acc_id");
                        acc_kind=object.getString("acc_kind");
                        acc_userid=object.getString("acc_userid");
                        acc_cont=object.getString("acc_cont");
                        acc_date=object.getString("date");
                        acc_backmoney=object.getString("backmoney");
                        acc_img=object.getString("acc_img");
                        JPushLocalNotification ln7 = new JPushLocalNotification();
                        ln7.setBuilderId(0);
                        ln7.setTitle("一个新的报销申请");
                        ln7.setContent("您有一个"+acc_kind+"报销申请");
                        ln7.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln7);
                        SystemNews news6=new SystemNews();
                        news6.setType("7");
                        news6.setContent(acc_id+"@#$"+acc_kind+"@#$"+acc_userid+"@#$"+acc_cont+"@#$"+acc_date+"@#$"+acc_backmoney+"@#$"+acc_img);
                        news6.setTitle(acc_kind);
                        news6.setTime(datastring);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news6);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news6);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent7news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent7news);
                        ActivityManager activityManager7 = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName7 = activityManager7.getRunningTasks(1).get(0).topActivity;
                        String classname7=componentName7.getClassName();
                        Log.d("当前活动名",classname7);
                        if (classname7.equals(BXNo.class.getName())){
                            Intent intent1=new Intent("newbx");
                            MyApplication.getContext().sendBroadcast(intent1);
                        }
                        break;
                    case "9":
                        qjapplyid=object.getString("id");
                        qjapplykind=object.getString("kind");
                        qjman_id=object.getString("man_id");
                        qjdatime=object.getString("datime");
                        qjstart=object.getString("startDate");
                        qjend=object.getString("endDate");
                        qjreason=object.getString("reason");
                        JPushLocalNotification ln9 = new JPushLocalNotification();
                        ln9.setBuilderId(0);
                        ln9.setTitle("一个新的请假申请");
                        ln9.setContent("您有一个"+qjapplykind+"请假申请");
                        ln9.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln9);
                        SystemNews news7=new SystemNews();
                        news7.setType("9");
                        news7.setContent(qjapplyid+"@#$"+qjapplykind+"@#$"+qjman_id+"@#$"+qjdatime+"@#$"+qjstart+"@#$"+qjend+"@#$"+qjreason);
                        news7.setTitle(qjapplykind);
                        news7.setTime(datastring);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news7);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news7);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent9news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent9news);
                        ActivityManager activityManager9 = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentName9 = activityManager9.getRunningTasks(1).get(0).topActivity;
                        String classname9=componentName9.getClassName();
                        Log.d("当前活动名",classname9);
                        if (classname9.equals(QJNo.class.getName())){
                            Intent intent1=new Intent("newqj");
                            MyApplication.getContext().sendBroadcast(intent1);
                        }

                        break;
                    case "8":
                        ccapplyid=object.getString("id");
                        ccapplykind=object.getString("kind");
                        ccman_id=object.getString("man_id");
                        ccdatime=object.getString("datime");
                        ccstart=object.getString("startDate");
                        ccend=object.getString("endDate");
                        ccreason=object.getString("reason");
                        JPushLocalNotification ln8 = new JPushLocalNotification();
                        ln8.setBuilderId(0);
                        ln8.setTitle("一个新的出差申请");
                        ln8.setContent("您有一个"+ccapplykind+"出差申请");
                        ln8.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln8);
                        SystemNews news8=new SystemNews();
                        news8.setType("8");
                        news8.setContent(ccapplyid+"@#$"+ccapplykind+"@#$"+ccman_id+"@#$"+ccdatime+"@#$"+ccstart+"@#$"+ccend+"@#$"+ccreason);
                        news8.setTitle(ccapplykind);
                        news8.setTime(datastring);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news8);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news8);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent10news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent10news);
                        ActivityManager activityManagerx = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentNamex= activityManagerx.getRunningTasks(1).get(0).topActivity;
                        String classnamex=componentNamex.getClassName();
                        Log.d("当前活动名",classnamex);
                        if (classnamex.equals(SPCCNO.class.getName())){
                            Intent intent1=new Intent("newcc");
                            MyApplication.getContext().sendBroadcast(intent1);
                        }
                        break;
                    case "10":
                        clapplysign=object.getString("sign");
                        clman_id=object.getString("man_id");
                        cldatime=object.getString("datime");
                        clreason=object.getString("reason");
                        clmaterials=object.getString("materials");
                        JPushLocalNotification ln10 = new JPushLocalNotification();
                        ln10.setBuilderId(0);
                        ln10.setTitle("一个新的材料申请");
                        ln10.setContent("您有一个新的部门材料申请");
                        ln10.setNotificationId(11111111) ;
                        JPushInterface.addLocalNotification(context, ln10);
                        SystemNews news9=new SystemNews();
                        news9.setType("10");
                        news9.setContent(clapplysign+"@#$"+clman_id+"@#$"+cldatime+"@#$"+clreason+"@#$"+clmaterials);
                        news9.setTitle(clman_id);
                        news9.setTime(datastring);
                        if (newsstring==null){
                            List<SystemNews> newslist1=new ArrayList<SystemNews>();
                            newslist1.add(news9);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }else {
                            List<SystemNews> newslist1=new Gson().fromJson(newsstring,newstype);
                            newslist1.add(0,news9);
                            aCache.put("systemnews",new Gson().toJson(newslist1));
                        }
                        Intent intent11news=new Intent("systemnews");
                        MyApplication.getContext().sendBroadcast(intent11news);
                        ActivityManager activityManagerxi = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                        ComponentName componentNamexi= activityManagerxi.getRunningTasks(1).get(0).topActivity;
                        String classnamexi=componentNamexi.getClassName();
                        Log.d("当前活动名",classnamexi);
                        if (classnamexi.equals(CLNo.class.getName())){
                            Intent intent1=new Intent("newcl");
                            MyApplication.getContext().sendBroadcast(intent1);
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
                }else if (type.equals("2")){
                    ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                    String classname=componentName.getClassName();
                   if (MyApplication.count>0){
                       if (!classname.equals(ApplyPass.class.getName())){
                           if (answer.equals("1")){
                               Log.d("极光推送自定义动作","用户点击了材料申请审核回复");
                               Intent intent1=new Intent(MyApplication.getContext(),ApplyPass.class);
                               intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               context.startActivity(intent1);
                           }
                       }
                       if (!classname.equals(ApplyRefuse.class.getName())){
                           if (answer.equals("2")){
                               Log.d("极光推送自定义动作","用户点击了材料申请审核回复");
                               Intent intent1=new Intent(MyApplication.getContext(),ApplyRefuse.class);
                               intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               context.startActivity(intent1);

                           }
                       }
                   }

                }else if (type.equals("4")){
                    ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                    String classname=componentName.getClassName();
                    if (MyApplication.count>0){
                        if (!classname.equals(QJApplyok.class.getName())){
                            if (qjanswer.equals("1")){
                                Intent intent1=new Intent(MyApplication.getContext(),QJApplyok.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        }
                        if (!classname.equals(QJApplyrefuse.class.getName())){
                            if (qjanswer.equals("2")){
                                Intent intent1=new Intent(MyApplication.getContext(),QJApplyrefuse.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        }
                    }
                }else if (type.equals("5")){
                    ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                    String classname=componentName.getClassName();
                    if (MyApplication.count>0){
                        if (!classname.equals(CCApplyok.class.getName())){
                            if (ccanswer.equals("1")){
                                Intent intent1=new Intent(MyApplication.getContext(),CCApplyok.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        }
                        if (!classname.equals(CCApplyrefuse.class.getName())){
                            if (ccanswer.equals("2")){
                                Intent intent1=new Intent(MyApplication.getContext(),CCApplyrefuse.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        }
                    }
                }else if (type.equals("6")){
                    ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                    String classname=componentName.getClassName();
                    if (MyApplication.count>0){
                        if (!classname.equals(BaoXiaoMain.class.getName())){
                            Intent intent1=new Intent(MyApplication.getContext(),BaoXiaoMain.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                    }
                }else if (type.equals("7")){
                    ActivityManager activityManager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
                    String classname=componentName.getClassName();
                    if (MyApplication.count>0){
                        if (!classname.equals(BXNo.class.getName())){
                            Intent intent1=new Intent(MyApplication.getContext(),BXNo.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
