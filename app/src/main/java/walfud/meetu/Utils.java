package walfud.meetu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
}
