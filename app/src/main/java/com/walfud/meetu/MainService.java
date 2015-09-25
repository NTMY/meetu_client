package com.walfud.meetu;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.walfud.common.ServiceBinder;
import com.walfud.meetu.manager.LocationManager;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.manager.UserManager;

import org.meetu.client.handler.MeetuHandler;
import org.meetu.client.listener.MeetuListener;
import org.meetu.client.listener.MeetuUploadListener;
import org.meetu.dto.BaseDto;
import org.meetu.model.LocationCurr;
import org.meetu.util.ListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by song on 2015/6/24.
 */
public class MainService extends Service {

    public static final String TAG = "MainService";
    public static final String EXTRA_READ_SETTING = "EXTRA_READ_SETTING";
    private static final long UPDATE_INTERVAL = 10 * 60 * 1000; // 10 min

    private LocationManager mLocationManager;
    private Timer mEngineTimer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_REDELIVER_INTENT;
        }

        mLocationManager = LocationManager.getInstance();
        mLocationManager.init();

        if (intent.getBooleanExtra(EXTRA_READ_SETTING, true)) {
            if (PrefsManager.getInstance().isAutoReport()) {
                startAutoReportSelf();
            }
            if (PrefsManager.getInstance().isAutoSearch()) {
                startAutoSearchNearby();
            }
        }

        Toast.makeText(MeetUApplication.getContext(), "Nice to Meet U", Toast.LENGTH_SHORT).show();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mEngineTimer.cancel();
        mLocationManager.destroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    // Function
    public interface OnDataRequestListener {
        void onStartSearch();

        void onNoFriendNearby();

        void onFoundFriends(List<LocationCurr> nearbyFriendList);

        void onError(int errorCode);
    }

    private OnDataRequestListener mOnSearchListener;
    public void setOnSearchListener(OnDataRequestListener listener) {
        mOnSearchListener = listener;
    }

    public void reportSelf() {
        mLocationManager.getLocation(new LocationManager.OnLocationListener() {
            @Override
            public void onLocation(AMapLocation aMapLocation) {
                // Report location only
                new AsyncTask<AMapLocation, Void, BaseDto>() {

                    private BaseDto mBaseDto;
                    private MeetuUploadListener mUploadListener = new MeetuUploadListener() {
                        @Override
                        public void upload(BaseDto baseDto) {
                            mBaseDto = baseDto;
                        }
                    };

                    @Override
                    protected BaseDto doInBackground(AMapLocation... params) {
                        try {
                            AMapLocation aMapLocation = params[0];

                            LocationCurr locationCurr = new LocationCurr();
                            locationCurr.setUserId(UserManager.getInstance().getCurrentUser().getUserId().intValue());
                            locationCurr.setLatitude(aMapLocation.getLatitude());
                            locationCurr.setLongitude(aMapLocation.getLongitude());
                            locationCurr.setAddress(aMapLocation.getAddress());
                            locationCurr.setBusiness(aMapLocation.getDistrict());

                            if (locationCurr.getLatitude() != 0
                                    && locationCurr.getLongitude() != 0) {
                                new MeetuHandler().onUpload(mUploadListener, locationCurr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return mBaseDto;
                    }
                }.execute(aMapLocation);
            }
        });
    }

    public void searchNearby() {
        mLocationManager.getLocation(new LocationManager.OnLocationListener() {
            @Override
            public void onLocation(AMapLocation aMapLocation) {
                // Report location & get network result
                new AsyncTask<AMapLocation, Void, List<LocationCurr>>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        if (mOnSearchListener != null) {
                            mOnSearchListener.onStartSearch();
                        }
                    }

                    @Override
                    protected List<LocationCurr> doInBackground(AMapLocation... params) {
                        final List<LocationCurr> locationCurrList = new ArrayList<>();

                        try {
                            AMapLocation aMapLocation = params[0];

                            LocationCurr locationCurr = new LocationCurr();
                            locationCurr.setUserId(UserManager.getInstance().getCurrentUser().getUserId().intValue());
                            locationCurr.setLatitude(aMapLocation.getLatitude());
                            locationCurr.setLongitude(aMapLocation.getLongitude());
                            locationCurr.setAddress(aMapLocation.getAddress());
                            locationCurr.setBusiness(aMapLocation.getDistrict());

                            if (locationCurr.getLatitude() != 0
                                    && locationCurr.getLongitude() != 0) {
                                new MeetuHandler().onMeetu(new MeetuListener() {
                                    @Override
                                    public void meetu(ListBean listBean) {
                                        if (listBean != null && listBean.getList() != null) {
                                            locationCurrList.addAll(listBean.getList());
                                        }
                                    }
                                }, locationCurr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return locationCurrList;
                    }

                    @Override
                    protected void onPostExecute(List<LocationCurr> locationCurrList) {
                        super.onPostExecute(locationCurrList);

                        if (mOnSearchListener != null) {
                            if (locationCurrList != null && !locationCurrList.isEmpty()) {
                                mOnSearchListener.onFoundFriends(locationCurrList);
                            } else {
                                mOnSearchListener.onNoFriendNearby();
                            }
                        }
                    }
                }.execute(aMapLocation);
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
            mEngineTimer.schedule(mReportSelfTimerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
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
            mEngineTimer.schedule(mSearchOthersTimerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
        }
    }

    public void stopAutoSearchNearby() {
        if (mSearchOthersTimerTask != null) {
            mSearchOthersTimerTask.cancel();
            mSearchOthersTimerTask = null;
            mEngineTimer.purge();
        }
    }

    // Helper
    public static final Intent SERVICE_INTENT = new Intent(MeetUApplication.getContext(), MainService.class);
    public static void startService(Bundle extras) {
        if (extras != null) {
            SERVICE_INTENT.putExtras(extras);
        }

        MeetUApplication.getContext().startService(SERVICE_INTENT);
    }

    /**
     * Start service and do action by preference value
     */
    public static void startServiceWithSetting() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_READ_SETTING, true);

        startService(bundle);
    }
    /**
     * Start service and do nothing ( ignore preference )
     */
    public static void startServiceIgnoreSetting() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_READ_SETTING, false);

        MainService.startService(bundle);
    }

    public static void stopService() {
        MeetUApplication.getContext().stopService(SERVICE_INTENT);
    }

    public static boolean isServiceRunning() {
        return Utils.isServiceRunning(MeetUApplication.getContext(), SERVICE_INTENT);
    }

    public static void setAutoReport(Context context, boolean autoReport) {
        setService(context, autoReport, FLAG_AUTO_REPORT);
    }

    // Internal
    private static final int FLAG_AUTO_REPORT = 1 << 0;
    private static void setService(Context context, boolean value, final int flag) {
        context.bindService(SERVICE_INTENT, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MainService mainService = ((ServiceBinder<MainService>) service).getService();

                switch (flag) {
                    case FLAG_AUTO_REPORT: {
                        mainService.startAutoReportSelf();
                    }
                        break;
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Snackbar.make(null, "bind service failed", Snackbar.LENGTH_INDEFINITE).show();
            }
        }, 0);
    }
}