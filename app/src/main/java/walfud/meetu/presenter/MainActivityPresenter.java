package walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.walfud.common.ServiceBinder;

import org.meetu.model.LocationCurr;
import org.meetu.model.User;

import java.util.ArrayList;
import java.util.List;

import walfud.meetu.MeetUApplication;
import walfud.meetu.R;
import walfud.meetu.Utils;
import walfud.meetu.manager.PrefsManager;
import walfud.meetu.model.MainModel;
import walfud.meetu.view.FeedbackActivity;
import walfud.meetu.view.MainActivity;

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

            //
            mView.setAutoReportSwitch(PrefsManager.getInstance().isAutoReport());
            mView.setAutoSearchSwitch(PrefsManager.getInstance().isAutoSearch());
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
    public void onClickRadarView() {
        mView.showSearching();
        mMainModel.searchNearby();
    }

    public void onClickNavigation() {
        mView.switchNavigation();
    }

    public void onClickAutoReport(boolean isChecked) {
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
    public void onClickAutoSearch(boolean isChecked) {
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
    public void onClickFeedback() {
        FeedbackActivity.startActivity(mView);
    }
    public void onClickExit() {
        release(true);
        Utils.clearNotification(MeetUApplication.getContext(), Utils.NOTIFICATION_ID);
        mView.finish();
    }

    public void onNavigationClosed() {
        // Save Settings
        // TODO:
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

    private User mUser;
    public void setUser(User user) {
        mUser = user;
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
