package walfud.meetu.presenter;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;
import walfud.meetu.model.Foo;
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
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mModel = null;
            }
        };

        if (getServiceState()) {
            MeetUApplication.getApplication().bindService(LocationService.SERVICE_INTENT, mServiceConnection, 0);
        }
    }

    // On Event
    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            LocationService.startService();
            MeetUApplication.getApplication().bindService(LocationService.SERVICE_INTENT, mServiceConnection, 0);
        } else {
            MeetUApplication.getApplication().unbindService(mServiceConnection);
            LocationService.stopService();
        }
    }

    public void onClick() {
        Location location = mModel.getLocation();
        String httpRequest = Foo.toUrlRequest(location);

        Toast.makeText(MeetUApplication.getApplication(), String.format("request(%s)",
                        httpRequest),
                Toast.LENGTH_SHORT).show();

        Foo.httpPost(httpRequest, new Foo.onHttpPostResponse() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MeetUApplication.getApplication(), String.format("response(%s)",
                                response),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Presenter Function
    public boolean getServiceState() {
        return Utils.isServiceRunning(MeetUApplication.getApplication(), LocationService.SERVICE_INTENT);
    }
}
