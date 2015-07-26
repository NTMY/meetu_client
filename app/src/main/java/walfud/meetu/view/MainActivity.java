package walfud.meetu.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import walfud.meetu.R;
import walfud.meetu.model.Data;
import walfud.meetu.model.DataRequest;
import walfud.meetu.presenter.MainActivityPresenter;


public class MainActivity extends Activity implements View.OnClickListener, DataRequest.OnDataRequestListener {

    private Button mRadarView;
    private ListView mNearbyFriendsListView;
    private MainActivityPresenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
        }
    };
    private Button mNavigation;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadarView = (Button) findViewById(R.id.radar_view);
        mNearbyFriendsListView = (ListView) findViewById(R.id.nearby_friends_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigation = (Button) findViewById(R.id.navigation);


        mPresenter = new MainActivityPresenter(this);
        mPresenter.init(this);
        mRadarView.setOnClickListener(this);
        mDrawerLayout.setDrawerListener(mDrawerListener);
        mNavigation.setOnClickListener(this);
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

            default:
                break;
        }
    }

    // View Function
    public void onBindingSuccess() {
        Toast.makeText(this, "Binding service successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFriendNearby() {
        mNearbyFriendsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{}));
    }

    @Override
    public void onFoundFriends(List<Data> nearbyFriendList) {
        String[] nearbyFriends = new String[nearbyFriendList.size()];
        for (int i = 0; i < nearbyFriends.length; i++) {
            nearbyFriends[i] = nearbyFriendList.get(i).getImei();
        }

        mNearbyFriendsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nearbyFriends));
    }

    @Override
    public void onError(int errorCode) {
        Toast.makeText(this, String.format("DataRequest.onError(%d)", errorCode), Toast.LENGTH_LONG).show();
    }

    private boolean mIsNavShowing = false;
    public void switchNavigation() {
        if (mIsNavShowing) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }

        mIsNavShowing = !mIsNavShowing;
    }
}
