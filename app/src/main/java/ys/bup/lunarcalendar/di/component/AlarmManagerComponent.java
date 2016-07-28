package ys.bup.lunarcalendar.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ys.bup.lunarcalendar.di.module.AlarmManagerModule;

/**
 * Created by ys on 2016. 7. 28..
 */
@Singleton
@Component(dependencies = AppComponent.class, modules = AlarmManagerModule.class)
public interface AlarmManagerComponent {

//    AlarmManager alarmManager();
//    AlarmControl alarmControl();
}
