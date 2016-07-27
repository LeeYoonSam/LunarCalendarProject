package ys.bup.lunarcalendar.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.GregorianCalendar;

import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.activities.IntroAt;

/**
 * Created by ys on 2016. 7. 27..
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();
        Log.d("AlarmReceiver", "AlarmReceiver " + action);

        if(action == null) {
            setNotification(context, intent);
        }
        else if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
            AlarmTimeManager alarmObject = new AlarmTimeManager(context.getApplicationContext());;
            alarmObject.restoreAlarm();
        }
        else {
            setNotification(context, intent);
        }
    }

    private void setNotification(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        String pushMessage = bundle.getString("MESSAGE");
        int notiID = bundle.getInt("NOTIID");


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, IntroAt.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(context);
        mCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mCompatBuilder.setTicker(pushMessage);
        mCompatBuilder.setWhen(new GregorianCalendar().getTimeInMillis());
        mCompatBuilder.setContentTitle(context.getString(R.string.app_name));
        mCompatBuilder.setContentText(pushMessage);
        mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mCompatBuilder.setContentIntent(pendingIntent);
        mCompatBuilder.setAutoCancel(true);
        nm.notify(notiID, mCompatBuilder.build());
    }
}
