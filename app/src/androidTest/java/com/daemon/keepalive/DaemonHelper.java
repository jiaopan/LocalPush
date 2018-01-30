/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.daemon.keepalive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Build;

/**
 * Created by fcc on 17/8/30.
 */

public class DaemonHelper implements Runnable {
    private static final String TAG = "DaemonHelper";
    private static final String SOCKET_PREFIX = "s_dx_";
    private static final String DAEMON_SOURCE_ARM = "armeabi/daemon";
    private static final String DAEMON_SOURCE_X86 = "x86/daemon";
    private static final String DAEMON_TARGET_NAME = "daemon";
    private static final int DAEMON_ACTION_START = 255;
    private static final int DAEMON_ACTION_STOP = 0;
    private static final String ACTION_LAUNCH_FROM_DAEMON = "com.deamon.ACTION";
    private LocalServerSocket mServiceSocket;
    private LocalSocket mClientSocket;
    private String mSocketName;
    private File mDaemonFile;
    private String mServiceName;
    private Context mContext;

    public DaemonHelper(Context ctx, String serviceName) {
        this.mContext = ctx;
        this.mServiceName = serviceName;
        this.mSocketName = SOCKET_PREFIX + System.currentTimeMillis();
        this.mDaemonFile = new File(this.mContext.getFilesDir(), DAEMON_TARGET_NAME);
    }

    public void run() {
        if(this.mServiceSocket != null) {
            this.stop();
        }

        try {
            this.mServiceSocket = new LocalServerSocket(this.mSocketName);
        } catch (IOException var3) {
            com.test.myapplication.LogHelper.e(TAG, "create server error!");
            return;
        }

        if(!this.startDaemon()) {
            com.test.myapplication.LogHelper.e(TAG, "error!");
            this.stop();
        } else {
            while(true) {
                try {
                    this.mClientSocket = this.mServiceSocket.accept();
                    this.mClientSocket.getOutputStream().write(DAEMON_ACTION_START);
                } catch (IOException var2) {
                    com.test.myapplication.LogHelper.e(TAG, "listen error!");
                    this.stop();
                    return;
                }

                while(true) {
                    try {
                        if(this.mClientSocket.getInputStream().read() > 0) {
                            continue;
                        }
                    } catch (IOException var4) {
                        ;
                    }

                    this.startDaemon();
                    if(this.mServiceSocket == null) {
                        return;
                    }
                    break;
                }
            }
        }
    }

    public void stop() {
        if(this.mClientSocket != null) {
            if(this.mClientSocket.isConnected()) {
                try {
                    this.mClientSocket.getOutputStream().write(0);
                    this.mClientSocket.close();
                } catch (IOException var3) {
                    ;
                }
            }

            this.mClientSocket = null;
        }

        if(this.mServiceSocket != null) {
            try {
                this.mServiceSocket.close();
            } catch (IOException var2) {
                ;
            }

            this.mServiceSocket = null;
        }

    }

    private File getDaemonFile() {
        if(!this.mDaemonFile.exists()) {
            try {
                String e = DAEMON_SOURCE_ARM;
                if(Build.CPU_ABI.equalsIgnoreCase("x86")) {
                    e = DAEMON_SOURCE_X86;
                }

                InputStream is = this.mContext.getAssets().open(e);
                this.saveStreamToFile(is, this.mDaemonFile);
                com.test.myapplication.LogHelper.d(TAG, this.mDaemonFile.length() + "<---mDaemonFile.length");
                this.mDaemonFile.setExecutable(true);
                is.close();
            } catch (IOException var3) {
                com.test.myapplication.LogHelper.e(TAG, "prepare error: " + var3);
                return null;
            }
        }

        return this.mDaemonFile;
    }

    private boolean startDaemon() {
        File f = this.getDaemonFile();
        if(f == null) {
            return false;
        } else {
            com.test.myapplication.LogHelper.d(TAG, "file name = " + f.getName() + ",file ParentFile = " + f.getParentFile().getAbsolutePath());
            HashMap env = new HashMap(System.getenv());
            env.put("PATH", ".:" + (String)env.get("PATH"));
            env.put("arg1", this.mSocketName);
            env.put("arg2", this.mContext.getPackageName() + "/" + this.mServiceName);
            env.put("arg3", this.mContext.getApplicationInfo().dataDir);
            env.put("arg4", this.mContext.getApplicationInfo().sourceDir);
            env.put("arg5", String.valueOf(Build.VERSION.SDK_INT));
            env.put("arg6", ACTION_LAUNCH_FROM_DAEMON);
            String[] envp = new String[env.size()];
            int i = 0;

            String ret;
            for(Iterator e = env.keySet().iterator(); e.hasNext(); envp[i++] = ret + "=" + (String)env.get(ret)) {
                ret = (String)e.next();
            }

            try {
                Process var8 = Runtime.getRuntime().exec(f.getName(), envp, f.getParentFile());
                int var9 = var8.waitFor();
                if(var9 == 0) {
                    return true;
                }

                com.test.myapplication.LogHelper.e(TAG, "daemon error, ret " + var9);
            } catch (Exception var7) {
                com.test.myapplication.LogHelper.e(TAG, "daemon error: " + var7);
            }

            return false;
        }
    }

    private void saveStreamToFile(InputStream is, File file) {
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            byte[] e = new byte[1024];

            int len;
            while((len = is.read(e)) != -1) {
                os.write(e, 0, len);
            }

            os.flush();
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception var13) {
                var13.printStackTrace();
            }

        }

    }
}
