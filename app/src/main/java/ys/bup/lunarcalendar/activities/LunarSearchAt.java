package ys.bup.lunarcalendar.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.entity.FavoriteEntity;


public class LunarSearchAt  extends BaseLoadingActivity implements OnClickListener {

	@BindView(R.id.tvYear)
	TextView tvYear;

	@BindView(R.id.tvMonth)
	TextView tvMonth;

	@BindView(R.id.tvDay)
	TextView tvDay;

	@BindView(R.id.btLunar)
	TextView btLunar;

	@BindView(R.id.btSolar)
	TextView btSolar;

	@BindView(R.id.tvConvertDate)
	TextView tvConvertDate;

	@BindView(R.id.btAddFavorite)
	Button btAddFavorite;
	
	@BindView(R.id.overlayAlarm)
	AlarmAddOverlay overlayAlarm;

	AlarmAddOverlay.AlarmObject saveAlarm;
	
	String selectDate = "";
	boolean isLunar = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.at_lunar_search);

		ButterKnife.bind(this);

		overlayAlarm.setAddAlarmListener(alarmCallback, LunarSearchAt.this);
		overlayAlarm.setVisibility(View.GONE);

		tvYear.setOnClickListener(this);
		tvMonth.setOnClickListener(this);
		tvDay.setOnClickListener(this);
	}

	AlarmAddOverlay.AlarmAddListener alarmCallback = new AlarmAddOverlay.AlarmAddListener() {

		@Override
		public void clickAdd(AlarmAddOverlay.AlarmObject alarm) {

			tvYear.setText(tvYear.getText().toString());
			tvMonth.setText(tvMonth.getText().toString());
			tvDay.setText(tvDay.getText().toString());
			
			saveAlarm = alarm;
			
			Log.d("AlarmAddLister", "Memo : " + alarm.memo + " Hour : " + alarm.getHour() + " Minute : " + alarm.getMinute());
			
			overlayAlarm.setVisibility(View.GONE);

			btAddFavorite.setVisibility(View.VISIBLE);
		}
	};

    @OnClick(R.id.btAddFavorite)
    public void addFavorite()
	{
		if(!checkInvalid()) {
			return;
		}

		// Persist your data easily
		realm.beginTransaction();
		FavoriteEntity tmp = realm.createObject(FavoriteEntity.class);
		tmp.setYear(tvYear.getText().toString());
		tmp.setMonth(tvMonth.getText().toString());
		tmp.setDay(tvDay.getText().toString());

		if(saveAlarm != null)
		{
			tmp.setHour(saveAlarm.getHour());
			tmp.setMinute(saveAlarm.getMinute());
			tmp.setMemo(saveAlarm.getMemo());
		}

		tmp.setLunar(isLunar);
		realm.commitTransaction();

		Toast.makeText(LunarSearchAt.this, "Add Success", Toast.LENGTH_SHORT).show();
		finish();
    }

	public boolean checkInvalid()
	{
		try {
			if(tvYear.getText().toString().length() < 1 || tvMonth.getText().toString().length() < 1 || tvDay.getText().toString().length() < 1) {
				Toast.makeText(LunarSearchAt.this, "검색하고 싶은 날짜를 선택하세요", Toast.LENGTH_SHORT).show();
				return false;
			}

			return true;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

    
	@OnClick(R.id.btLunar)
	public void setLunar()
	{
		isLunar = true;
		btLunar.setBackgroundColor(Color.BLUE);
		btSolar.setBackgroundColor(Color.GRAY);
		
		setConvertDate();
	}

	@OnClick(R.id.btSolar)
	public void setSolar()
	{
		isLunar = false;
		btLunar.setBackgroundColor(Color.GRAY);
		btSolar.setBackgroundColor(Color.BLUE);
		
		setConvertDate();
	}

	@OnClick(R.id.btAddAlarm)
	public void showAddAlarmOverlay()
	{
		overlayAlarm.setVisibility(View.VISIBLE);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvYear:
		case R.id.tvMonth:
		case R.id.tvDay:
			DialogDatePicker();
			break;

		default:
			break;
		}
	}

	public void setConvertDate()
	{
		Log.d("DatePicker", "DATE : " + selectDate);
		
		if(isLunar)
			tvConvertDate.setText("음력 " + CommUtils.converSolarToLunar(selectDate));
		else
			tvConvertDate.setText("양력 " + CommUtils.convertLunarToSolar(selectDate));
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
				
				// Log.d("DatePicker", "DATE : " + selectDate);
				
				// if(isLunar)
				// 	tvConvertDate.setText("음력 " + CommUtils.converSolarToLunar(selectDate));
				// else
				// 	tvConvertDate.setText("양력 " + CommUtils.convertLunarToSolar(selectDate));
				setConvertDate();
				
				tvYear.setText(String.valueOf(year));
				tvMonth.setText(String.valueOf(monthOfYear+1));
				tvDay.setText(String.valueOf(dayOfMonth));
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
		}
		
		DatePickerDialog alert = new DatePickerDialog(LunarSearchAt.this,  mDateSetListener, cyear, cmonth, cday);

		// 날짜제한 - 오늘 날짜 이상은 선택이 안되게
//		alert.getDatePicker().setMaxDate(new Date().getTime());
		alert.show();
	}

}
