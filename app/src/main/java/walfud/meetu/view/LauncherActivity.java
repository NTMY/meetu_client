package walfud.meetu.view;

import android.app.Activity;
import android.os.Bundle;

import walfud.meetu.model.PrefsModel;

/**
 * Created by walfud on 2015/8/18.
 */
public class LauncherActivity extends Activity {

    public static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (false) {
        } else if (PrefsModel.getInstance().getShowSplash()) {
            SplashActivity.startActivity(this);
        } else {
            LoginActivity.startActivity(this);
        }

        finish();
    }
}
