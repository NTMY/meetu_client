package walfud.meetu.presenter;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.model.Data;
import walfud.meetu.model.DataRequest;
import walfud.meetu.model.ModelHub;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private ModelHub mModelHub;
    private ServiceConnection mEngineServiceConnection = new ServiceConnection() {

        private DataRequest.OnDataRequestListener mOnSearchListener = new DataRequest.OnDataRequestListener() {
            @Override
            public void onNoFriendNearby() {
                mView.showSearchResult(new ArrayList<Data>());
            }

            @Override
            public void onFoundFriends(List<Data> nearbyFriendList) {
                mView.showSearchResult(nearbyFriendList);
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

            // Update Main UI
            mView.setAutoReportSwitch(mModelHub.isAutoReport());
            mView.setAutoSearchSwitch(mModelHub.isAutoSearch());
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
    }
    public void onClickExit() {
        release(true);
        mView.finish();
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
