package ys.bup.lunarcalendar.activities;

public class FavoriteEntity {

	private String memo;
	private String convertDate;
	
	public FavoriteEntity()
	{
		this.memo = null;
		this.convertDate = null;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getConvertDate() {
		return convertDate;
	}

	public void setConvertDate(String convertDate) {
		this.convertDate = convertDate;
	}
	
	
}
