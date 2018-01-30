/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.test.myapplication.LogHelper;

/**
 * Created by fcc on 17/8/30.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private Handler mJobHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            LogHelper.d(LogHelper.TAG, "JobService task running");
            jobFinished((JobParameters) msg.obj, true);
            return true;
        }
    });

    public JobSchedulerService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogHelper.d(LogHelper.TAG, "jobSchedulerService start job");
        this.mJobHandler.sendMessage(Message.obtain(this.mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogHelper.d(LogHelper.TAG, "jobSchedulerService stop job");
        this.mJobHandler.removeMessages(1);
        return false;
    }
}
