package com.github.garik_.testapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlarmBroadcastReceiver alarm = new AlarmBroadcastReceiver();

        long c = System.currentTimeMillis();
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/1.mp3";
        alarm.setAlarm(this.getApplicationContext(), c, 0, filePath, 1);
        alarm.setAlarm(this.getApplicationContext(), c + 1000 * 80, 1000 * 60, filePath, 2);
    }
}
