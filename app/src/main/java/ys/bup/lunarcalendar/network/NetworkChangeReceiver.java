package ys.bup.lunarcalendar.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ys.bup.lunarcalendar.activities.BaseLoadingActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
//		String status = NetworkUtil.getConnectivityStatusString(context);
//		Toast.makeText(context, status, Toast.LENGTH_LONG).show();
		
		Intent sendData = new Intent(BaseLoadingActivity.ACTION_NETWORKCHANGE);
		sendData.putExtra("networkStatus", NetworkUtil.getConnectivityStatus(context));
		context.sendBroadcast(sendData);
	}
}
