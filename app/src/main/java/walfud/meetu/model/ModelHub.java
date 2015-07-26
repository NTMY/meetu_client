package walfud.meetu.model;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/6/24.
 */
public class ModelHub extends Service {

    public static final String TAG = "ModelHub";

    private LocationHelper mLocationHelper;

    private static final long UPDATE_INTERVAL = 2000;

    private Timer mEngineTimer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationHelper = new LocationHelper();
        mLocationHelper.init();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mEngineTimer.cancel();
        mLocationHelper.destroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    // Function
    private DataRequest.OnDataRequestListener mOnSearchListener;
    public void setOnSearchListener(DataRequest.OnDataRequestListener listener) {
        mOnSearchListener = listener;
    }

    public void reportSelf() {
        mLocationHelper.getLocation(new LocationHelper.OnLocationListener() {
            @Override
            public void onLocation(Location location) {
                // Report location only
                DataRequest dataRequest = new DataRequest(new Data(location), null);   // Do NOT concern network response
                dataRequest.send();
            }
        });
    }
    public void searchNearby() {
        mLocationHelper.getLocation(new LocationHelper.OnLocationListener() {
            @Override
            public void onLocation(Location location) {
                // Report location & get network result
                DataRequest dataRequest = new DataRequest(new Data(location), mOnSearchListener);
                dataRequest.send();
            }
        });
    }

    private TimerTask mReportSelfTimerTask;
    public boolean isAutoReport() {
        return mReportSelfTimerTask != null;
    }
    public void startAutoReportSelf() {
        if (mReportSelfTimerTask == null) {
            mReportSelfTimerTask = new TimerTask() {
                @Override
                public void run() {
                    reportSelf();
                }
            };
            mEngineTimer.schedule(mReportSelfTimerTask, 0, UPDATE_INTERVAL);
        }
    }
    public void stopAutoReportSelf() {
        if (mReportSelfTimerTask != null) {
            mReportSelfTimerTask.cancel();
            mReportSelfTimerTask = null;
            mEngineTimer.purge();
        }
    }

    private TimerTask mSearchOthersTimerTask;
    public boolean isAutoSearch() {
        return mSearchOthersTimerTask != null;
    }
    public void startAutoSearchNearby() {
        if (mSearchOthersTimerTask == null) {
            mSearchOthersTimerTask = new TimerTask() {
                @Override
                public void run() {
                    searchNearby();
                }
            };
            mEngineTimer.schedule(mSearchOthersTimerTask, 0, UPDATE_INTERVAL);
        }
    }
    public void stopAutoSearchNearby() {
        if (mSearchOthersTimerTask != null) {
            mSearchOthersTimerTask.cancel();
            mSearchOthersTimerTask = null;
            mEngineTimer.purge();
        }
    }

    //
    public static final Intent SERVICE_INTENT = new Intent(MeetUApplication.getContext(), ModelHub.class);
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