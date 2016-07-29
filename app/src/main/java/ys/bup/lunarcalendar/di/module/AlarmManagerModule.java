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

    Context context;

    public AlarmManagerModule(Context context) {
        this.context = context;
    }

    @Provides
    AlarmControl providesAlarmControl(AlarmTimeManager manager) {
        return manager;
    }
}
