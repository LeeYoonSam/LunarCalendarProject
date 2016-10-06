package ys.bup.lunarcalendar.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.common.CommUtils;
import ys.bup.lunarcalendar.common.Constants;
import ys.bup.lunarcalendar.entity.AlarmEntity;
import ys.bup.lunarcalendar.entity.FavoriteEntity;
import ys.bup.lunarcalendar.entity.LunarSolarEntity;
import ys.bup.lunarcalendar.receiver.AlarmTimeManager;

public class LunarSearchAt extends BaseLoadingActivity {

	Realm realm;

	@BindView(R.id.tvConvertLunarDate)
	TextView tvConvertLunarDate;

	@BindView(R.id.tvSearchDate)
	TextView tvSearchDate;

	@BindView(R.id.llDateArea)
	LinearLayout llDateArea;

	@BindView(R.id.btAddFavorite)
	TextView btAddFavorite;

	@BindView(R.id.tvConvertDate)
	TextView tvConvertDate;

	@BindView(R.id.etMemo)
	EditText etMemo;

	@BindView(R.id.ivAlarm)
	ImageView ivAlarm;

	@BindView(R.id.llBottomArea)
	LinearLayout llBottomArea;

	@BindView(R.id.overlayAlarm)
	AlarmAddOverlay overlayAlarm;

    AlarmEntity saveAlarm = null;

	String selectDate = "";
	LunarSolarEntity tmpDate = null;

	int statusMode;
    FavoriteEntity updateData = null;


	/**********************************
	 * BindString
	 **********************************/
	@BindString(R.string.txt_hint_select_date)
	String txt_hint_select_date;

	@BindString(R.string.txt_error_insert)
	String txt_error_insert;

	@BindString(R.string.txt_noti_on_alarm)
	String txt_noti_on_alarm;

	@BindString(R.string.txt_noti_off_alarm)
	String txt_noti_off_alarm;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.at_lunar_search);

		ButterKnife.bind(this);

		overlayAlarm.setAddAlarmListener(alarmCallback, LunarSearchAt.this);
		overlayAlarm.setVisibility(View.GONE);


		statusMode = getIntent().getIntExtra("statusMode", Constants.STATUS_NONE);

		if(statusMode == Constants.STATUS_EDIT)
			setEditmode();
	}

	// 수정하러 들어왔을때 데이터 복구
	public void setEditmode() {

		updateData = getIntent().getParcelableExtra("favoriteItem");

		etMemo.setText(updateData.getMemo());
		tvConvertLunarDate.setText(updateData.showLunarDate());
		tvConvertDate.setText(updateData.showSolarDate());

        selectDate = updateData.getLunarDate();
        tmpDate = CommUtils.convertLunarToSolar(selectDate);

        if(updateData.isAlarmOn())
        {
            if(updateData.getAlarmHour() != 999 || updateData.getAlarmHour() != 0  && updateData.getAlarmMinute() != 999 || updateData.getAlarmMinute() != 0) {
                saveAlarm = new AlarmEntity();

                saveAlarm.setHour(updateData.getAlarmHour());
                saveAlarm.setMinute(updateData.getAlarmMinute());

                ivAlarm.setVisibility(View.VISIBLE);

                overlayAlarm.setAlarmEntity(saveAlarm);
            }
        }

		// 날짜입력 후 Visible, Gone 처리
		setSaveOnStatus();
	}


	@Override
	protected void onResume() {
		super.onResume();

		realm = Realm.getDefaultInstance();
	}

	@Override
	protected void onDestroy() {
		realm.close();
		super.onDestroy();
	}

	AlarmAddOverlay.AlarmAddListener alarmCallback = new AlarmAddOverlay.AlarmAddListener() {

		@Override
		public void clickAdd(AlarmEntity alarm) {

			saveAlarm = alarm;
			Log.d("AlarmAddLister", " Hour : " + alarm.getHour() + " Minute : " + alarm.getMinute());

			if(saveAlarm.getHour() != 999 || saveAlarm.getHour() != 0 && saveAlarm.getMinute() != 999 || saveAlarm.getMinute() != 0)
				ivAlarm.setVisibility(View.VISIBLE);

			overlayAlarm.setVisibility(View.GONE);

			Toast.makeText(LunarSearchAt.this, txt_noti_on_alarm, Toast.LENGTH_SHORT).show();
		}
	};

	@OnClick(R.id.ivAlarm)
	public void offAlarm() {
		saveAlarm = null;
		ivAlarm.setVisibility(View.GONE);

		Toast.makeText(this, txt_noti_off_alarm, Toast.LENGTH_SHORT).show();
	}

	@OnClick(R.id.btAddFavorite)
	public void addFavorite()
	{
		try
		{
            FavoriteEntity tmp = null;

            // 업데이트
            if(statusMode == Constants.STATUS_EDIT && updateData != null) {

                tmp = realm.where(FavoriteEntity.class)
                        .equalTo("seq", updateData.getSeq())
                        .findFirst();
            }

            realm.beginTransaction();

            int seq = 1;

            // 신규 추가
            if(tmp == null)
            {
                try {
                    seq = realm.where(FavoriteEntity.class).max("seq").intValue() + 1;
                } catch(Exception ex) {
                    seq = 1;
                }

                tmp = realm.createObject(FavoriteEntity.class);

                // 신규 추가일때만 시퀀스 생성
                tmp.setSeq(seq);
            }

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

            tmp.setLunarDate(tmpDate.getLunarDate());
            tmp.setSolarDate(tmpDate.getSolarDate());
            tmp.setInsertDate(tmpDate.getInsertDate());

            // 알람 추가 작업해야함
            if(saveAlarm != null)
            {
                // 알람이 유효할때만 저장하고 알람 상태값 변경하기
                if(saveAlarm.getHour() != 999 || saveAlarm.getHour() != 0 && saveAlarm.getMinute() != 999 || saveAlarm.getMinute() != 0) {
                    tmp.setAlarmHour(saveAlarm.getHour());
                    tmp.setAlarmMinute(saveAlarm.getMinute());

                    tmp.setAlarmOn(true);

                    // 입력 성공후에 알람 설정하도록 수정해야함
                    new AlarmTimeManager(LunarSearchAt.this).setAlarm(tmp);
                }
            } else {
				tmp.setAlarmHour(999);
				tmp.setAlarmMinute(999);
				tmp.setAlarmOn(false);

				// 알람 취소 시키기
				new AlarmTimeManager(LunarSearchAt.this).cancelAlarm(tmp);
			}

            realm.commitTransaction();

            // 키보드 숨기기
            CommUtils.hideSoftInput(LunarSearchAt.this, etMemo);

            finish();

		} catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(LunarSearchAt.this, txt_error_insert, Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	@OnClick(R.id.btAddAlarm)
	public void showAddAlarmOverlay()
	{
		if(selectDate.length() == 8)
			overlayAlarm.setVisibility(View.VISIBLE);
		else
			Toast.makeText(LunarSearchAt.this, txt_hint_select_date, Toast.LENGTH_SHORT).show();
	}

//	@OnClick(R.id.btSearchDate)
	@OnClick(R.id.tvSearchDate)
	public void searchDate() {
		DialogDatePicker();
	}

	// 데이트 피커 다이얼로그
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

						tvConvertLunarDate.setText("음력: " + CommUtils.getShowFormat(selectDate));
						tvConvertDate.setText("양력: " + tmpDate.getConvertDate());

						// 날짜입력 후 Visible, Gone 처리
						setSaveOnStatus();
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

	public void setSaveOnStatus() {
		// 알람추가 / 저장하기 영역 보여주기
		llBottomArea.setVisibility(View.VISIBLE);

		// 선택글자 숨기고 날짜 컨버팅한 결과 영역 보여주기
		tvSearchDate.setVisibility(View.GONE);
		llDateArea.setVisibility(View.VISIBLE);
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
