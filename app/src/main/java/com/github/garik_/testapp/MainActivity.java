package com.github.garik_.testapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private AlarmBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarm = new AlarmBroadcastReceiver();

        long c = System.currentTimeMillis();
        alarm.setAlarm(this.getApplicationContext(), c,0, "/sdcard/1.mp3",1 );
        alarm.setAlarm(this.getApplicationContext(), c + 1000*80,1000*60, "/sdcard/1.mp3",2 );
    }
}
