package com.github.garik_.testapp;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

public class GarikApp extends Application {
    public static final String TAG = "garik.djan";

    private SparseIntArray pAlarms;

    @Override
    public void onCreate() {
        super.onCreate();

        pAlarms = new SparseIntArray();
    }

    public void setAlarmCount(int uid, int count) {
        pAlarms.put(uid, count);
    }

    public void removeAlarm(int uid) {
        pAlarms.delete(uid);
    }

    public int getAlarmCount(int uid) {
        int count = pAlarms.get(uid);
        setAlarmCount(uid, ++count);

        return count;
    }


    public String getKeys() {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < pAlarms.size(); i++) {
            int key = pAlarms.keyAt(i);
            list.add(Integer.toString(key));
        }

        return TextUtils.join(",", list);
    }
}
