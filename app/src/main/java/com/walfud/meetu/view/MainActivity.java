package com.walfud.meetu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.MainActivityPresenter;

import org.meetu.model.LocationCurr;

import java.util.List;


public class MainActivity extends BaseActivity
        implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private ActionBarDrawerToggle mDrawerToggle;
    /**
     * Whether need searching animation
     */
    private boolean mIsSearchingAnimate;

    // Fragment

    // Content
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private CoordinatorLayout mContentLayout;
    private FrameLayout mFragmentLayout;
    private FloatingActionButton mFab;

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
        mFriendFragment = (FriendFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search);
        mFab = $(R.id.fab_search);

        //
        mPresenter = new MainActivityPresenter(this, mFriendFragment);

        //
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mIsSearchingAnimate = false;
        mFab.animate().setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFab.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (mIsSearchingAnimate) {
                    mFab.animate().rotationYBy(180).setInterpolator(new OvershootInterpolator()).setStartDelay(500).setDuration(1000);
                } else {
                    mFab.setEnabled(true);
                }
            }
        });
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
            mPresenter.exit();
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

    // View Function
    public void showSearchResult(List<LocationCurr> locationList) {
    }

    public void showSearching(boolean start) {
        mIsSearchingAnimate = start;
        if (start == false) {
            return;
        }

        mFab.animate().rotationYBy(180).setInterpolator(new OvershootInterpolator()).setDuration(1000);
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
