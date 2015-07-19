package walfud.meetu.presenter;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

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
    private LocationService mLocationService;

    private ServiceConnection mServiceConnection;

    public MainActivityPresenter(MainActivity view) {
        mView = view;
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocationService = ((ServiceBinder<LocationService>) service).getService();

                if (mLocationService != null) {
                    mView.onBindingSuccess();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mLocationService = null;
            }
        };



        // Start service
        LocationService.startService();
        MeetUApplication.getContext().bindService(LocationService.SERVICE_INTENT, mServiceConnection, 0);
//        MeetUApplication.getContext().unbindService(mServiceConnection);
//        LocationService.stopService();
    }

    // View Event
    public void onRadarViewClick() {
        Location location = mLocationService.getLocation();
        DataRequest dataRequest = new DataRequest(new Data(location), mView);
        dataRequest.send();
    }

    // Presenter Function
}
