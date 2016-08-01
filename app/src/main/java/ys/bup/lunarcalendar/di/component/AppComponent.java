package ys.bup.lunarcalendar.di.component;

import android.content.Context;

import dagger.Component;
import ys.bup.lunarcalendar.AppModule;
import ys.bup.lunarcalendar.LunarCalendarApplication;
import ys.bup.lunarcalendar.activities.LunarSearchAt;

/**
 * Created by ys on 2016. 7. 28..
 */
@Component(modules = { AppModule.class } )
public interface AppComponent {
    void inject(LunarCalendarApplication application);
	void inject(AlarmTimeManager alarmTimeManager);
}


