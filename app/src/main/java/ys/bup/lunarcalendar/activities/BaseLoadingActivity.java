package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import io.realm.Realm;
import ys.bup.lunarcalendar.network.NetworkUtil;

public class BaseLoadingActivity extends Activity {
	
	public final static String ACTION_NETWORKCHANGE = "action.network.onchange";
	
	ProgressDialog pbLoading = null;

	Realm realm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter intentFilter = new IntentFilter(ACTION_NETWORKCHANGE);
		registerReceiver(mRecevier, intentFilter);
		
		if(pbLoading == null)
			createLoading();

		realm = Realm.getDefaultInstance();
	}

	public void createLoading()
	{
		pbLoading = new ProgressDialog(this);
		pbLoading.setMessage("Loading...");
		pbLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pbLoading.setCancelable(true);
	}
	
	public void startLoading()
	{
		if(pbLoading == null)
			return;
		
		if(!pbLoading.isShowing())
			pbLoading.show();
	}
	
	public void endLoading()
	{
		if(pbLoading == null)
			return;
		
		pbLoading.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		pbLoading = null;
		
		unregisterReceiver(mRecevier);
		realm.close();

		super.onDestroy();
	}
	
	private BroadcastReceiver mRecevier = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_NETWORKCHANGE)) {
				int networkStatus = intent.getIntExtra("networkStatus", NetworkUtil.TYPE_NOT_CONNECTED);
				onChangeNetwork(networkStatus);
			}
		}
	};
	
	public void onChangeNetwork(int type) {}
		
}
