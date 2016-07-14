package ys.bup.lunarcalendar;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarCalendarApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
		.name(Realm.DEFAULT_REALM_NAME)
		.schemaVersion(0)
		.deleteRealmIfMigrationNeeded()
		.build();
		Realm.setDefaultConfiguration(realmConfiguration);
    }
}
