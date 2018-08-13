package com.github.garik_.testapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class InsertActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    static final String BROADCAST_ACTION = InsertActivity.class.getName();
    private int mAction;
    private int mPosition;
    private Alarm mAlarm;

    private int timeViewId;
    private Calendar mTriggetAtDate;
    FilePickerDialog  dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Bundle b = getIntent().getExtras();
        mAction = b.getInt(MainActivity.ACTION);
        mPosition = 0;

        mTriggetAtDate = new GregorianCalendar();


        switch (mAction) {
            case MainActivity.ACTION_UPDATE:
                mAlarm = new Alarm(b);
                mPosition = b.getInt(MainActivity.POSITION);

                mTriggetAtDate.setTimeInMillis(mAlarm.getTriggerAtMillis());

                Button btn = findViewById(R.id.set_interval_time);
                btn.setText(mAlarm.getIntervalTimeFormat());

                btn = findViewById(R.id.set_trigerat_date);
                btn.setText(mAlarm.getTriggerAtDateFormat());
                btn = findViewById(R.id.set_trigerat_time);
                btn.setText(mAlarm.getTriggerAtTimeFormat());

                TextInputEditText editText = findViewById(R.id.set_repeat);

                editText.setText(Integer.toString(mAlarm.getRepeatCount()));

                editText = findViewById(R.id.set_filepath);
                editText.setText(mAlarm.getFilePath());

                break;
            default:
                mAlarm = new Alarm();
                break;
        }

        /*Button mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BROADCAST_ACTION);

                getApplicationContext().sendBroadcast(intent);
                finish();
            }
        });*/

        final TextInputEditText fileText = findViewById(R.id.set_filepath);
        //fileText.setEnabled(false);

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File("/");
        properties.error_dir = new File("/");
        properties.offset = new File("/");
        properties.extensions = new String[]{"mp3"};
        dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Выбрать mp3 файл");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //mAlarm.setFilePath(files[0]);
                fileText.setText(files[0]);
            }
        });

        fileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });




    }

    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(this,"Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void showTimePickerDialog(View v) {

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setListener(this);

        // -- default values
        newFragment.hour = 0;
        newFragment.minute = 0;

        timeViewId = v.getId();

        switch (timeViewId) {
            case R.id.set_interval_time:
                newFragment.minute = 5;
                break;
        }

        if (MainActivity.ACTION_UPDATE == mAction) {
            switch (timeViewId) {
                case R.id.set_interval_time:
                    newFragment.minute = (int) TimeUnit.MILLISECONDS.toMinutes(mAlarm.getIntervalMillis());
                    newFragment.hour = (int) TimeUnit.MILLISECONDS.toHours(mAlarm.getIntervalMillis());
                    break;
            }
        }

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        Button btn = findViewById(timeViewId);

        switch (timeViewId) {
            case R.id.set_trigerat_time:
                mTriggetAtDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mTriggetAtDate.set(Calendar.MINUTE, minute);
                mTriggetAtDate.set(Calendar.SECOND, 0);
                mTriggetAtDate.set(Calendar.MILLISECOND, 0);

                mAlarm.setTriggerAtMillis(mTriggetAtDate.getTimeInMillis());
                btn.setText(mAlarm.getTriggerAtTimeFormat());
                break;
            case R.id.set_interval_time:
                mAlarm.setIntervalMillis(minute * 60000);
                btn.setText(mAlarm.getIntervalTimeFormat());
                break;
        }


    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Button btn = findViewById(R.id.set_trigerat_date);
        btn.setText((day < 10 ? "0" + day : day) + "/" + (month < 10 ? "0" + month : month));

        mTriggetAtDate.set(Calendar.YEAR, year);
        mTriggetAtDate.set(Calendar.MONTH, month);
        mTriggetAtDate.set(Calendar.DAY_OF_MONTH, day);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private Boolean isExists(String filepath) {


        if (!filepath.isEmpty()) {
            File file = new File(filepath);
            return file.exists();
        }
        return false;
    }

    public void updateAlarm(View v) {


        mAlarm.setTriggerAtMillis(mTriggetAtDate.getTimeInMillis());

        TextInputEditText t = findViewById(R.id.set_repeat);
        String s = t.getText().toString();

        if (!TextUtils.isEmpty(s)) {
            mAlarm.setRepeatCount(Integer.parseInt(s));
        }

        t = findViewById(R.id.set_filepath);
        mAlarm.setFilePath(t.getText().toString());

        if (!isExists(mAlarm.getFilePath())) {
            TextInputLayout mFileLayout = findViewById(R.id.filepath_layout);
            String file_error = getResources().getString(R.string.file_error);
            mFileLayout.setError(file_error);
            mFileLayout.setErrorEnabled(true);

            return;
        }

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtras(mAlarm.toBundle());
        intent.putExtra(MainActivity.ACTION, mAction);
        intent.putExtra(MainActivity.POSITION, mPosition);
        getApplicationContext().sendBroadcast(intent);

        finish();
    }

    public static class TimePickerFragment extends DialogFragment {

        public int hour;
        public int minute;
        private TimePickerDialog.OnTimeSetListener mListener;

        public void setListener(TimePickerDialog.OnTimeSetListener mListener) {
            this.mListener = mListener;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            if (0 == hour && 0 == minute) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

            }


            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), mListener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener mListener;

        public void setListener(DatePickerDialog.OnDateSetListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), mListener, year, month, day);
        }


    }
}
