package walfud.meetu;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;

/**
 * Created by song on 2015/6/21.
 */
public class Utils {
//    public static String httpPost()

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

    public interface OnHttpPostResponse {
        void onResponse(String response);
    }
    public static void httpPost(final String request, final OnHttpPostResponse onHttpPostResponse) {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HttpClient httpClient = new DefaultHttpClient();
                // TODO: set timeout
                try {
                    HttpPost httpPost = new HttpPost(request);
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        InputStream inputStream = httpEntity.getContent();
                        byte[] buf = new byte[2048];        // TODO: while != -1
                        inputStream.read(buf);
                        inputStream.close();

                        return new String(buf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                onHttpPostResponse.onResponse(s);
            }
        }.execute();
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
