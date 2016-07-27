package ys.bup.lunarcalendar.entity;

import android.support.annotation.IntRange;

/**
 * Created by ys on 2016. 7. 27..
 */
public class AlarmEntity implements Comparable<AlarmEntity>
{
    int hour;
    int minute;

    boolean isRepeat;

    public AlarmEntity() {
        this.hour = 999;
        this.minute = 999;

        this.isRepeat = false;
    }

    public AlarmEntity(@IntRange(from=0, to=23) int hour, @IntRange(from=0, to=59) int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    @Override
    public int compareTo(AlarmEntity t) {
        return (this.hour - t.hour) * 3600 + (this.minute - t.minute) * 60;
    }
}
