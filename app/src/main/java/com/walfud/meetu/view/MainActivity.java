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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kanak.emptylayout.EmptyLayout;
import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainActivityPresenter;

import org.meetu.model.LocationCurr;

import java.util.List;


public class MainActivity extends BaseActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private EmptyLayout mFriendList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Content
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mContentLayout;
    private Toolbar mToolbar;
    private ListView mNearbyFriendsListView;
    private FloatingActionButton mSearch;

    // Navigation
    private NavigationView mNavigation;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = $(R.id.dl);
        mContentLayout = $(R.id.cl_content);
        mToolbar = $(R.id.tb);
        mNearbyFriendsListView = $(R.id.lv_nearby_friend_list);
        mSearch = $(R.id.fab_search);
        mNavigation = $(R.id.nvg);

        //
        mPresenter = new MainActivityPresenter(this);
        mPresenter.init();

        mFriendList = new EmptyLayout(this, mNearbyFriendsListView);

        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
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
//        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return true;
    }

    // View Function
    public void showSearchResult(List<LocationCurr> friendList) {

        if (friendList.size() == 0) {
            // No friend nearby
            mFriendList.showEmpty();
        } else {
            String[] nearbyFriends = new String[friendList.size()];
            for (int i = 0; i < nearbyFriends.length; i++) {
                nearbyFriends[i] = String.valueOf(friendList.get(i).getUserId());
            }

            mNearbyFriendsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nearbyFriends));
        }
    }

    public void showSearching() {
        mFriendList.showLoading();
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
