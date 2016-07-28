package ys.bup.lunarcalendar.di.module;

import android.app.AlarmManager;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ys.bup.lunarcalendar.common.AlarmControl;

/**
 * Created by ys on 2016. 7. 28..
 */
@Module
public class AlarmManagerModule {

    AlarmControl alarmControl;
    Context context;

    public AlarmManagerModule(Context context, AlarmControl alarmControl) {
        this.context = context;
    }

    @Provides
    AlarmManager providesAlarmManager(Context context) {
        return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    AlarmControl providesAlarmControl() {
        return alarmControl;
    }
}
