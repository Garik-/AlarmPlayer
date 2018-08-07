package com.github.garik_.testapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Alarm {
    private int id;
    private int uniqueId;
    private String filePath;
    private long triggerAtMillis;
    private long intervalMillis;
    private int repeatCount;

    private String getFormatDate(long millis, String format) {
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }


    public String getIntervalTimeFormat() {
        return getFormatDate(this.intervalMillis, "mm:ss");
    }

    public String getTriggerAtTimeFormat() {
        return getFormatDate(this.triggerAtMillis, "mm:ss");
    }

    public Alarm(int id, String filePath, long triggerAtMillis, long intervalMillis, int repeatCount, int uniqueId) {
        this.id = id;
        this.filePath = filePath;
        this.triggerAtMillis = triggerAtMillis;
        this.intervalMillis = intervalMillis;
        this.repeatCount = repeatCount;
        this.uniqueId = uniqueId;
    }

    public Alarm(String filePath, long triggerAtMillis, long intervalMillis, int repeatCount) {

        this.filePath = filePath;
        this.triggerAtMillis = triggerAtMillis;
        this.intervalMillis = intervalMillis;
        this.repeatCount = repeatCount;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public long getTriggerAtMillis() {
        return this.triggerAtMillis;
    }

    public long getIntervalMillis() {
        return this.intervalMillis;
    }

    public int getUniqueId() {
        return this.uniqueId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTriggerAtMillis(long triggerAtMillis) {
        this.triggerAtMillis = triggerAtMillis;
    }

    public void setIntervalMillis(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}
