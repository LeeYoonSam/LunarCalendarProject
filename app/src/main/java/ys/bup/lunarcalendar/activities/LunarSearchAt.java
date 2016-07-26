package ys.bup.lunarcalendar.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.entity.FavoriteEntity;

public class LunarSearchAt  extends BaseLoadingActivity {

	Realm realm;

	@BindView(R.id.btSearchDate)
	Button btSearchDate;

	@BindView(R.id.btAddFavorite)
	TextView btAddFavorite;

	@BindView(R.id.tvConvertDate)
	TextView tvConvertDate;

	@BindView(R.id.etMemo)
	EditText etMemo;

	@BindView(R.id.ivAlarm)
	ImageView ivAlarm;


	@BindView(R.id.overlayAlarm)
	AlarmAddOverlay overlayAlarm;

	AlarmAddOverlay.AlarmObject saveAlarm;

	String selectDate = "";
	CommUtils.LunarSolarEntity tmpDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.at_lunar_search);

		ButterKnife.bind(this);

		overlayAlarm.setAddAlarmListener(alarmCallback, LunarSearchAt.this);
		overlayAlarm.setVisibility(View.GONE);

		realm = Realm.getDefaultInstance();
	}

	@Override
	protected void onDestroy() {
		realm.close();
		super.onDestroy();
	}

	AlarmAddOverlay.AlarmAddListener alarmCallback = new AlarmAddOverlay.AlarmAddListener() {

		@Override
		public void clickAdd(AlarmAddOverlay.AlarmObject alarm) {

			saveAlarm = alarm;
			Log.d("AlarmAddLister", " Hour : " + alarm.getHour() + " Minute : " + alarm.getMinute());

			if(saveAlarm.getHour() != 999 && saveAlarm.getMinute() != 999)
				ivAlarm.setVisibility(View.VISIBLE);

			overlayAlarm.setVisibility(View.GONE);
		}
	};

	@OnClick(R.id.btAddFavorite)
	public void addFavorite()
	{
		try
		{
			// Persist your data easily
			realm.beginTransaction();
			FavoriteEntity tmp = realm.createObject(FavoriteEntity.class);

			String memo = etMemo.getText().toString();

			if(memo != null)
			{
				if(memo.length() < 1) {
					Toast.makeText(LunarSearchAt.this, getString(R.string.txt_memo), Toast.LENGTH_SHORT).show();
					return;
				} else {
					tmp.setMemo(memo);
				}
			}

			// 알람 저장
			if(saveAlarm != null)
			{
				// 알람이 유효할때만 저장하고 알람 상태값 변경하기
				if(saveAlarm.getHour() != 999 || saveAlarm.getMinute() != 999) {
					tmp.setAlarmHour(saveAlarm.getHour());
					tmp.setAlarmMinute(saveAlarm.getMinute());

					tmp.setAlarmOn(true);
				}
			}

			tmp.setLunarDate(tmpDate.getLunarDate());
			tmp.setSolarDate(tmpDate.getSolarDate());

			 // 알람 추가 작업해야함


			realm.commitTransaction();

			// 키보드 숨기기
			CommUtils.hideSoftInput(LunarSearchAt.this, etMemo);

			Toast.makeText(LunarSearchAt.this, "Add Success", Toast.LENGTH_SHORT).show();
			finish();

		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(LunarSearchAt.this, "입력 실패!\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
		}

	}

	@OnClick(R.id.btAddAlarm)
	public void showAddAlarmOverlay()
	{
		if(selectDate.length() == 8)
			overlayAlarm.setVisibility(View.VISIBLE);
		else
			Toast.makeText(LunarSearchAt.this, "날짜를 선택해 주세요", Toast.LENGTH_SHORT).show();
	}

	@OnClick(R.id.btSearchDate)
	public void searchDate() {
		DialogDatePicker();
	}

	// 데이터 피커 다이얼로그
	public void DialogDatePicker(){

		DatePickerDialog.OnDateSetListener mDateSetListener =
				new DatePickerDialog.OnDateSetListener() {
					// onDateSet method
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						int month = monthOfYear+1;
						int day = dayOfMonth;

						String sMonth = "";
						String sDay = "";

						if(month < 10)
							sMonth = "0" + String.valueOf(month);
						else
							sMonth = String.valueOf(month);

						if(day < 10)
							sDay = "0" + String.valueOf(day);
						else
							sDay = String.valueOf(day);

						selectDate = String.valueOf(year) + sMonth + sDay;

						Log.d("DatePicker", "DATE : " + selectDate);

						tmpDate = CommUtils.convertLunarToSolar(selectDate);
						tvConvertDate.setText("양력 " + tmpDate.getConvertDate());

						btSearchDate.setText(CommUtils.getShowFormat(selectDate));

						// 날짜 선택시 알람추가 버튼 및 메모 작성공간 보여주기
						btAddFavorite.setVisibility(View.VISIBLE);
						etMemo.setVisibility(View.VISIBLE);
					}
				};

		int cyear;
		int cmonth;
		int cday;

		if(selectDate.length() >= 6)
		{
			cyear = Integer.parseInt(selectDate.substring(0, 4));
			cmonth = Integer.parseInt(selectDate.substring(4, 6)) - 1;
			cday =Integer.parseInt( selectDate.substring(6));
		}
		else
		{
			Calendar c = Calendar.getInstance();

			cyear = c.get(Calendar.YEAR);
			cmonth = c.get(Calendar.MONTH);
			cday = c.get(Calendar.DAY_OF_MONTH);

			StringBuffer ret = new StringBuffer();
			ret.append(String.format("%04d", cyear));
			ret.append(String.format("%02d", cmonth + 1));
			ret.append(String.format("%02d", cday));

			String tmpData = CommUtils.converStringSolarToLunar(ret.toString());
			cyear = Integer.parseInt(tmpData.substring(0, 4));
			cmonth = Integer.parseInt(tmpData.substring(4, 6)) - 1;
			cday =Integer.parseInt( tmpData.substring(6));
		}

		DatePickerDialog alert = new DatePickerDialog(LunarSearchAt.this,  mDateSetListener, cyear, cmonth, cday);

		// 날짜제한 - 오늘 날짜 이상은 선택이 안되게
		//		alert.getDatePicker().setMaxDate(new Date().getTime());
		alert.show();
	}

	@Override
	public void onBackPressed() {

		if(overlayAlarm.getVisibility() == View.VISIBLE) {
			overlayAlarm.setVisibility(View.GONE);

			return;
		}

		super.onBackPressed();
	}
}
