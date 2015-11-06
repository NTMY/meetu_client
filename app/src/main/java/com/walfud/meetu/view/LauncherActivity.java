package com.walfud.meetu.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.walfud.common.PermissionUtils;
import com.walfud.common.collection.CollectionUtil;
import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.Constants;
import com.walfud.meetu.MainService;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.manager.PrefsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/8/18.
 */
public class LauncherActivity extends BaseActivity {

    public static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find all required permissions
        List<String> requestPermList = new ArrayList<>();
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] allPerms = packageInfo.requestedPermissions;

            for (String perm : allPerms) {
                // Required
                if (PermissionUtils.isDangerousPerm(perm)
                        && ActivityCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_DENIED) {
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
//                        // If permission is denied forever, exit
//                        showFailureTip("Please grant all permission to me -_-...");
//                        return;
//                    }

                    requestPermList.add(perm);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (requestPermList.isEmpty()) {
            // All perms granted
            launch();
        } else {
            // Request lacked of permission
            ActivityCompat.requestPermissions(this, CollectionUtil.toStrings(requestPermList), Constants.REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_PERMISSION:
                // Check all permissions granted
                for (int i = 0; i < permissions.length; i++) {
                    String perm = permissions[i];
                    boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    if (!granted) {
                        // If lack of some permission, onExit
                        showFailureTip("Please grant all permission to me -_-...");
                        return;
                    }
                }

                launch();
                break;

            default:
                break;
        }
    }

    // Internal
    private void launch() {
        // Check update
        UmengUpdateAgent.update(MeetUApplication.getContext());

        if (false) {
        } else if (PrefsManager.getInstance().getShowSplash()) {
            SplashActivity.startActivity(this);
        } else {
            LoginActivity.startActivity(this);
        }

        MainService.startServiceIgnoreSetting();

        finish();
    }

    private void showFailureTip(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
        finish();
        return;
    }
}