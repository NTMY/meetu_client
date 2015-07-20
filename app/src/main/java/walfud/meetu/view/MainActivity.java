package walfud.meetu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import walfud.meetu.R;
import walfud.meetu.model.Data;
import walfud.meetu.model.DataRequest;
import walfud.meetu.presenter.MainActivityPresenter;


public class MainActivity extends Activity implements View.OnClickListener, DataRequest.OnDataRequestListener {

    private RadarView mRadarView;
    private ListView mNearbyFriendsListView;
    private MainActivityPresenter mPresenter;

    // Event bus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadarView = (RadarView) findViewById(R.id.radar_view);
        mNearbyFriendsListView = (ListView) findViewById(R.id.nearby_friends_list);

        mPresenter = new MainActivityPresenter(this);
        mRadarView.setOnClickListener(this);
        mRadarView.start();
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
            case R.id.radar_stub:
                mPresenter.onRadarViewClick();
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
        mNearbyFriendsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[] {}));
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
}
