package com.github.garik_.testapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(date);
    }

    /**
     * Возвращает форму множественного числа в русском языке
     *
     * @param n число чего-то
     * @return 0 один, 1 мало, 2 много
     * @link http://docs.translatehouse.org/projects/localization-guide/en/latest/l10n/pluralforms.html?id=l10n/pluralforms
     */
    private int plural(long n) {
        return n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
    }


    public String getIntervalTimeFormat() {
        long m = TimeUnit.MILLISECONDS.toMinutes(this.intervalMillis);


        StringBuilder result = new StringBuilder();
        if (m > 0) {
            switch (this.plural(m)) {
                case 0:
                    result.append("Каждую");
                    break;
                case 1:
                case 2:
                    result.append("Каждые ");
                    result.append(m);
                    break;
            }

            switch (this.plural(m)) {
                case 0:
                    result.append(" минуту");
                    break;
                case 1:
                    result.append(" минуты");
                    break;
                case 2:
                    result.append(" минут");
                    break;
            }
        }

        return result.toString();
    }

    public String getTriggerAtDateFormat() {
        return getFormatDate(this.triggerAtMillis, "dd/MM");
    }

    public String getTriggerAtTimeFormat() {
        return getFormatDate(this.triggerAtMillis, "HH:mm");
    }

    public String getRepeatCountFormat() {
        StringBuilder sb = new StringBuilder("Повторять " + this.repeatCount + " ");
        switch (this.plural(new Long(this.repeatCount))) {
            case 0:
            case 2:
                sb.append("раз");
                break;
            case 1:
                sb.append("раза");
                break;
        }
        return sb.toString();
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
