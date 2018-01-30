/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by fcc on 17/8/30.
 */

public class DaemonService extends Service {
    public DaemonService() {
    }

    public void onCreate() {
        super.onCreate();
        com.test.myapplication.LogHelper.d(com.test.myapplication.LogHelper.TAG, "DaemonService OnCreate");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
