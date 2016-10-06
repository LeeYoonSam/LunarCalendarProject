package ys.bup.lunarcalendar;

import android.app.AlarmManager;
import android.content.Context;

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
	Context provideApplicationContext() {
		return application;
	}

	@Provides
	AlarmManager provideAlarmManager() {
		return (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
	}
}
