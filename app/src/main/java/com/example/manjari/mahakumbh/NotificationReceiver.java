package com.example.manjari.mahakumbh;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class NotificationReceiver extends WakefulBroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        ComponentName comp = new ComponentName(context.getPackageName(),NotificationIntentService.class.getName());
        startWakefulService(context,intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
