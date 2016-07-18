package ys.bup.lunarcalendar;

import android.app.Application;

/**
 * Created by ys on 2016. 7. 13..
 */
public class LunarCalendarApplication extends Application {

    private ApplicationComponent component;

	@Singleton
	@Component(modules = AppModule.class)
	public interface ApplicationComponent {
		void inject(LunarCalendarApplication application);
		void inject(LunarMainAt mainActivity);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initApplicationComponent();
	}

	private void initApplicationComponent() {
		this.component = DaggerLunarCalendarApplication_ApplicationComponent.builder()
				.applicationModule(new AppModule(this))
				.build();
		
		component.inject(this);
	}

	public ApplicationComponent getApplicationComponent() {
		return this.component;
	}
}
