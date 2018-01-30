/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.test.myapplication.LogHelper;

/**
 * Created by fcc on 17/8/30.
 */

public class KeepAliveReciver extends BroadcastReceiver {
    public KeepAliveReciver() {
    }

    public void onReceive(Context context, Intent intent) {
        LogHelper.d(LogHelper.TAG, context.getPackageName() + ", KeepAliveReciver start");
    }
}
