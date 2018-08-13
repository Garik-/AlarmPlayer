package com.github.garik_.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlarmPlayer";
    private static final String TABLE_ALARMS = "alarms";

    static final String FIELD_ID = "id";
    static final String FIELD_PATH = "path";
    static final String FIELD_REPEAT = "repeat";
    static final String FIELD_TRIGGER = "trigger_at";
    static final String FIELD_INTERVAL = "interval";
    static final String FIELD_ALARM_ID = "alarm_id";

    private final String[] TABLE_ALARMS_FIELDS;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.TABLE_ALARMS_FIELDS = new String[]{
                FIELD_ID, FIELD_PATH, FIELD_TRIGGER, FIELD_INTERVAL, FIELD_REPEAT, FIELD_ALARM_ID
        };


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY," + FIELD_PATH + " TEXT,"
                + FIELD_REPEAT + " INTEGER," + FIELD_TRIGGER + " INTEGER,"
                + FIELD_INTERVAL + " INTEGER," + FIELD_ALARM_ID + " INTEGER" + ")";

        Log.d(GarikApp.TAG, "Create table");
        Log.d(GarikApp.TAG, CREATE_ALARMS_TABLE);

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        onCreate(db);
    }

    private Alarm toAlarm(Cursor cursor) {
        return new Alarm(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Long.parseLong(cursor.getString(2)),
                Long.parseLong(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));
    }

    private ContentValues toValues(Alarm alarm) {
        ContentValues values = new ContentValues();

        values.put(FIELD_PATH, alarm.getFilePath());
        values.put(FIELD_REPEAT, alarm.getRepeatCount());
        values.put(FIELD_TRIGGER, alarm.getTriggerAtMillis());
        values.put(FIELD_INTERVAL, alarm.getIntervalMillis());
        values.put(FIELD_ALARM_ID, alarm.getUniqueId());

        return values;
    }

    @Override
    public List<Alarm> getAllAlarms() {
        List<Alarm> alarmList = new ArrayList<>();
        String selectQuery = "SELECT  " + TextUtils.join(", ", TABLE_ALARMS_FIELDS) + " FROM " + TABLE_ALARMS;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    alarmList.add(toAlarm(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();

        return alarmList;
    }

    public List<Alarm> getNewAlarms(String keys) {
        List<Alarm> alarmList = new ArrayList<>();

        String selectQuery = "SELECT  " + TextUtils.join(", ", TABLE_ALARMS_FIELDS) + " FROM "
                + TABLE_ALARMS + " WHERE " + FIELD_TRIGGER + " >= " + System.currentTimeMillis()
                + (TextUtils.isEmpty(keys) ? "" : " AND " + FIELD_ALARM_ID + " NOT IN (" + keys + ")");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d(GarikApp.TAG, selectQuery);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Log.d(GarikApp.TAG, cursor.toString());

                    alarmList.add(toAlarm(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        db.close();


        return alarmList;
    }

    @Override
    public Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Alarm alarm = null;

        Cursor cursor = db.query(TABLE_ALARMS, TABLE_ALARMS_FIELDS, FIELD_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, "1");

        if (cursor != null) {
            cursor.moveToFirst();

            alarm = toAlarm(cursor);
            cursor.close();
        }

        db.close();

        return alarm;
    }

    @Override
    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_ALARMS, null, toValues(alarm));
        alarm.setId((int) id);
        db.close();
    }

    @Override
    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.update(TABLE_ALARMS, toValues(alarm), FIELD_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});

        db.close();

        return result;
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, FIELD_ID + " = ?", new String[]{String.valueOf(alarm.getId())});
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, null, null);
        db.close();
    }
}
