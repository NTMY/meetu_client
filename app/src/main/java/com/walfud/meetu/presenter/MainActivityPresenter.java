package com.walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.walfud.meetu.MainService;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;
import com.walfud.meetu.Utils;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.view.FriendFragment;
import com.walfud.meetu.view.MainActivity;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mMainActivity;
    private FriendFragment mFriendFragment;
    private MainService mMainService;
    private PrefsManager mPrefsManager;

    public MainActivityPresenter(MainActivity view, FriendFragment friendFragment) {
        mMainActivity = view;
        mFriendFragment = friendFragment;
        mMainService = MainService.getInstance();
        mPrefsManager = PrefsManager.getInstance();

        //
        mMainService.setOnSearchListener(new MainService.OnDataRequestListener() {
            @Override
            public void onStartSearch() {
                mMainActivity.showSearching(true);
            }

            @Override
            public void onNoFriendNearby() {
                mMainActivity.showSearching(false);
                mMainActivity.showSearchResult(new ArrayList<LocationCurr>());
            }

            @Override
            public void onFoundFriends(List<LocationCurr> nearbyFriendList) {
                mMainActivity.showSearching(false);
                mMainActivity.showSearchResult(nearbyFriendList);

                // Notify
                Intent intent = new Intent(MeetUApplication.getContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MeetUApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Utils.showNotification(MeetUApplication.getContext(), Utils.NOTIFICATION_ID, pendingIntent, null, R.drawable.portrait,
                        String.format("%d friends nearby", nearbyFriendList.size()), null, null, null);
            }

            @Override
            public void onError(int errorCode) {
                mMainActivity.showSearching(false);
                mMainActivity.showSearchResult(new ArrayList<LocationCurr>());
                Toast.makeText(MeetUApplication.getContext(), String.format("DataRequest.onError(%d)", errorCode), Toast.LENGTH_LONG).show();
            }
        });
        mMainService.configWithSetting();
    }

    // Function
    public void search() {
        mMainService.search();
    }

    public void exit() {
        MainService.stopService();
        Utils.clearNotification(mMainActivity, Utils.NOTIFICATION_ID);
        mMainActivity.finish();
    }
}
