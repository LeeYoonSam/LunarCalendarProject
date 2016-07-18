package ys.bup.lunarcalendar;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class AppModule {

	LunarCalendarApplication application;

	public AppModule(LunarCalendarApplication application) {
		this.application = application;

		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(application).deleteRealmIfMigrationNeeded().build();
		Realm.setDefaultConfiguration(realmConfiguration);

	}

	@Provides
	@Singleton
	Context provideApplicationContext() {
		return application;
	}
}
