package walfud.meetu.model;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.presenter.MainActivityPresenter;

/**
 * It's a wrapper class, transit method call to the `EngineService` class.
 * Created by song on 2015/7/26.
 */
public class Model {

    public static final String TAG = "Model";

    private MainActivityPresenter mPresenter;
    private EngineService mEngineService;
    private ServiceConnection mEngineServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mEngineService = ((ServiceBinder<EngineService>) service).getService();

            mEngineService.startAutoReportSelf();
            mEngineService.startAutoSearchNearby();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mEngineService = null;
        }
    };

    public Model(MainActivityPresenter presenter) {
        mPresenter = presenter;
    }

    // Function
    public void init() {
        bindEngineService();
    }
    public void destroy() {
        MeetUApplication.getContext().unbindService(mEngineServiceConnection);
        EngineService.stopService();
    }

    public void setOnSearchListener(DataRequest.OnDataRequestListener listener) {
        mEngineService.setOnSearchListener(listener);
    }

    public void reportSelf() {
        mEngineService.reportSelf();
    }
    public void searchNearby() {
        mEngineService.searchNearby();
    }

    public void startAutoReportSelf() {
        if (mEngineService != null) {
            mEngineService.startAutoReportSelf();
        } else {
            bindEngineService();
        }
    }
    public void stopAutoReportSelf() {
    }

    public void startAutoSearchNearby() {
        if (mEngineService != null) {
            mEngineService.startAutoSearchNearby();
        } else {
            bindEngineService();
        }
    }
    public void stopAutoSearchNearby() {
    }

    private void bindEngineService() {
        EngineService.startService();
        MeetUApplication.getContext().bindService(EngineService.SERVICE_INTENT, mEngineServiceConnection, 0);
    }
}
