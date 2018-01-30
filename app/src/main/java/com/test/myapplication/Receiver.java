package com.test.myapplication;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;


public class Receiver extends BroadcastReceiver {
    private static final String NOTIFICATION_NAME = "AA";
    private static final int NOTIFICATION_ID = 1111;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("jiaopan", "Push Demo Receiver onReceive");
        sendNotification(context);
    }

    private void sendNotification(Context context) {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        Notification notification = new Notification(R.drawable.ic_launcher_background, "aa", System.currentTimeMillis());
//        notification.contentIntent = contentIntent;
//        notifyManager.notify(111, notification);


//        //获取PendingIntent
//        Intent mainIntent = new Intent(context, MainActivity.class);
//        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //创建 Notification.Builder 对象
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                //点击通知后自动清除
//                .setAutoCancel(true)
//                .setContentTitle("我是带Action的Notification")
//                .setContentText("点我会打开MainActivity")
//                .setContentIntent(mainPendingIntent);
//        //发送通知
//        notifyManager.notify(3, builder.build());


        NotificationManager notifyManage = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.icon = R.drawable.ic_launcher_background;
        notification.tickerText = Html.fromHtml(context.getString(R.string.app_name));
        notification.defaults = 0;
        notification.sound = null;
        notification.vibrate = null;
        notification.contentIntent = getContentIntent(context);
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

//    private static RemoteViews getContentViews(Context context) {
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.battery_amount_notification);
//        remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.notification_battery_amount);
//        remoteViews.setTextViewText(R.id.notification_title, Html.fromHtml(context.getString(R.string.battery_amount_notification_title)));
//        remoteViews.setTextViewText(R.id.notification_button, context.getString(R.string.battery_amount_notification_button));
//        return remoteViews;
//    }
}
