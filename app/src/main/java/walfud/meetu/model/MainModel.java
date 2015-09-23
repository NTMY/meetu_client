package walfud.meetu.model;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.walfud.common.ServiceBinder;

import org.meetu.client.handler.MeetuHandler;
import org.meetu.client.listener.MeetuListener;
import org.meetu.client.listener.MeetuUploadListener;
import org.meetu.dto.BaseDto;
import org.meetu.model.LocationCurr;
import org.meetu.util.ListBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import walfud.meetu.MeetUApplication;
import walfud.meetu.Utils;
import walfud.meetu.manager.LocationManager;
import walfud.meetu.manager.PrefsManager;
import walfud.meetu.manager.UserManager;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/24.
 */
public class MainModel extends Service {

    public static final String TAG = "MainModel";
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
    public static final int ERROR_UNKNOWN = 0;

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

                    private List<LocationCurr> mLocationCurrList;
                    private MeetuListener mMeetUListener = new MeetuListener() {
                        @Override
                        public void meetu(ListBean listBean) {
                            mLocationCurrList = listBean.getList();
                        }
                    };

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        if (mOnSearchListener != null) {
                            mOnSearchListener.onStartSearch();
                        }
                    }

                    @Override
                    protected List<LocationCurr> doInBackground(AMapLocation... params) {
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
                                new MeetuHandler().onMeetu(mMeetUListener, locationCurr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return mLocationCurrList;
                    }

                    @Override
                    protected void onPostExecute(List<LocationCurr> locationCurrList) {
                        super.onPostExecute(locationCurrList);

                        if (mOnSearchListener != null) {
                            if (locationCurrList == null || locationCurrList.size() == 0) {
                                mOnSearchListener.onNoFriendNearby();
                            } else {
                                mOnSearchListener.onFoundFriends(locationCurrList);
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

    //
    public static final Intent SERVICE_INTENT = new Intent(MeetUApplication.getContext(), MainModel.class);

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

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

        MainModel.startService(bundle);
    }

    public static void stopService() {
        MeetUApplication.getContext().stopService(SERVICE_INTENT);
    }

    public static boolean isServiceRunning() {
        return Utils.isServiceRunning(MeetUApplication.getContext(), SERVICE_INTENT);
    }


    // Debug
    private MainActivity mMainActivity;
}