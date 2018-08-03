package com.github.garik_.testapp;

public class Alarm {
    int id;
    int uniqueId;
    String filePath;
    long triggerAtMillis;
    long intervalMillis;
    int repeatCount;

    public Alarm() {

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
}
