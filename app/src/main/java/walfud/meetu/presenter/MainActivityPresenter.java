package walfud.meetu.presenter;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.model.Data;
import walfud.meetu.model.DataRequest;
import walfud.meetu.model.LocationService;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private LocationService mModel;

    private ServiceConnection mServiceConnection;

    public MainActivityPresenter(MainActivity view) {
        mView = view;
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mModel = ((ServiceBinder<LocationService>) service).getService();

                if (mModel != null) {
                    mView.onBindingSuccess();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mModel = null;
            }
        };

        if (LocationService.isServiceRunning()) {
            MeetUApplication.getContext().bindService(LocationService.SERVICE_INTENT, mServiceConnection, 0);
        }
    }

    // View Event
    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            LocationService.startService();
            MeetUApplication.getContext().bindService(LocationService.SERVICE_INTENT, mServiceConnection, 0);
        } else {
            MeetUApplication.getContext().unbindService(mServiceConnection);
            LocationService.stopService();
        }
    }

    public void onClick() {
        // TODO: debug
        Location location = mModel.getLocation();
        DataRequest dataRequest = new DataRequest(new Data(location), null);
        dataRequest.send();
        Toast.makeText(MeetUApplication.getContext(), dataRequest.toUrlRequest(), Toast.LENGTH_SHORT).show();
    }

    // Presenter Function
    public boolean getServiceState() {
        return LocationService.isServiceRunning();
    }
}
