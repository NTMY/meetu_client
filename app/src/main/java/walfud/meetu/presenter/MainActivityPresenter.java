package walfud.meetu.presenter;

import android.util.Log;

import java.util.List;

import walfud.meetu.model.Data;
import walfud.meetu.model.DataRequest;
import walfud.meetu.model.LocationHelper;
import walfud.meetu.model.Model;
import walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mView;
    private Model mModel;

    public MainActivityPresenter(MainActivity view) {
        mView = view;
        mModel = new Model(this);

        //
        mModel.init();
        mModel.startAutoReportSelf();
        mModel.startAutoSearchNearby();
    }

    // View Event
    public void onRadarViewClick() {
        mModel.searchNearby();
    }

    // Presenter Function
    public void setOnSearchListener(DataRequest.OnDataRequestListener listener) {
        mModel.setOnSearchListener(listener);
    }
}
