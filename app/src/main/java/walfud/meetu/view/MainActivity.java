package walfud.meetu.view;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import walfud.meetu.R;
import walfud.meetu.StaticHandler;
import walfud.meetu.model.Data;
import walfud.meetu.presenter.MainActivityPresenter;


public class MainActivity extends Activity
        implements View.OnClickListener, StaticHandler.OnHandleMessage {

    private Button mRadarView;
    private ListView mNearbyFriendsListView;
    private MainActivityPresenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private Button mNavigation;

    // Navigation
    private Switch mAutoReport;
    private Switch mAutoSearch;
    private Button mExit;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadarView = (Button) findViewById(R.id.radar_view);
        mNearbyFriendsListView = (ListView) findViewById(R.id.nearby_friends_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigation = (Button) findViewById(R.id.navigation);
        {
            RelativeLayout autoReportLayout = (RelativeLayout) findViewById(R.id.auto_report);
            TextView autoReportDescription = (TextView) autoReportLayout.findViewById(R.id.description);
            autoReportDescription.setText("自动更新我的位置");

            mAutoReport = (Switch) autoReportLayout.findViewById(R.id.toggle);
        }
        {
            RelativeLayout autoSearchLayout = (RelativeLayout) findViewById(R.id.auto_search);
            TextView autoSearchDescription = (TextView) autoSearchLayout.findViewById(R.id.description);
            autoSearchDescription.setText("自动搜索附近的好友");

            mAutoSearch = (Switch) autoSearchLayout.findViewById(R.id.toggle);
        }
        mExit = (Button) findViewById(R.id.exit);

        mPresenter = new MainActivityPresenter(this);
        mPresenter.init();
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
        mExit.setOnClickListener(this);
        mHandler = new StaticHandler<>(this);
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

    // View Function
    public void showSearchResult(List<Data> friendList) {
        String[] nearbyFriends = new String[friendList.size()];
        for (int i = 0; i < nearbyFriends.length; i++) {
            nearbyFriends[i] = friendList.get(i).getImei();
        }

        mNearbyFriendsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nearbyFriends));
    }

    private boolean mIsNavShowing = false;
    public void switchNavigation() {
        if (mIsNavShowing) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        mIsNavShowing = !mIsNavShowing;
    }

    public void setAutoReportSwitch(boolean check) {
        mAutoReport.setChecked(check);
    }
    public void setAutoSearchSwitch(boolean check) {
        mAutoSearch.setChecked(check);
    }

    public Handler getMainActivityHandler() {
        return mHandler;
    }
}
