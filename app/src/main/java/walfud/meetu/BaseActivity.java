package walfud.meetu;

import android.view.MotionEvent;

import com.bugtags.library.Bugtags;

import roboguice.activity.RoboActivity;

public class BaseActivity extends RoboActivity {

    @Override
    protected void onResume() {
        super.onResume();

        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }
}