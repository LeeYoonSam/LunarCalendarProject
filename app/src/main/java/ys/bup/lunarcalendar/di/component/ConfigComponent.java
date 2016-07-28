package ys.bup.lunarcalendar.di.component;

import dagger.Component;
import ys.bup.lunarcalendar.activities.BaseLoadingActivity;
import ys.bup.lunarcalendar.activities.LunarMainAt;
import ys.bup.lunarcalendar.di.module.ConfigModule;

/**
 * Created by ys on 2016. 7. 28..
 */
@Component(dependencies = AppComponent.class, modules = ConfigModule.class)
public interface ConfigComponent {

    void inject(BaseLoadingActivity baseLoadingActivity);
    void inject(LunarMainAt lunarMainAt);

//    RealmConfiguration realmConfiguration();
}
