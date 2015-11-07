package com.walfud.meetu;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by song on 2015/6/21.
 */
public class Utils {

    public static boolean isServiceRunning(Context context, Intent serviceIntent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfoList) {
            if (runningServiceInfo.service.getClassName().equals(serviceIntent.getComponent().getClassName())) {
                return true;
            }
        }

        return false;
    }

    public static final int NOTIFICATION_ID = 0x10000;
    public static void showNotification(Context context, int id, PendingIntent pendingIntent,
                                           Bitmap largeIcon, int smallIconResId,
                                           CharSequence contentTitle, CharSequence contentText, CharSequence subText,
                                           String person) {
        Notification notification = new Notification.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(largeIcon).setSmallIcon(smallIconResId)
                .setContentTitle(contentTitle).setContentText(contentText).setSubText(subText)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
//                .addPerson(person)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }
    public static void clearNotification(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
