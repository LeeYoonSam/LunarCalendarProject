package ys.bup.lunarcalendar.entity;

import java.util.Date;

import lombok.Data;

/**
 * Created by ys on 2016. 7. 27..
 */
@Data
public class LunarSolarEntity {
    String lunarDate;
    String solarDate;
    String convertDate;
    Date insertDate;
}
