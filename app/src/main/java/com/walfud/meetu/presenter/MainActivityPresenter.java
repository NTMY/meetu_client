package com.walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.walfud.common.ServiceBinder;
import com.walfud.meetu.MainService;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;
import com.walfud.meetu.Utils;
import com.walfud.meetu.view.FriendFragment;
import com.walfud.meetu.view.MainActivity;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mMainActivity;
    private FriendFragment mFriendFragment;
    private MainService mMainService;
    private ServiceConnection mEngineServiceConnection = new ServiceConnection() {

        private MainService.OnDataRequestListener mOnSearchListener = new MainService.OnDataRequestListener() {
            @Override
            public void onStartSearch() {
                mMainActivity.showSearching();
            }

            @Override
            public void onNoFriendNearby() {
                mMainActivity.showSearchResult(new ArrayList<LocationCurr>());
            }

            @Override
            public void onFoundFriends(List<LocationCurr> nearbyFriendList) {
                mMainActivity.showSearchResult(nearbyFriendList);

                // Notify
                Intent intent = new Intent(MeetUApplication.getContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MeetUApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Utils.showNotification(MeetUApplication.getContext(), Utils.NOTIFICATION_ID, pendingIntent, null, R.drawable.portrait,
                        String.format("%d friends nearby", nearbyFriendList.size()), null, null, null);
            }

            @Override
            public void onError(int errorCode) {
                Toast.makeText(MeetUApplication.getContext(), String.format("DataRequest.onError(%d)", errorCode), Toast.LENGTH_LONG).show();
            }
        };

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMainService = ((ServiceBinder<MainService>) service).getService();

            // Init model
            mMainService.setOnSearchListener(mOnSearchListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainService = null;
        }
    };

    public MainActivityPresenter(MainActivity view, FriendFragment friendFragment) {
        mMainActivity = view;
        mFriendFragment = friendFragment;
    }

    // View Event
    public void search() {
        mMainService.searchNearby();
    }

    // Presenter Function
    public void init() {
        if (mMainService == null) {
            MainService.startServiceIgnoreSetting();
            MeetUApplication.getContext().bindService(MainService.SERVICE_INTENT, mEngineServiceConnection, 0);
        }
    }

    /**
     *
     * @param stopService `false` if only unbind service, `true` will unbind and stop service.
     */
    public void release(boolean stopService) {
        if (mMainService != null) {
            MeetUApplication.getContext().unbindService(mEngineServiceConnection);
            mMainService = null;
        }

        if (stopService) {
            if (MainService.isServiceRunning()) {
                MainService.stopService();
            }
        }
    }
}
