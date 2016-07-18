package ys.bup.lunarcalendar;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppModule {

	/*
	 * apply plugin: 'com.neenbedankt.android-apt'
 
buildscript {
    repositories {
        mavenCentral()
    }
 
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}
 
android {
    ........
 
    dependencies {
        .......
        provided 'javax.annotation:jsr250-api:1.0'
        apt 'com.google.dagger:dagger-compiler:2.0'
        compile 'com.google.dagger:dagger:2.0'
    }
}

참고사이트
http://drcarter.tistory.com/169

	 */
	private final LunarCalendarApplication application;

	public AppModule(LunarCalendarApplication application) {
		this.application = application;

		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(application).deleteRealmIfMigrationNeeded().build();
		Realm.setDefaultConfiguration(realmConfiguration);

	}

	@Provides
	Context provideApplicationContext() {
		return application;
	}
}
