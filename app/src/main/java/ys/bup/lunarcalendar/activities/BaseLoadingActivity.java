package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import io.realm.Realm;

public class BaseLoadingActivity extends Activity {

	ProgressDialog pbLoading = null;

	Realm realm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	
	protected void startLoading()
	{
		if(pbLoading == null)
			return;
		
		if(!pbLoading.isShowing())
			pbLoading.show();
	}

	protected void endLoading()
	{
		if(pbLoading == null)
			return;
		
		pbLoading.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		pbLoading = null;

		realm.close();

		super.onDestroy();
	}
}
