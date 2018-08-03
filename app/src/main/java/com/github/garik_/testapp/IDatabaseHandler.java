package com.github.garik_.testapp;

import java.util.List;

public interface IDatabaseHandler {
    void addAlarm(Alarm alarm);

    Alarm getAlarm(int id);

    List<Alarm> getAllAlarms();

    int updateAlarms(Alarm alarm);

    void deleteAlarm(Alarm alarm);

    void deleteAll();
}
