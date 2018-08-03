package com.github.garik_.testapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Bundle;
import android.util.Log;


import java.util.UUID;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String LOCK_TAG = "garik.djan";
    private static final String FIELD_PATH = "path";
    private static final String FIELD_ID = "id";
    private static final String FIELD_REPEAT = "repeat";

    private static final String TAG = "garik.djan";


    @Override
    public void onReceive(Context context, Intent intent) {
        /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);

        wl.acquire();*/

        context = context.getApplicationContext();

        Bundle extras = intent.getExtras();

        int id = extras.getInt(FIELD_ID);
        int repeat = extras.getInt(FIELD_REPEAT);
        String filePath = extras.getString(FIELD_PATH);

        Log.d(TAG, "Receive alarm ID "+ id);

        Player mp = new Player();
        mp.play(context, filePath);

        GarikApp app = ((GarikApp) context);
        int count = app.getAlarmCount(id);
        if (count == repeat) {
            cancelAlarm(context, intent, id);
        }

        //wl.release();
    }

    private Intent createIntent(Context context, int id, String filePath, int repeat) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        intent.putExtra(FIELD_ID, id);
        intent.putExtra(FIELD_PATH, filePath);
        intent.putExtra(FIELD_REPEAT, repeat);

        return intent;
    }

    public void setAlarm(Context context, long triggerAtMillis, long intervalMillis, String filePath, int repeat) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int uniqueId = UUID.randomUUID().hashCode();
        Intent intent = createIntent(context, uniqueId, filePath, repeat);

        PendingIntent pi = PendingIntent.getBroadcast(context, uniqueId, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pi);

        GarikApp app = (GarikApp) context;
        app.setAlarmCount(uniqueId, 0);

        Log.d(TAG, "Set alarm ID "+ uniqueId);
    }

    private void cancelAlarm(Context context, Intent intent, int uniqueId) {
        PendingIntent sender = PendingIntent.getBroadcast(context, uniqueId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        GarikApp app = (GarikApp) context;
        app.removeAlarm(uniqueId);

        Log.d(TAG, "Cancel alarm ID "+ uniqueId);
    }
}
