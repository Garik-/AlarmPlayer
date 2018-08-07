package com.github.garik_.testapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

// TODO: посмотри проект https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/  там описана логика работы с данными

public class MainActivity extends AppCompatActivity implements AlarmItemFragment.OnListFragmentInteractionListener {


    @Override
    public void onListFragmentInteraction(Alarm item) {
        Intent intent = new Intent(this, InsertActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


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
