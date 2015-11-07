package com.walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.walfud.common.collection.CollectionUtils;
import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.database.User;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.presenter.MainActivityPresenter;
import com.walfud.meetu.util.Transformer;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    private static final String EXTRA_NEARBY_FRIEND_IDS = "EXTRA_NEARBY_FRIEND_IDS";

    private MainActivityPresenter mPresenter;
    private ActionBarDrawerToggle mDrawerToggle;

    // Content
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private CoordinatorLayout mContentLayout;
    private FrameLayout mFragmentLayout;
    private MainFragment mMainFragment;

    // Navigation
    private FriendFragment mFriendFragment;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = $(R.id.dl);
        mToolbar = $(R.id.tb);
        mContentLayout = $(R.id.cl_content);
        mFragmentLayout = $(R.id.fl_fragment);
        mMainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
        mFriendFragment = (FriendFragment) getSupportFragmentManager().findFragmentById(R.id.drawer);

        //
        mPresenter = new MainActivityPresenter(this);

        //
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Parse intent
        // Nearby friend
        List<MainFragment.NearbyFriendData> nearbyFriendIdList = getNearbyFriendIdsExtra(getIntent());
        if (!nearbyFriendIdList.isEmpty()) {
            mMainFragment.setNearbyFriend(nearbyFriendIdList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingActivity.startActivity(this, null);
            return true;
        } else if (id == R.id.action_exit) {
            mPresenter.onExit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            moveTaskToBack(true);
        }
    }

    // Function

    // Helper
    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void setNearbyFriendIdsExtra(Intent intent, List<Long> nearbyFriendIdList) {
        Bundle bundle = new Bundle();
        bundle.putLongArray(EXTRA_NEARBY_FRIEND_IDS, CollectionUtils.toLongs(nearbyFriendIdList));
        intent.putExtras(bundle);
    }
    public static List<MainFragment.NearbyFriendData> getNearbyFriendIdsExtra(Intent intent) {
        List<MainFragment.NearbyFriendData> nearbyFriendDataList = new ArrayList<>();

        long[] nearbyFriendIds = intent.getLongArrayExtra(EXTRA_NEARBY_FRIEND_IDS);
        if (nearbyFriendIds != null) {
            for (long userId : nearbyFriendIds) {
                User user = UserManager.getInstance().getUser(userId);
                nearbyFriendDataList.add(Transformer.user2NearbyFriendData(user));
            }
        }

        return nearbyFriendDataList;
    }
}
