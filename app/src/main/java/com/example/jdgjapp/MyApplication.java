package com.example.jdgjapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.CurrentUserSettings;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.WorkerThread;
import com.mob.MobApplication;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mwh on 2018/1/22.
 */

public class MyApplication extends MobApplication {

    private static Context context;
    private static String userid;
    private WorkerThread mWorkerThread;
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacksImpl;
    public static int count=0;
    public static String bossid="1000";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mActivityLifecycleCallbacksImpl=new ActivityLifecycleCallbacksImpl();
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);

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
    private class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks{
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }
        @Override
        public void onActivityStarted(Activity activity) {
            count++;
        }
        @Override
        public void onActivityResumed(Activity activity) {
        }
        @Override
        public void onActivityPaused(Activity activity) {
        }
        @Override
        public void onActivityStopped(Activity activity) {
            count--;
        }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public static final CurrentUserSettings mVideoSettings = new CurrentUserSettings();
}
