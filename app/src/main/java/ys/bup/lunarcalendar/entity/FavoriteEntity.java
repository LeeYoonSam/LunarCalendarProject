package ys.bup.lunarcalendar.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import ys.bup.lunarcalendar.common.CommUtils;

public class FavoriteEntity extends RealmObject {

	private Date insertDate;

	@Required
	private String memo;

	@Required
	private String solarDate;

	@Required
	private String lunarDate;

	private int alarmHour;
	private int alarmMinute;

	private boolean isAlarmOn;
	private boolean isAlarmRepeat;

	public FavoriteEntity () {
		this.memo = null;
		this.solarDate = null;
		this.lunarDate = null;

		this.alarmHour = 999;
		this.alarmMinute = 999;

		this.isAlarmOn = false;
		this.isAlarmRepeat = false;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getSolarDate() {
		return solarDate;
	}
	public void setSolarDate(String solarDate) {
		this.solarDate = solarDate;
	}
	public String getLunarDate() {
		return lunarDate;
	}
	public void setLunarDate(String lunarDate) {
		this.lunarDate = lunarDate;
	}

	public int getAlarmHour() {
		return alarmHour;
	}

	public void setAlarmHour(int alarmHour) {
		this.alarmHour = alarmHour;
	}

	public int getAlarmMinute() {
		return alarmMinute;
	}

	public void setAlarmMinute(int alarmMinute) {
		this.alarmMinute = alarmMinute;
	}

	public String showLunarDate() {
		return CommUtils.getShowFormat(this.lunarDate);
	}

	public String showSolarDate() {
		return CommUtils.getShowFormat(this.solarDate);
	}

	public boolean isAlarmOn() {
		return isAlarmOn;
	}

	public void setAlarmOn(boolean isAlarmOn) {
		this.isAlarmOn = isAlarmOn;
	}

	public boolean isAlarmRepeat() {
		return isAlarmRepeat;
	}

	public void setAlarmRepeat(boolean isAlarmRepeat) {
		this.isAlarmRepeat = isAlarmRepeat;
	}
}
