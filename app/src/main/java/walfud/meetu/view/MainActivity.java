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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.kanak.emptylayout.EmptyLayout;

import org.meetu.model.LocationCurr;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import walfud.meetu.Constants;
import walfud.meetu.R;
import walfud.meetu.StaticHandler;
import walfud.meetu.model.ParcelableUser;
import walfud.meetu.presenter.MainActivityPresenter;


public class MainActivity extends RoboActivity
        implements View.OnClickListener, StaticHandler.OnHandleMessage {

    public static final String TAG = "MainActivity";

    private MainActivityPresenter mPresenter;
    private EmptyLayout mFriendList;

    @InjectView(R.id.radar_view)
    private Button mRadarView;
    @InjectView(R.id.nearby_friends_list)
    private ListView mNearbyFriendsListView;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation)
    private MaterialMenuView mNavigation;

    // Navigation
    @InjectView(R.id.navigation_layout)
    private LinearLayout mNavLayout;
    @InjectView(R.id.user_id)
    private TextView mUserId;
    @InjectView(R.id.current_location)
    private TextView mLocation;
    @InjectView(R.id.user_name)
    private EditText mUserName;
    @InjectView(R.id.say_hi)
    private EditText mSayHi;
    private Switch mAutoReport;
    private Switch mAutoSearch;
    @InjectView(R.id.feedback)
    private Button mFeedback;
    @InjectView(R.id.exit)
    private Button mExit;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            autoReportDescription.setText("自动更新我的位置");

            mAutoReport = (Switch) autoReportLayout.findViewById(R.id.toggle);
        }
        {
            RelativeLayout autoSearchLayout = (RelativeLayout) findViewById(R.id.auto_search);
            TextView autoSearchDescription = (TextView) autoSearchLayout.findViewById(R.id.description);
            autoSearchDescription.setText("自动搜索附近好友");

            mAutoSearch = (Switch) autoSearchLayout.findViewById(R.id.toggle);
        }

        mPresenter = new MainActivityPresenter(this);
        mPresenter.init();
        mPresenter.setUser(((ParcelableUser) getIntent().getParcelableExtra(Constants.KEY_USER)));
        mRadarView.setOnClickListener(this);
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
            case R.id.radar_view:
                mPresenter.onClickRadarView();
                break;

            case R.id.navigation:
                mPresenter.onClickNavigation();
                break;

            case R.id.feedback:
                mPresenter.onClickFeedback();
                break;

            case R.id.exit:
                mPresenter.onClickExit();
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
    public void handleMessage(Message msg) {
        switch (msg.what) {
            default:
                break;
        }
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
