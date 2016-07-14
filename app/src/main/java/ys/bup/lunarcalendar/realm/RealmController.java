package ys.bup.lunarcalendar.realm;//package com.albert.study.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
	private static RealmController instance;
	private final Realm realm;

	public RealmController(Application application) {
		realm = Realm.getDefaultInstance();
	}

	public static RealmController with(Fragment fragment) {

		if (instance == null) {
			instance = new RealmController(fragment.getActivity().getApplication());
		}
		return instance;
	}

	public static RealmController with(Activity activity) {

		if (instance == null) {
			instance = new RealmController(activity.getApplication());
		}
		return instance;
	}

	public static RealmController with(Application application) {

		if (instance == null) {
			instance = new RealmController(application);
		}
		return instance;
	}

	public static RealmController getInstance() {

		return instance;
	}

	public Realm getRealm() {

		return realm;
	}

	//Refresh the realm istance
	public void refresh() {

		realm.isAutoRefresh();
	}

	//clear all objects from Book.class
	public void clearAll() {

		realm.beginTransaction();
		realm.deleteAll();
		realm.commitTransaction();
	}

	//find all objects in the Book.class
	public RealmResults<RealmFavoriteObject> getFavorites() {

		return realm.where(RealmFavoriteObject.class).findAll();
	}

	//query a single item with the given id
	public RealmFavoriteObject getFavorite(String idx) {

		return realm.where(RealmFavoriteObject.class).equalTo("idx", idx).findFirst();
	}

	//check if Book.class is empty
//	public boolean hasFavorites() {
//
//		return !realm.insert(FavoriteEntity.class).isEmpty();
//	}

	//query example
	public RealmResults<RealmFavoriteObject> queryedFavorites() {

		return realm.where(RealmFavoriteObject.class)
				.contains("year", "2016")
				.findAll();
	}
}
