package walfud.meetu.presenter;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
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
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mModel = ((ServiceBinder<Model>) service).getService();

            mModel.setOnSearchListener(mOnSearchListener);
            mModel.startAutoReportSelf();
            mModel.startAutoSearchNearby();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mModel = null;
        }
    };

    public MainActivityPresenter(MainActivity view) {
        mView = view;
        mModel = new Model();
    }

    // View Event
    public void onRadarViewClick() {
        mModel.searchNearby();
    }

    // Presenter Function
    private DataRequest.OnDataRequestListener mOnSearchListener;
    public void init(DataRequest.OnDataRequestListener listener) {
        mOnSearchListener = listener;
        bindEngineService();
    }
    public void destory() {
        MeetUApplication.getContext().unbindService(mEngineServiceConnection);
        Model.stopService();
    }

    //
    private void bindEngineService() {
        Model.startService();
        MeetUApplication.getContext().bindService(Model.SERVICE_INTENT, mEngineServiceConnection, 0);
    }
}
