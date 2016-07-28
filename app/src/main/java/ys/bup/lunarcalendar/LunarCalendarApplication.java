package ys.bup.lunarcalendar;

import android.app.Application;

import ys.bup.lunarcalendar.di.component.DaggerAppComponent;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarCalendarApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		DaggerAppComponent.builder()
				.appModule(new AppModule(this))
				.build().inject(this);
	}
}
