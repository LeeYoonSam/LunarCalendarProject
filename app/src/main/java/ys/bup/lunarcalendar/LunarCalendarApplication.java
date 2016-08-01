package ys.bup.lunarcalendar;

import android.app.Application;

import ys.bup.lunarcalendar.di.component.AppComponent;
import ys.bup.lunarcalendar.di.component.DaggerAppComponent;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarCalendarApplication extends Application {

    private AppComponent component;

	@Override
	public void onCreate() {
		super.onCreate();

		this.component = DaggerAppComponent.builder()
				.appModule(new AppModule(this))
				.build();

		component.inject(this);
	}

	public AppComponent getApplicationComponent() {
		return this.component;
	}
}
