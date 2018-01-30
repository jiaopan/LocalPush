package com.keepalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.test.myapplication.App;
import com.test.myapplication.MainActivity;
import com.test.myapplication.R;


@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private static final String NOTIFICATION_NAME = "MyApp";
    private static final int NOTIFICATION_ID = 1111;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("jiaopan", "MyJobService onStartJob ~~~");
        Toast.makeText(App.sInstance, "JobService start", Toast.LENGTH_LONG).show();
//        showNotification();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("jiaopan", "MyJobService onStopJob");
        return false;
    }

    private void showNotification() {
        NotificationManager notifyManage = (NotificationManager) App.sInstance.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.icon = R.drawable.ic_launcher_background;
        notification.tickerText = Html.fromHtml(App.sInstance.getString(R.string.app_name));
        notification.defaults = 0;
        notification.sound = null;
        notification.vibrate = null;
        notification.contentIntent = getContentIntent(App.sInstance);
//        notification.contentView = getContentViews(context);
        notifyManage.notify(NOTIFICATION_NAME, NOTIFICATION_ID, notification);
    }

    private static PendingIntent getContentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent realIntent = PendingIntent
                .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return realIntent;
    }
}
