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
import android.os.Handler;

/**
 * Created by fcc on 17/8/30.
 */

public class KeepAlive {
    private static final int JOB_ID = 1;
    private static final long JOB_PERIODIC_TIME = 600000L;
    private static final Handler mHandler = new Handler();

    public KeepAlive() {
    }

    public static void init(final Context context, boolean isDebug) {
        com.test.myapplication.LogHelper.setDebug(isDebug);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                com.test.myapplication.LogHelper.d(com.test.myapplication.LogHelper.TAG, "start JobScheduler");
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                        new ComponentName(context.getPackageName(), JobSchedulerService.class.getName()));
                builder.setPersisted(true);
                builder.setPeriodic(JOB_PERIODIC_TIME);
                jobScheduler.cancel(JOB_ID);
                jobScheduler.schedule(builder.build());
            } catch (Exception e) {
                // do nothing
                com.test.myapplication.LogHelper.d(com.test.myapplication.LogHelper.TAG, "start JobScheduler error");
                e.printStackTrace();
            }

        } else {
            com.test.myapplication.LogHelper.d(com.test.myapplication.LogHelper.TAG, "start Daemon");
            (new Thread(new DaemonHelper(context, DaemonService.class.getName()))).start();
        }

        mHandler.post(new Runnable() {
            public void run() {
                com.test.myapplication.LogHelper.d(com.test.myapplication.LogHelper.TAG, "start send broad cast");
                Intent intent = new Intent("com.daemon.action.KEEP_ALIVE");
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                context.sendBroadcast(intent);
                KeepAlive.mHandler.postDelayed(this, JOB_PERIODIC_TIME);
            }
        });
    }
}
