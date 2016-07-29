package ys.bup.lunarcalendar.di.module;

import android.app.AlarmManager;
import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ys.bup.lunarcalendar.AppModule;
import ys.bup.lunarcalendar.LunarCalendarApplication;

/**
 * Created by ys on 2016. 7. 28..
 */
@Module (includes = AppModule.class)
public class AlarmManagerModule {

    @Inject
    LunarCalendarApplication application;

    @Provides
    AlarmManager providesAlarmManager() {
        return (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
    }
}
