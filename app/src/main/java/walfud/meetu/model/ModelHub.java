package walfud.meetu.model;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;

import org.meetu.client.handler.MeetuHandler;
import org.meetu.client.listener.MeetuListener;
import org.meetu.client.listener.MeetuUploadListener;
import org.meetu.dto.BaseDto;
import org.meetu.model.LocationCurr;
import org.meetu.model.User;
import org.meetu.util.ListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/24.
 */
public class ModelHub extends Service {

    public static final String TAG = "ModelHub";

    private LocationHelper mLocationHelper;

    private static final long UPDATE_INTERVAL = 10 * 60 * 1000; // 10 min

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
    public static final int ERROR_UNKNOWN = 0;

    public interface OnDataRequestListener {
        void onNoFriendNearby();

        void onFoundFriends(List<Data> nearbyFriendList);

        void onError(int errorCode);
    }

    private OnDataRequestListener mOnSearchListener;
    public void setOnSearchListener(OnDataRequestListener listener) {
        mOnSearchListener = listener;
    }

    private User mUser;
    public void setUser(User user) {
        mUser = user;
    }

    public void reportSelf() {
        mLocationHelper.getLocation(new LocationHelper.OnLocationListener() {
            @Override
            public void onLocation(final Location location) {
                // Report location only
                new AsyncTask<Void, Void, BaseDto>() {

                    private BaseDto mBaseDto;
                    private MeetuUploadListener mUploadListener = new MeetuUploadListener() {
                        @Override
                        public void upload(BaseDto baseDto) {
                            mBaseDto = baseDto;
                        }
                    };

                    @Override
                    protected BaseDto doInBackground(Void... params) {
                        try {
                            Data data = new Data(location);
                            data.setUserId(mUser.getId());
                            new MeetuHandler().onUpload(mUploadListener, data.toLocationCurr());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return mBaseDto;
                    }
                }.execute();
            }
        });
    }

    public void searchNearby() {
        mLocationHelper.getLocation(new LocationHelper.OnLocationListener() {
            @Override
            public void onLocation(final Location location) {
                // Report location & get network result
                new AsyncTask<Void, Void, List<LocationCurr>>() {

                    private List<LocationCurr> mLocationCurrList;
                    private MeetuListener mMeetUListener = new MeetuListener() {
                        @Override
                        public void meetu(ListBean listBean) {
                            mLocationCurrList = listBean.getList();
                        }
                    };

                    @Override
                    protected List<LocationCurr> doInBackground(Void... params) {
                        try {
                            new MeetuHandler().onMeetu(mMeetUListener, new Data(mUser.getId(), location).toLocationCurr());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return mLocationCurrList;
                    }

                    @Override
                    protected void onPostExecute(List<LocationCurr> locationCurrs) {
                        super.onPostExecute(locationCurrs);

                        if (locationCurrs != null && mOnSearchListener != null) {
                            if (locationCurrs.size() == 0) {
                                mOnSearchListener.onNoFriendNearby();
                            } else {
                                List<Data> dataList = new ArrayList<>();
                                for (LocationCurr locationCurr : locationCurrs) {
                                    dataList.add(new Data(locationCurr));
                                }
                                mOnSearchListener.onFoundFriends(dataList);
                            }
                        }
                    }
                }.execute();
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


    // Debug
    private MainActivity mMainActivity;

    public void setDebug(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }
}