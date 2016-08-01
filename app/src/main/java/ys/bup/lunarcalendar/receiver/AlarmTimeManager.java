package ys.bup.lunarcalendar.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Singleton;

import ys.bup.lunarcalendar.entity.FavoriteEntity;

/**
 * Created by ys on 2016. 7. 27..
 */
@Singleton
public class AlarmTimeManager {
    private ArrayList<FavoriteEntity> alarmList = new ArrayList<FavoriteEntity>();

    @Inject
    private AlarmManager mManager;

    private Context _context;

    // constructor
    public AlarmTimeManager(Context context) {

//        mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        LunarCalendarApplication.getAppComponent().inject(this);

        this._context = context;
    }
//
//    public static AlarmTimeManager getInstance(Context context) {
//        if( instance == null) {
//            synchronized (AlarmTimeManager.class) {
//                if(instance == null) {
//                    instance = new AlarmTimeManager(context);
//                }
//            }
//        }
//        return instance;
//    }

    // 앱 재부팅시 다시 등록하기
    public void restoreAlarm()
    {
        for (FavoriteEntity alarmItem: alarmList) {
            try {
                insertAlarm(alarmItem);
            }
            catch (Exception e) {}
        }
    }

    // 알람 설정
    public void setAlarm(FavoriteEntity alarmItem)
    {
        insertAlarm(alarmItem);

        alarmList.remove(alarmItem);
        alarmList.add(alarmItem);
    }

    public void cancelAlarm(FavoriteEntity object)
    {
        deleteAlarm(Long.toString(object.getInsertDate().getTime()).substring(4));

        alarmList.remove(object);
    }

    public boolean checkAlarm(FavoriteEntity object)
    {
        if (existAlarm(Long.toString(object.getInsertDate().getTime()).substring(4))) {
            return true;
        }

        return false;
    }

    private void insertAlarm(FavoriteEntity alarmItem)
    {
        GregorianCalendar mCalendar = new GregorianCalendar();

//		long now = System.currentTimeMillis();
        long insertTime = alarmItem.getInsertDate().getTime();


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(insertTime);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayofmonth = c.get(Calendar.DAY_OF_MONTH);

        mCalendar.set(year, month, dayofmonth, alarmItem.getAlarmHour(), alarmItem.getAlarmMinute(), 0);

        //		if (insertTime > mCalendar.getTimeInMillis())
        //		{
        //			mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        //		}
        //
        //		if (now < mCalendar.getTimeInMillis())
        //		{
        //			// 입력받은 날짜의 time 을 알람코드로 사용
        //			String alarmCode = Long.toString(alarmItem.getInsertDate().getTime());
        //			String alarmMsg = "알람 설정";
        //
        //			saveAlarm(_context, Integer.parseInt(alarmCode), alarmMsg, mCalendar.getTimeInMillis(), alarmItem.isAlarmRepeat());
        //		}

        // 입력받은 날짜의 time 을 알람코드로 사용 / int 범위를 초과하기 때문에 앞에 4자리 자름
        String alarmCode = Long.toString(alarmItem.getInsertDate().getTime()).substring(4);
        String alarmMsg = alarmItem.getMemo();

        saveAlarm(Integer.parseInt(alarmCode), alarmMsg, mCalendar.getTimeInMillis(), alarmItem.isAlarmRepeat());

        if(alarmItem.isAlarmRepeat())
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    // 저장하기
    private void saveAlarm(int intent_id, String msg, long when, boolean isRepeat)
    {
        Intent intent = new Intent(_context, AlarmReceiver.class);
        intent.putExtra("MESSAGE", msg);
        intent.putExtra("NOTIID", intent_id);
        PendingIntent sender = PendingIntent.getBroadcast(_context, intent_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(isRepeat)
        {
            long interval = 0;
//			if(CommUtils.chkYun(2001))
//				interval = 366 * 24 * 60 * 60 * 1000;	// 윤년
//			else
//				interval = 365 * 24 * 60 * 60 * 1000;

            interval = 30 * 1000;	// 30초마다 반복 테스트

            mManager.setRepeating(AlarmManager.RTC_WAKEUP, when, interval, sender);
        }
        else
            mManager.set(AlarmManager.RTC_WAKEUP, when, sender);
    }

    private void deleteAlarm(String code)
    {
        final int intent_id = Integer.parseInt(code);

        Intent intent = new Intent(_context, AlarmReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(_context, intent_id, intent, PendingIntent.FLAG_NO_CREATE);
        if (sender != null) {
            mManager.cancel(sender);
            sender.cancel();
        }
    }

    private boolean existAlarm(String code) {
        boolean result = false;

        final int intent_id = Integer.parseInt(code);

        Intent intent = new Intent(_context.getApplicationContext(), AlarmReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(_context.getApplicationContext(), intent_id, intent, PendingIntent.FLAG_NO_CREATE);
        if (sender != null)
        {
            result = true;
        }
        return result;
    }


    //    private  void setSharedPreferences(){
    //		try {
    //			JSONArray jsonArray = new JSONArray();
    //			for(int i=0; i< alarmList.size(); i++) {
    //				FavoriteEntity object = alarmList.get(i);
    //				JSONObject json = object.getJson();
    //				jsonArray.put(json);
    //			}
    //            Editor ed = pref.edit();
    //            ed.putString("alarmObject", jsonArray.toString());
    //            ed.commit();
    //		} catch (Exception e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //	}
}
