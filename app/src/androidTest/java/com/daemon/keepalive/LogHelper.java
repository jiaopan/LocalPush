/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import android.util.Log;

/**
 * Created by fcc on 17/8/30.
 */

public class LogHelper {
    public static final String TAG = "KEEP_ALIVE";
    private static boolean DEBUG = false;

    public LogHelper() {
    }

    public static void setDebug(boolean isDebug) {
        DEBUG = isDebug;
    }

    public static void d(String subTag, String msg) {
        if (DEBUG) {
            Log.d(TAG, getLogMsg(subTag, msg));
        }

    }

    public static void e(String subTag, String msg) {
        if (DEBUG) {
            Log.e(TAG, getLogMsg(subTag, msg));
        }

    }

    private static String getLogMsg(String subTag, String msg) {
        return "[" + subTag + "] " + msg;
    }
}
