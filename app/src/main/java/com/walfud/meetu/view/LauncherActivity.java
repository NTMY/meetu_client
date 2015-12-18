package com.walfud.meetu.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.Constants;
import com.walfud.meetu.MainService;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.walle.PermissionUtils;
import com.walfud.walle.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/8/18.
 */
public class LauncherActivity extends BaseActivity {

    public static final String TAG = "LauncherActivity";
    private Version mUpdateVersion = Version.parse("v0.0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check update
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                switch (i) {
                    case UpdateStatus.Yes: // has update
                    case UpdateStatus.NoneWifi: // none wifi
                        UmengUpdateAgent.showUpdateDialog(MeetUApplication.getContext(), updateResponse);
                        if (updateResponse != null) {
                            mUpdateVersion = Version.parse(updateResponse.version);
                        }
                        break;

                    default:
                        break;
                }
            }
        });
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.NotNow:   // User chooses cancel
                        // Force update: if major version update was canceled, we don't allow to continue
                        if (Math.abs(MeetUApplication.getVersion().getMajor() - mUpdateVersion.getMajor()) > 0) {
                            Toast.makeText(LauncherActivity.this, "Please update to newest version", Toast.LENGTH_LONG).show();
                            finishAll();
                        }
                        break;

                    default:
                        break;
                }
            }
        });
        UmengUpdateAgent.update(MeetUApplication.getContext());

        // Check permission
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
            ActivityCompat.requestPermissions(this, com.walfud.walle.Transformer.stringCollection2Strings(requestPermList), Constants.REQUEST_PERMISSION);
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
        if (PrefsManager.getInstance().getShowSplash()) {
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