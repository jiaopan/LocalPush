package com.test.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.keepalive.MyJobService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.alarm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("jiaopan", "alarm regist");
                registerRepeatAlarmEvent(1000 * 5);
            }
        });
        findViewById(R.id.job).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("jiaopan", "job regist");
                registerJobSchedule();
            }
        });
    }

    /**
     * 大师后台周期性alarm事件
     */
    private void registerRepeatAlarmEvent(long period) {
        Intent intent = new Intent(this, Receiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Intent intent = new Intent(sInstance, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pi = PendingIntent.getActivity(sInstance, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, period, pi);
    }

    private void registerJobSchedule() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("jiaopan", "start regist Job ~");
            JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(0,
                    new ComponentName(this, MyJobService.class));

            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                Log.e("jiaopan", "JobSchedule setMinimumLatency");
                builder.setMinimumLatency(5 * 1000);
            } else {
                Log.e("jiaopan", "JobSchedule setPeriodic");
                builder.setPeriodic(5 * 1000);
            }

            mJobScheduler.schedule(builder.build());
        }
    }
}
