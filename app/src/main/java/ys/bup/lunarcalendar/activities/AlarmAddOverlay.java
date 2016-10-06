package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ys.bup.lunarcalendar.R;
import ys.bup.lunarcalendar.entity.AlarmEntity;


public class AlarmAddOverlay extends RelativeLayout {

	@BindView(R.id.tvHour)
	TextView tvHour;

	@BindView(R.id.tvMinute)
	TextView tvMinute;

	@BindView(R.id.btAlarmAdd)
	Button btAlarmAdd;

	Activity parentAt;

	private AlarmAddListener parentListener;
	private AlarmEntity params = new AlarmEntity();

	public interface AlarmAddListener {
		public void clickAdd(AlarmEntity alarm);
	}

	public void setAddAlarmListener(AlarmAddListener listener, Activity act) {
		parentListener = listener;
		this.parentAt = act;
	}

	public AlarmAddOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(getContext());
		final View view = inflater.inflate(R.layout.overlay_add_alarm, this);

		ButterKnife.bind(this, view);
	}

	public void setAlarmEntity(AlarmEntity alarm) {
		params = alarm;

        if(params.getHour() != 999 && params.getMinute() != 999) {
            tvHour.setText(Integer.toString(params.getHour()));
            tvMinute.setText(Integer.toString(params.getMinute()));
        }
	}

	@OnClick({R.id.tvHour, R.id.tvMinute})
	public void onClickShowPicker() {
		callNumberPicker();
	}

	@OnClick(R.id.btAlarmAdd)
	public void onClickAddAlarm() {
		if(params.getHour() == 999 || params.getMinute() == 999) {
			Toast.makeText(this.getContext(), "시간을 선택 주세요.", Toast.LENGTH_SHORT).show();
			return;
		}

		parentListener.clickAdd(params);
	}

	public void callNumberPicker()
	{
		// timepicker set Callback
		OnTimeSetListener tCallBack = new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				try
				{
					params.setHour(hourOfDay);
					params.setMinute(minute);

					tvHour.setText(Integer.toString(hourOfDay));
					tvMinute.setText(Integer.toString(minute));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};

		int hour;
		int min;

		if(params.getHour() == 999 || params.getMinute() == 999)
		{
			Calendar c = Calendar.getInstance();

			hour = c.get(Calendar.HOUR_OF_DAY);
			min = c.get(Calendar.MINUTE);
		}
		else
		{
			hour = params.getHour();
			min = params.getMinute();
		}

		TimePickerDialog tPicker = new TimePickerDialog(getContext(), tCallBack, hour, min, true);
		tPicker.show();
	}



//	// 알람 객체
//	public class AlarmObject implements Comparable<AlarmObject>
//	{
//		int hour;
//		int minute;
//
//		public AlarmObject() {
//			this.hour = 999;
//			this.minute = 999;
//		}
//
//		public AlarmObject(@IntRange(from=0, to=23) int hour, @IntRange(from=0, to=59) int minute) {
//			this.hour = hour;
//			this.minute = minute;
//		}
//
//		public int getHour() {
//			return hour;
//		}
//
//		public void setHour(int hour) {
//			this.hour = hour;
//		}
//
//		public int getMinute() {
//			return minute;
//		}
//
//		public void setMinute(int minute) {
//			this.minute = minute;
//		}
//
//		@Override
//		public int compareTo(AlarmObject t) {
//			return (this.hour - t.hour)*3600 + (this.minute - t.minute)*60;
//		}
//	}
}
