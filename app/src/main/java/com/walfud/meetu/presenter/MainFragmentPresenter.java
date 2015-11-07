package com.walfud.meetu.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.walfud.meetu.MainService;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.R;
import com.walfud.meetu.Statistics;
import com.walfud.meetu.Utils;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.util.Transformer;
import com.walfud.meetu.view.MainActivity;
import com.walfud.meetu.view.MainFragment;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/11/6.
 */
public class MainFragmentPresenter {

    public static final String TAG = "MainFragmentPresenter";

    private MainFragment mView;
    private MainService mMainService;
    private PrefsManager mPrefsManager;

    public MainFragmentPresenter(MainFragment view) {
        mView = view;
        mMainService = MainService.getInstance();
        mPrefsManager = PrefsManager.getInstance();

        //
        mMainService.setOnSearchListener(new MainService.OnDataRequestListener() {
            @Override
            public void onStartSearch() {
                mView.showSearching(true);
            }

            @Override
            public void onStopSearch() {
                mView.showSearching(false);
            }

            @Override
            public void onFoundFriends(List<LocationCurr> nearbyFriendLocationList) {
                // Transform to ui data
                List<MainFragment.NearbyFriendData> nearbyFriendDataList = Transformer.locationCurr2NearbyFriendData(nearbyFriendLocationList);

                mView.showNearbyFriend(nearbyFriendDataList);

                // Notify
                if (!nearbyFriendDataList.isEmpty()) {
                    Intent intent = new Intent(MeetUApplication.getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    List<Long> nearbyFriendIds = new ArrayList<>();
                    for (MainFragment.NearbyFriendData nearbyFriendData : nearbyFriendDataList) {
                        nearbyFriendIds.add(nearbyFriendData.userId);
                    }
                    MainActivity.setNearbyFriendIdsExtra(intent, nearbyFriendIds);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MeetUApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Utils.showNotification(MeetUApplication.getContext(), Utils.NOTIFICATION_ID, pendingIntent, null, R.drawable.ic_favorite_border_white_48dp,
                            String.format("%d friends nearby", nearbyFriendDataList.size()), "click here to meet each other", null, null);
                }
            }

            @Override
            public void onError(int errorCode) {
                mView.showNearbyFriend(new ArrayList<MainFragment.NearbyFriendData>());
                Toast.makeText(MeetUApplication.getContext(), String.format("DataRequest.onError(%d)", errorCode), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Function
    public void onSearch() {
        mMainService.search();

        Statistics.getInstance().search();
    }
}
