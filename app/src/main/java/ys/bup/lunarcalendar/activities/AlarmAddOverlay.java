package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import ys.bup.lunarcalendar.R;

public class AlarmAddOverlay extends FrameLayout implements OnClickListener {

	EditText etMemo;

	TextView tvHour;
	TextView tvMinute;

	Button btAlarmAdd;
	
	Activity parentAt;

	private AlarmAddListener parentListener;
	private AlarmObject params = new AlarmObject();

	public interface AlarmAddListener {
		public void clickAdd(AlarmObject alarm);
	}

	public void setAddAlarmListener(AlarmAddListener listener, Activity act) {
		parentListener = listener;
		this.parentAt = act;
	}

	public AlarmAddOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(getContext());
		final View view = inflater.inflate(R.layout.overlay_add_alarm, this);

		etMemo = (EditText)view.findViewById(R.id.etMemo);

		tvHour = (TextView)view.findViewById(R.id.tvHour);
		tvHour.setOnClickListener(this);

		tvMinute = (TextView)view.findViewById(R.id.tvMinute);
		tvMinute.setOnClickListener(this);

		btAlarmAdd = (Button)view.findViewById(R.id.btAlarmAdd);
		btAlarmAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvHour:
		case R.id.tvMinute:
			callNumberPicker();
			break;

		case R.id.btAlarmAdd:

			params.setMemo(etMemo.getText().toString());

			if(checkInvalid()) {
				parentListener.clickAdd(params);
				CommUtils.hideSoftInput(parentAt, etMemo);
			}

			break;
		default:
			break;
		}
	}

	public boolean checkInvalid()
	{
		try {
			if(params.getMemo() == null || params.getMemo().length() < 1) {
				Toast.makeText(this.getContext(), "메모를 작성해주세요", Toast.LENGTH_SHORT).show();
				return false;
			}

			if(params.getHour() == 999 || params.getMinute() == 999) {
				Toast.makeText(this.getContext(), "시간을 지정해주세요", Toast.LENGTH_SHORT).show();
				return false;
			}

			return true;
		}
		catch (Exception e){
			e.printStackTrace();

			return false;
		}
	}

	// 알람 날짜 선택
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

		if(params.hour == 999 || params.minute == 999)
		{
			Calendar c = Calendar.getInstance();

			hour = c.get(Calendar.HOUR_OF_DAY);
			min = c.get(Calendar.MINUTE);
		}
		else
		{
			hour = params.hour;
			min = params.minute;
		}

		TimePickerDialog tPicker = new TimePickerDialog(getContext(), tCallBack, hour, min, true);
		tPicker.show();
	}



	// 알람 객체
	public class AlarmObject implements Comparable<AlarmObject>
	{
		String memo;
		int hour;
		int minute;

		public AlarmObject() {
			this.memo = null;
			this.hour = 999;
			this.minute = 999;
		}

		public AlarmObject(@IntRange(from=0, to=23) int hour, @IntRange(from=0, to=59) int minute) {
			this.hour = hour;
			this.minute = minute;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		@Override
		public int compareTo(AlarmObject t) {
			return (this.hour - t.hour)*3600 + (this.minute - t.minute)*60;
		}
	}

}
