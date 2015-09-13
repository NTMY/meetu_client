package walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import org.meetu.model.LocationCurr;
import org.meetu.model.User;

import java.util.ArrayList;
import java.util.List;

import walfud.meetu.MeetUApplication;
import walfud.meetu.R;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;
import walfud.meetu.model.ModelHub;
import walfud.meetu.model.PrefsModel;
import walfud.meetu.view.FeedbackActivity;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private ModelHub mModelHub;
    private ServiceConnection mEngineServiceConnection = new ServiceConnection() {

        private ModelHub.OnDataRequestListener mOnSearchListener = new ModelHub.OnDataRequestListener() {
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
            mModelHub = ((ServiceBinder<ModelHub>) service).getService();

            // Init model
            mModelHub.setOnSearchListener(mOnSearchListener);
            mModelHub.setDebug(mView);
            mModelHub.setUser(mUser);

            //
            mView.setAutoReportSwitch(PrefsModel.getInstance().isAutoReport());
            mView.setAutoSearchSwitch(PrefsModel.getInstance().isAutoSearch());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mModelHub = null;
        }
    };

    public MainActivityPresenter(MainActivity view) {
        mView = view;
    }

    // View Event
    public void onClickRadarView() {
        mView.showSearching();
        mModelHub.searchNearby();
    }

    public void onClickNavigation() {
        mView.switchNavigation();
    }

    public void onClickAutoReport(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mModelHub.startAutoReportSelf();
        } else {
            mModelHub.stopAutoReportSelf();
        }

        PrefsModel.getInstance().setAutoReport(isChecked);
    }
    public void onClickAutoSearch(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mModelHub.startAutoSearchNearby();
        } else {
            mModelHub.stopAutoSearchNearby();
        }

        PrefsModel.getInstance().setAutoSearch(isChecked);
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
        if (mModelHub == null) {
            ModelHub.startService();
            MeetUApplication.getContext().bindService(ModelHub.SERVICE_INTENT, mEngineServiceConnection, 0);
        }
    }
    /**
     *
     * @param stopService `false` if only unbind service, `true` will unbind and stop service.
     */
    public void release(boolean stopService) {
        if (mModelHub != null) {
            MeetUApplication.getContext().unbindService(mEngineServiceConnection);
            mModelHub = null;
        }

        if (stopService) {
            if (ModelHub.isServiceRunning()) {
                ModelHub.stopService();
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
        if (mModelHub == null) {
            Toast.makeText(MeetUApplication.getContext(), "Model Service Unbinding", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
