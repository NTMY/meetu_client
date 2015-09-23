package walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.kanak.emptylayout.EmptyLayout;
import com.walfud.common.StaticHandler;

import org.meetu.model.LocationCurr;

import java.util.List;

import walfud.meetu.BaseActivity;
import walfud.meetu.R;
import walfud.meetu.presenter.MainActivityPresenter;


public class MainActivity extends BaseActivity
        implements View.OnClickListener, Handler.Callback {

    public static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private EmptyLayout mFriendList;

    private ListView mNearbyFriendsListView;

    private DrawerLayout mDrawerLayout;
    private MaterialMenuView mNavigation;

    // Navigation
    private LinearLayout mNavLayout;
    private TextView mUserId;
    private Switch mAutoReport;
    private Switch mAutoSearch;
    private Button mFeedback;
    private Button mExit;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNearbyFriendsListView = $(R.id.nearby_friends_list);
        mDrawerLayout = $(R.id.dl_nvg);
        mNavLayout = $(R.id.nvg);
        mNavigation = $(R.id.navigation);
        mUserId = $(R.id.user_id);
        mAutoReport = (Switch) $(R.id.auto_report).findViewById(R.id.toggle);
        mAutoSearch = (Switch) $(R.id.auto_search).findViewById(R.id.toggle);
        mFeedback = $(R.id.feedback);
        mExit = $(R.id.exit);

        mNavigation = (MaterialMenuView) findViewById(R.id.navigation);
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            private boolean isDrawerOpened;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);

                mNavigation.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_CHECK,
                        isDrawerOpened ? 2 - slideOffset : slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;

                mPresenter.onNavigationClosed();
            }
        });
        {
            RelativeLayout autoReportLayout = (RelativeLayout) findViewById(R.id.auto_report);
            TextView autoReportDescription = (TextView) autoReportLayout.findViewById(R.id.description);
            autoReportDescription.setText("Auto Report My Location");

            mAutoReport = (Switch) autoReportLayout.findViewById(R.id.toggle);
        }
        {
            RelativeLayout autoSearchLayout = (RelativeLayout) findViewById(R.id.auto_search);
            TextView autoSearchDescription = (TextView) autoSearchLayout.findViewById(R.id.description);
            autoSearchDescription.setText("Auto Search Nearby Friend");

            mAutoSearch = (Switch) autoSearchLayout.findViewById(R.id.toggle);
        }

        mPresenter = new MainActivityPresenter(this);
        mPresenter.init();
        mNavigation.setOnClickListener(this);
        mAutoReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.onClickAutoReport(isChecked);
            }
        });
        mAutoSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.onClickAutoSearch(isChecked);
            }
        });
        mFeedback.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mHandler = new StaticHandler<>(this);

        mFriendList = new EmptyLayout(this, mNearbyFriendsListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            case R.id.navigation:
                mPresenter.onClickNavigation();
                break;

            case R.id.feedback:
                mPresenter.onClickFeedback();
                break;

            case R.id.exit:
                mPresenter.onClickExit();
                break;

            case R.id.fab_search:
                mPresenter.onClickSearch();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.release(false);      // Just unbind service but keep service running
    }

    private StaticHandler<MainActivity> mHandler;
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
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

    public void switchNavigation() {
        if (mDrawerLayout.isDrawerOpen(mNavLayout)) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public int getUserId() {
        return Integer.valueOf(mUserId.getText().toString());
    }

    public void setAutoReportSwitch(boolean check) {
        mAutoReport.setChecked(check);
    }
    public void setAutoSearchSwitch(boolean check) {
        mAutoSearch.setChecked(check);
    }

    public void showSearching() {
        mFriendList.showLoading();
    }

    public Handler getMainActivityHandler() {
        return mHandler;
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
