package walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import walfud.meetu.BaseActivity;
import walfud.meetu.R;
import walfud.meetu.manager.PrefsManager;

/**
 * Created by walfud on 2015/8/18.
 */
public class SplashActivity extends BaseActivity {

    public static final String TAG = "SplashActivity";

    private ViewFlipper mFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mFlipper.setInAnimation(this, android.R.anim.fade_in);
        mFlipper.setOutAnimation(this, android.R.anim.fade_out);
    }

    public void onClick(View v) {
        if (mFlipper.getDisplayedChild() != mFlipper.getChildCount() - 1) {
            mFlipper.showNext();
        } else {
            LoginActivity.startActivity(this);

            PrefsManager.getInstance().setShowSplash(false);
            finish();
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
