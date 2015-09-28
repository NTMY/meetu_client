package com.walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.walfud.common.collection.CollectionUtil;
import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainActivityPresenter;
import com.walfud.meetu.util.Transformer;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private ActionBarDrawerToggle mDrawerToggle;

    // Fragment
    private FriendFragment mFriendFragment;

    // Content
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private CoordinatorLayout mContentLayout;
    private FrameLayout mFragmentLayout;
    private FloatingActionButton mFab;

    // Navigation
    private NavigationView mNavigation;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = $(R.id.dl);
        mToolbar = $(R.id.tb);
        mContentLayout = $(R.id.cl_content);
        mFragmentLayout = $(R.id.fl_fragment);
        mFriendFragment = (FriendFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search);
        mFab = $(R.id.fab_search);
        mNavigation = $(R.id.nvg);

        //
        mPresenter = new MainActivityPresenter(this);
        mPresenter.init();

        //
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigation.setNavigationItemSelectedListener(this);
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

        mPresenter.release(false);      // Just unbind service but keep service running
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_search:
                mPresenter.search();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_profile:
                break;

            case R.id.menu_setting:
                SettingActivity.startActivity(this, null);
                break;

            default:
                break;

        }

        return true;
    }

    // View Function
    public void showSearchResult(List<LocationCurr> locationList) {
        if (CollectionUtil.isEmpty(locationList)) {
            // No friend nearby
        } else {
            //
            List<FriendFragment.FriendData> friendList = new ArrayList<>();
            for (LocationCurr locationCurr : locationList) {
                friendList.add(Transformer.locationCurr2FriendData(this, locationCurr));
            }

            mFriendFragment.setFriendList(friendList);
        }
    }

    public void showSearching() {
    }

    //
    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
