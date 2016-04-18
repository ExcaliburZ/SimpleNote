package com.wings.simplenote.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "AlarmReceiver");
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        alarm.putExtra(AlarmClock.EXTRA_HOUR, 23);
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, 50);
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(intent);
    }

}
