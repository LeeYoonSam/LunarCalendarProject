package ys.bup.lunarcalendar.di.component;

import dagger.Component;
import ys.bup.lunarcalendar.AppModule;
import ys.bup.lunarcalendar.LunarCalendarApplication;
import ys.bup.lunarcalendar.receiver.AlarmTimeManager;

/**
 * Created by ys on 2016. 8. 1..
 */
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(LunarCalendarApplication application);
    void inject(AlarmTimeManager alarmTimeManager);
}
