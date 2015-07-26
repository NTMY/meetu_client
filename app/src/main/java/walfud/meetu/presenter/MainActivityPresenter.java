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
import walfud.meetu.model.Model;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private Model mModel;
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
            mModel = ((ServiceBinder<Model>) service).getService();

            // Init model
            mModel.setOnSearchListener(mOnSearchListener);

            // Update Main UI
            mView.setAutoReportSwitch(mModel.isAutoReport());
            mView.setAutoSearchSwitch(mModel.isAutoSearch());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mModel = null;
        }
    };

    public MainActivityPresenter(MainActivity view) {
        mView = view;
    }

    // View Event
    public void onClickRadarView() {
        mModel.searchNearby();
    }

    public void onClickNavigation() {
        mView.switchNavigation();
    }

    public void onClickAutoReport(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mModel.startAutoReportSelf();
        } else {
            mModel.stopAutoReportSelf();
        }
    }
    public void onClickAutoSearch(boolean isChecked) {
        if (!checkModelBind()) {
            return;
        }

        if (isChecked) {
            mModel.startAutoSearchNearby();
        } else {
            mModel.stopAutoSearchNearby();
        }
    }
    public void onClickExit() {
        release(true);
        mView.finish();
    }

    // Presenter Function
    public void init() {
        if (mModel == null) {
            Model.startService();
            MeetUApplication.getContext().bindService(Model.SERVICE_INTENT, mEngineServiceConnection, 0);
        }
    }

    /**
     *
     * @param stopService `false` if only unbind service, `true` will unbind and stop service.
     */
    public void release(boolean stopService) {
        if (mModel != null) {
            MeetUApplication.getContext().unbindService(mEngineServiceConnection);
            mModel = null;
        }

        if (stopService) {
            if (Model.isServiceRunning()) {
                Model.stopService();
            }
        }
    }

    //
    /**
     * Check if the model service has been bound successfully.
     */
    private boolean checkModelBind() {
        if (mModel == null) {
            Toast.makeText(MeetUApplication.getContext(), "Model Service Unbinding", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
