package com.walfud.meetu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static List<Activity> sActivityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sActivityList.add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Bugtags.onResume(this);

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        Bugtags.onPause(this);

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sActivityList.remove(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

//        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    protected <T extends View> T $(@IdRes int resId) {
        return (T) findViewById(resId);
    }

    // Function
    public void finishAll() {
        for (Activity activity : sActivityList) {
            activity.finish();
        }
    }
}