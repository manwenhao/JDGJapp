package com.example.jdgjapp.work.bangong.gongdan;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;


public class FileUploadManager {
   private  static String url;
    private static ReportListener mylistener;
    /*
    提供监听器接口，方便后续拓展
    发送信息之前请设置监听器
    * */
    public static void setMylistener(ReportListener listener){
        mylistener=listener;
    }
    public static void setUrl(String myurl){
        url=myurl;
    }
    /*
    * 文件以map的形式发送，描述文字以参数的形式发送。
    * 每个文件的key都为imgs，文件名为map中的string
    * 建议获取文件的方法以网上的一个文件factory的方式获取
    *
    * */
    public static void uploadReport(final Map<String,File> files, final String des){

        if (files!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("send report","111");
                    OkHttpUtils.post()
                            .url(url)
                            .addParams("des",des)
                            .files("imgs",files)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    mylistener.onError(call, e, id);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    mylistener.onSuccess(response, id);
                                }
                            });


                }
            }).start();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("send report","222");
                    OkHttpUtils.post()
                            .url(url)
                            .addParams("content",des)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    mylistener.onError(call, e, id);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    mylistener.onSuccess(response, id);
                                }
                            });
                }
            }).start();
        }
    }
}
