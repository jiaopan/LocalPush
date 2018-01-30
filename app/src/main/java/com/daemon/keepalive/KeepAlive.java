/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;

import com.test.myapplication.LogHelper;

/**
 * Created by fcc on 17/8/30.
 */

public class KeepAlive {
    private static final int JOB_ID = 1;
    private static final long JOB_PERIODIC_TIME = 5000L;
    private static final Handler mHandler = new Handler();

    public KeepAlive() {
    }

    public static void init(final Context context, boolean isDebug) {
        LogHelper.setDebug(isDebug);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                LogHelper.d(LogHelper.TAG, "start JobScheduler");
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                        new ComponentName(context.getPackageName(), JobSchedulerService.class.getName()));
                builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
                builder.setRequiresDeviceIdle(false);
                if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LogHelper.d(LogHelper.TAG, "start setMinimumLatency");
                    builder.setMinimumLatency(JOB_PERIODIC_TIME;
                } else {
                    LogHelper.d(LogHelper.TAG, "start setPeriodic");
                    builder.setPeriodic(JOB_PERIODIC_TIME);
                }

                jobScheduler.schedule(builder.build());
            } catch (Exception e) {
                // do nothing
                LogHelper.d(LogHelper.TAG, "start JobScheduler error");
                e.printStackTrace();
            }

        } else {
            LogHelper.d(LogHelper.TAG, "start Daemon");
            (new Thread(new DaemonHelper(context, DaemonService.class.getName()))).start();
        }

        mHandler.post(new Runnable() {
            public void run() {
                LogHelper.d(LogHelper.TAG, "start send broad cast");
                Intent intent = new Intent("com.daemon.action.KEEP_ALIVE");
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                context.sendBroadcast(intent);
                KeepAlive.mHandler.postDelayed(this, JOB_PERIODIC_TIME);
            }
        });
    }
}
