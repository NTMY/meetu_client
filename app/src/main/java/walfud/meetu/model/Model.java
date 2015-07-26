package walfud.meetu.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/6/24.
 */
public class Model extends Service {

    public static final String TAG = "EngineService";

    private LocationHelper mLocationHelper;

    private static final long UPDATE_INTERVAL = 2000;

    private Timer mTimer = new Timer();
    private TimerTask mReportSelfTimerTask = new TimerTask() {
        @Override
        public void run() {
            mLocationHelper.reportSelf();
        }
    };
    private TimerTask mSearchOthersTimerTask = new TimerTask() {
        @Override
        public void run() {
            mLocationHelper.searchNearby();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationHelper = new LocationHelper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTimer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    // Function
    public void setOnSearchListener(DataRequest.OnDataRequestListener listener) {
        mLocationHelper.setOnSearchListener(listener);
    }

    public void reportSelf() {
        mLocationHelper.reportSelf();
    }
    public void searchNearby() {
        mLocationHelper.searchNearby();
    }

    public void startAutoReportSelf() {
        mTimer.schedule(mReportSelfTimerTask, 0, UPDATE_INTERVAL);
    }
    public void stopAutoReportSelf() {
    }

    public void startAutoSearchNearby() {
        mTimer.schedule(mSearchOthersTimerTask, 0, UPDATE_INTERVAL);
    }
    public void stopAutoSearchNearby() {
    }

    //
    public static final Intent SERVICE_INTENT = new Intent(MeetUApplication.getContext(), Model.class);
    public static void startService() {
        MeetUApplication.getContext().startService(SERVICE_INTENT);
    }
    public static void stopService() {
        MeetUApplication.getContext().stopService(SERVICE_INTENT);
    }
    public static boolean isServiceRunning() {
        return Utils.isServiceRunning(MeetUApplication.getContext(), SERVICE_INTENT);
    }
}