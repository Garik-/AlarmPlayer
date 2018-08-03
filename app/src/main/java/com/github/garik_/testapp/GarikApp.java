package com.github.garik_.testapp;

import android.app.Application;
import java.util.HashMap;

public class GarikApp extends Application {
    private HashMap<Integer, Integer> pAlarms;

    @Override
    public void onCreate() {
        super.onCreate();

        pAlarms = new HashMap<Integer, Integer>();
    }

    public void setAlarmCount(int uid, int count) {
        pAlarms.put(uid, count);
    }

    public void removeAlarm(int uid) {
        pAlarms.remove(uid);
    }

    public int getAlarmCount(int uid) {
        int count = 0;
        if(pAlarms.containsKey(uid)) {
            count = pAlarms.get(uid);
            setAlarmCount(uid, ++count);
        }

        return count;
    }
}
