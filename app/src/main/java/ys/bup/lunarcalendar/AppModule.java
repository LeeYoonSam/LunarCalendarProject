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

	 */
	private final StudyApplication application;

	public AppModule(StudyApplication application) {
		this.application = application;

		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(application).deleteRealmIfMigrationNeeded().build();
		Realm.setDefaultConfiguration(realmConfiguration);

	}

	@Provides
	Context provideApplicationContext() {
		return application;
	}
}
