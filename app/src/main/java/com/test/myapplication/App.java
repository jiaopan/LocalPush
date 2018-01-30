package com.test.myapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import com.daemon.keepalive.KeepAlive;


public class App extends Application {
    public static Application sInstance;
    private static final String ACTION = "com.test.demo.notification";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.e("jiaopan", "Push Demo onCreate API :"+ VERSION.SDK_INT );
    }

}
