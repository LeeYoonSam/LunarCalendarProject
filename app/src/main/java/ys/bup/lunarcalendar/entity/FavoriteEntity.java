package ys.bup.lunarcalendar.entity;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class FavoriteEntity extends RealmObject {

	@Required
	private String memo;

	@Required
	private String year;

	@Required
	private String month;

	@Required
	private String day;

	private boolean isLunar;

	private int hour;
	private int minute;

	public FavoriteEntity()
	{
		this.memo = null;
		this.year = null;
		this.month = null;
		this.day = null;
		this.isLunar = true;
		this.hour = 999;
		this.minute = 999;

	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean isLunar() {
		return isLunar;
	}

	public void setLunar(boolean lunar) {
		isLunar = lunar;
	}

	public String getDisplayDate()
	{
		if(this.year != null && this.month != null && this.day != null)
		{
			return year + "-" + month + "-" + day;
		}

		return "";
	}
}
