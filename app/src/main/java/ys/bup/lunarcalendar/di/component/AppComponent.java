package ys.bup.lunarcalendar.di.component;

import dagger.Component;
import ys.bup.lunarcalendar.AppModule;
import ys.bup.lunarcalendar.LunarCalendarApplication;

/**
 * Created by ys on 2016. 7. 28..
 */
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(LunarCalendarApplication application);
}


