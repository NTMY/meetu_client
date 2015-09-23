package com.walfud.meetu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.walfud.meetu.model.MainModel;

/**
 * Created by walfud on 2015/9/13.
 */
public class MainBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (false) {
        } else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Boot Complete
            MainModel.startServiceWithSetting();
        }
    }
}
