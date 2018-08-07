package com.github.garik_.testapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements AlarmItemFragment.OnListFragmentInteractionListener {


    @Override
    public void onListFragmentInteraction(Alarm item) {

    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseHandler db = new DatabaseHandler(this);
        Alarm alarm = new Alarm("test2123", System.currentTimeMillis(), 1000 * 60, 2);
        db.addAlarm(alarm);

        alarm.setFilePath("test35345");
        db.addAlarm(alarm);


        /*
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {





           // receiver.setAlarm(this.getApplicationContext(), new Alarm(filePath, System.currentTimeMillis(), 1000*60, 2));

            DatabaseHandler db = new DatabaseHandler(this);


            Log.d(GarikApp.TAG, "Inserting...");
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/1.mp3";

            Alarm alarm = new Alarm(filePath, System.currentTimeMillis(), 1000*60, 2);
            db.addAlarm(alarm);

            List<Alarm> alarms = db.getAllAlarms();

            AlarmBroadcastReceiver receiver = new AlarmBroadcastReceiver();


            for(Alarm a : alarms) {
                Log.d(GarikApp.TAG, "Set alarm "+a.getFilePath());

                int uniqueId = receiver.setAlarm(this.getApplicationContext(), a);
                a.setUniqueId(uniqueId);

                db.updateAlarm(a);
            }

            db.deleteAll();
        }*/
    }
}
