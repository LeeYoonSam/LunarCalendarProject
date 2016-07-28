package ys.bup.lunarcalendar.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ys on 2016. 7. 28..
 */
@Module
public class ConfigModule {

    private Context context;

    ConfigModule(Context context) {
        this.context = context;
    }

    @Provides
    RealmConfiguration providesRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        return realmConfiguration;
    }
}
