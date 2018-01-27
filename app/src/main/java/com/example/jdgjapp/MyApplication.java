package com.example.jdgjapp;

import android.app.Application;
import android.content.Context;

import com.mob.MobApplication;

import org.litepal.LitePal;

/**
 * Created by mwh on 2018/1/22.
 */

public class MyApplication extends MobApplication {

    private static Context context;
    private static String userid;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
    }
    public static void setid(String id) {
        userid = id;
    }
    public static String getid(){
        return userid;
    }

    public static Context getContext() {
        return context;
    }
}
