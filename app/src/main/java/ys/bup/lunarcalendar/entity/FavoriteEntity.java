package ys.bup.lunarcalendar.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import ys.bup.lunarcalendar.common.CommUtils;

public class FavoriteEntity extends RealmObject implements Parcelable {

	@PrimaryKey
	private int seq;

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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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

	protected FavoriteEntity(Parcel in) {
		seq = in.readInt();;
		memo = in.readString();
		solarDate = in.readString();
		lunarDate = in.readString();
		alarmHour = in.readInt();
		alarmMinute = in.readInt();
		isAlarmOn = in.readByte() != 0;
		isAlarmRepeat = in.readByte() != 0;
	}

	public static final Creator<FavoriteEntity> CREATOR = new Creator<FavoriteEntity>() {
		@Override
		public FavoriteEntity createFromParcel(Parcel in) {
			return new FavoriteEntity(in);
		}

		@Override
		public FavoriteEntity[] newArray(int size) {
			return new FavoriteEntity[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(seq);
		dest.writeString(memo);
		dest.writeString(solarDate);
		dest.writeString(lunarDate);
		dest.writeInt(alarmHour);
		dest.writeInt(alarmMinute);
		dest.writeByte((byte) (isAlarmOn ? 1 : 0));
		dest.writeByte((byte) (isAlarmRepeat ? 1 : 0));
	}
}
