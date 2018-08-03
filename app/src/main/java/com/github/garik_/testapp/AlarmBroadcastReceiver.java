package com.github.garik_.testapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import java.util.UUID;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String FIELD_PATH = "path";
    private static final String FIELD_ID = "id";
    private static final String FIELD_REPEAT = "repeat";
    private static final String FIELD_INTERVAL = "interval";

    @Override
    public void onReceive(Context context, Intent intent) {

        context = context.getApplicationContext();

        Bundle extras = intent.getExtras();
        if (null != extras) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (null != pm) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GarikApp.TAG);

                wl.acquire(extras.getLong(FIELD_INTERVAL));

                int id = extras.getInt(FIELD_ID);
                Log.d(GarikApp.TAG, "Receive alarm ID " + id);

                Player mp = new Player();
                mp.play(context, extras.getString(FIELD_PATH));

                GarikApp app = ((GarikApp) context);

                if (app.getAlarmCount(id) == extras.getInt(FIELD_REPEAT)) {
                    cancelAlarm(context, intent, id);
                }

                wl.release();
            } else {
                Log.e(GarikApp.TAG, "getSystemService POWER_SERVICE");
            }
        } else {
            Log.e(GarikApp.TAG, "empty Extras in alarm");
        }
    }

    private Intent createIntent(Context context, int id, String filePath, long intervalMillis, int repeat) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

        intent.putExtra(FIELD_ID, id);
        intent.putExtra(FIELD_PATH, filePath);
        intent.putExtra(FIELD_REPEAT, repeat);
        intent.putExtra(FIELD_INTERVAL, intervalMillis);

        return intent;
    }

    public void setAlarm(Context context, long triggerAtMillis, long intervalMillis, String filePath, int repeat) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (null != am) {

            int uniqueId = UUID.randomUUID().hashCode();
            Intent intent = createIntent(context, uniqueId, filePath, intervalMillis, repeat);

            PendingIntent pi = PendingIntent.getBroadcast(context, uniqueId, intent, 0);

            am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pi);

            GarikApp app = (GarikApp) context;
            app.setAlarmCount(uniqueId, 0);

            Log.d(GarikApp.TAG, "Set alarm ID " + uniqueId);
        } else {
            Log.e(GarikApp.TAG, "getSystemService ALARM_SERVICE");
        }
    }

    private void cancelAlarm(Context context, Intent intent, int uniqueId) {
        PendingIntent sender = PendingIntent.getBroadcast(context, uniqueId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (null != alarmManager) {
            alarmManager.cancel(sender);

            GarikApp app = (GarikApp) context;
            app.removeAlarm(uniqueId);

            Log.d(GarikApp.TAG, "Cancel alarm ID " + uniqueId);
        } else {
            Log.e(GarikApp.TAG, "getSystemService ALARM_SERVICE");
        }
    }
}