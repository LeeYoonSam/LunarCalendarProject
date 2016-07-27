package ys.bup.lunarcalendar.entity;

import java.util.Date;

/**
 * Created by ys on 2016. 7. 27..
 */
public class LunarSolarEntity {
    String lunarDate;
    String solarDate;
    String convertDate;
    Date insertDate;

    public String getLunarDate() {
        return lunarDate;
    }
    public void setLunarDate(String lunarDate) {
        this.lunarDate = lunarDate;
    }
    public String getSolarDate() {
        return solarDate;
    }
    public void setSolarDate(String solarDate) {
        this.solarDate = solarDate;
    }
    public String getConvertDate() {
        return convertDate;
    }
    public void setConvertDate(String convertDate) {
        this.convertDate = convertDate;
    }
    public Date getInsertDate() {
        return insertDate;
    }
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
}
