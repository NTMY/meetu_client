package com.walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.walfud.common.ServiceBinder;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;
import com.walfud.meetu.Utils;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.model.MainModel;
import com.walfud.meetu.view.FeedbackActivity;
import com.walfud.meetu.view.MainActivity;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private MainModel mMainModel;
    private ServiceConnection mEngineServiceConnection = new ServiceConnection() {

        private MainModel.OnDataRequestListener mOnSearchListener = new MainModel.OnDataRequestListener() {
            @Override
            public void onStartSearch() {
                mView.showSearching();
            }

            @Override
            public void onNoFriendNearby() {
                mView.showSearchResult(new ArrayList<LocationCurr>());
            }

            @Override
            public void onFoundFriends(List<LocationCurr> nearbyFriendList) {
                mView.showSearchResult(nearbyFriendList);

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
            mMainModel = ((ServiceBinder<MainModel>) service).getService();

            // Init model
            mMainModel.setOnSearchListener(mOnSearchListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainModel = null;
        }
    };

    public MainActivityPresenter(MainActivity view) {
        mView = view;
    }

    // View Event
    public void search() {
        mMainModel.searchNearby();
    }

    public void setAutoReport(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mMainModel.startAutoReportSelf();
        } else {
            mMainModel.stopAutoReportSelf();
        }

        PrefsManager.getInstance().setAutoReport(isChecked);
    }
    public void setAutoSearch(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mMainModel.startAutoSearchNearby();
        } else {
            mMainModel.stopAutoSearchNearby();
        }

        PrefsManager.getInstance().setAutoSearch(isChecked);
    }

    public void feedback() {
        FeedbackActivity.startActivity(mView);
    }
    public void exit() {
        release(true);
        Utils.clearNotification(MeetUApplication.getContext(), Utils.NOTIFICATION_ID);
        mView.finish();
    }

    // Presenter Function
    public void init() {
        if (mMainModel == null) {
            MainModel.startServiceIgnoreSetting();
            MeetUApplication.getContext().bindService(MainModel.SERVICE_INTENT, mEngineServiceConnection, 0);
        }
    }
    /**
     *
     * @param stopService `false` if only unbind service, `true` will unbind and stop service.
     */
    public void release(boolean stopService) {
        if (mMainModel != null) {
            MeetUApplication.getContext().unbindService(mEngineServiceConnection);
            mMainModel = null;
        }

        if (stopService) {
            if (MainModel.isServiceRunning()) {
                MainModel.stopService();
            }
        }
    }

    //
    /**
     * Check if the model service has been bound successfully.
     */
    private boolean checkModelBind() {
        if (mMainModel == null) {
            Toast.makeText(MeetUApplication.getContext(), "Model Service Unbinding", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
