package com.walfud.meetu.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kanak.emptylayout.EmptyLayout;
import com.walfud.meetu.R;

import org.meetu.model.LocationCurr;

import java.util.List;

/**
 * Created by walfud on 9/24/15.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    private Activity mActivity;
    private EmptyLayout mEmptyLayout;
    private ListView mNearbyFriendsList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mNearbyFriendsList = (ListView) view.findViewById(R.id.lv_nearby_friend_list);

        //
        mEmptyLayout = new EmptyLayout(mActivity, mNearbyFriendsList);

        return view;
    }

    // Function
    public void showSearchResult(List<LocationCurr> friendList) {
        if (friendList.size() == 0) {
            // No friend nearby
            mEmptyLayout.showEmpty();
        } else {
            String[] nearbyFriends = new String[friendList.size()];
            for (int i = 0; i < nearbyFriends.length; i++) {
                nearbyFriends[i] = String.valueOf(friendList.get(i).getUserId());
            }

            mNearbyFriendsList.setAdapter(new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, nearbyFriends));
        }
    }
    public void showSearching() {
        mEmptyLayout.showLoading();
    }

}
