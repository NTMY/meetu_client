package walfud.meetu.presenter;

import android.location.Location;
import android.widget.Toast;

import walfud.meetu.MeetUApplication;
import walfud.meetu.model.Foo;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private Foo mModel;

    public MainActivityPresenter(MainActivity view) {
        mView = view;
        mModel = new Foo(this);
    }

    public void onSwitchChanged(boolean isChecked) {
        if (isChecked) {
            mModel.init();
        } else {
            mModel.release();
        }
    }

    public void onClick() {
        Location location = mModel.getLocation();
        String httpRequest = mModel.toUrlRequest(location);

        Toast.makeText(MeetUApplication.getApplication(), String.format("request(%s)",
                        httpRequest),
                Toast.LENGTH_SHORT).show();

        mModel.httpPost(httpRequest, new Foo.onHttpPostResponse() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MeetUApplication.getApplication(), String.format("response(%s)",
                                response),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
