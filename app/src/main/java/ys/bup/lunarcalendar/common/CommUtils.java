package ys.bup.lunarcalendar.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ibm.icu.util.ChineseCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ys.bup.lunarcalendar.entity.LunarSolarEntity;

public class CommUtils {

	public static int 	displayWidth;
	public static int 	displayHeight;
	public static int 	displayDpi;
	public static float displayScale;

	public static final SimpleDateFormat mReceiveFormat = new SimpleDateFormat ( "yyyyMMdd");
	public static final SimpleDateFormat mShowFormat = new SimpleDateFormat ( "yyyy. MM. dd");

	public static String getDateByShowForamt(Date date) {
		return mShowFormat.format(date);
	}

	public static String getShowFormat(String strDate)
	{
		try {
			Date date = mReceiveFormat.parse(strDate);
			return getDateByShowForamt(date);
		}
		catch (Exception e) {
			e.printStackTrace();

			return "";
		}
	}

	/**
	 * 음력날짜를 양력날짜로 변환
	 * @param 음력날짜 (yyyyMMdd)
	 * @return 양력날짜 (yyyyMMdd)
	 */
	public static String convertStringLunarToSolar(String date) {

		ChineseCalendar cc = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();

		cc.set(ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4)) + 2637);
		cc.set(ChineseCalendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cc.set(ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

		cal.setTimeInMillis(cc.getTimeInMillis());

		return mReceiveFormat.format(cal.getTime());
	}

	/**
	 * 양력날짜를 음력날짜로 변환
	 * @param 양력날짜 (yyyyMMdd)
	 * @return 음력날짜 (yyyyMMdd)
	 */
	public static String converStringSolarToLunar(String date) {

		ChineseCalendar cc = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

		cc.setTimeInMillis(cal.getTimeInMillis());

		int y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int m = cc.get(ChineseCalendar.MONTH) + 1;
		int d = cc.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		ret.append(String.format("%04d", y));
		ret.append(String.format("%02d", m));
		ret.append(String.format("%02d", d));

		return ret.toString();
	}


	/**
	 * Today's Lunar & Solar Date
	 *
	 * @return
     */
	public static HashMap<String, String> getTodayLunarSolar() {

		HashMap<String, String> resultDate = new HashMap<>();

		Calendar today = Calendar.getInstance();

		// 양력 날짜
		resultDate.put("solarDate", "양력 " + getDateByShowForamt(today.getTime()));


		ChineseCalendar cc = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, today.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, today.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH));

		cc.setTimeInMillis(cal.getTimeInMillis());

		int y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int m = cc.get(ChineseCalendar.MONTH) + 1;
		int d = cc.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		ret.append(String.format("%04d", y)).append(". ");
		ret.append(String.format("%02d", m)).append(". ");
		ret.append(String.format("%02d", d));

		resultDate.put("lunarDate", "음력 " + ret.toString());


		return resultDate;
	}

	/**
	 * 음력날짜를 양력날짜로 변환
	 * @param 음력날짜 (yyyyMMdd)
	 * @return 양력날짜 (yyyyMMdd)
	 */
	public static LunarSolarEntity convertLunarToSolar(String date) {

		LunarSolarEntity tmpResult = new LunarSolarEntity();
		tmpResult.setLunarDate(date);

		ChineseCalendar cc = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();

		cc.set(ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4)) + 2637);
		cc.set(ChineseCalendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cc.set(ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

		cal.setTimeInMillis(cc.getTimeInMillis());

		tmpResult.setConvertDate(getDateByShowForamt(cal.getTime()));
		tmpResult.setSolarDate(mReceiveFormat.format(cal.getTime()));
		tmpResult.setInsertDate(new Date(cal.getTimeInMillis()));

		return tmpResult;
	}

	/**
	 * 양력날짜를 음력날짜로 변환
	 * @param 양력날짜 (yyyyMMdd)
	 * @return 음력날짜 (yyyyMMdd)
	 */
	public static LunarSolarEntity converSolarToLunar(String date) {

		LunarSolarEntity tmpResult = new LunarSolarEntity();
		tmpResult.setSolarDate(date);

		ChineseCalendar cc = new ChineseCalendar();
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

		cc.setTimeInMillis(cal.getTimeInMillis());

		int y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int m = cc.get(ChineseCalendar.MONTH) + 1;
		int d = cc.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		ret.append(String.format("%04d", y)).append("-");
		ret.append(String.format("%02d", m)).append("-");
		ret.append(String.format("%02d", d));

		tmpResult.setConvertDate(ret.toString());
		tmpResult.setLunarDate(ret.replace(0, ret.length(), "-").toString());
		tmpResult.setInsertDate(new Date(cal.getTimeInMillis()));

		return tmpResult;
	}


	public static int getDivision(float value)
	{
		//		LogUtil.Logd("getDivision Size", "value = " + (int)(value * (displayHeight / 1280.0f)));
		return (int)(value * (displayHeight / 1280.0f));
	}

	public static float getDivisionFloat(float value)
	{
		//		LogUtil.Logd("getDivision Size", "value = " + (int)(value * (displayHeight / 1280.0f)));
		return value * (displayHeight / 1280.0f);
	}

	public static void hideSoftInput(Activity activity, View view) {
		view.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showSoftInput(Activity activity, View view) {
		view.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, 0);
	}


	public static int getColor(Context context, int id) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.M) {
			return context.getColor(id);
		} else {
			return context.getResources().getColor(id);
		}
	}

	public static Drawable getDrawable(Context context, int id) {
		final int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.LOLLIPOP) {
			return context.getDrawable(id);
		} else {
			return context.getResources().getDrawable(id);
		}
	}
}
