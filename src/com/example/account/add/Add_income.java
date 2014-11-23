package com.example.account.add;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVAnalytics;
import com.example.account.utils.DateModel;
import com.example.account.utils.MyEditText;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class Add_income extends Fragment {
	private MyEditText editText, remarks_text;
	private TextView time_clcok;
	private Spinner spinner;
	private String time, date2, edit, remarks;
	private int year, month, day;
	private boolean flag;
	private SpinnerAdapter adapter;
	@SuppressLint("HandlerLeak")
	private Handler myhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				time_clcok.setText(time);
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.add_income, null);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		String[] mStringArray = getResources().getStringArray(
				R.array.project_income);
		adapter = new SpinnerAdapter(getActivity(), mStringArray);
		spinner.setAdapter(adapter);
		editText = (MyEditText) view.findViewById(R.id.editText1);
		remarks_text = (MyEditText) view.findViewById(R.id.remarks_text);
		time_clcok = (TextView) view.findViewById(R.id.clock_time);
		time_clcok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerDialog timePickerDialog = new TimePickerDialog(
						getActivity(), new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								String m,h;
								if (hourOfDay < 10) {
									h = "0" + hourOfDay;
								} else {
									h = hourOfDay + "";
								}
								if (minute < 10) {
									m = "0" + minute;
								} else {
									m = minute + "";
								}
								int second = (int) ((Math.random() * 50) + 10);
								time = h + ":" + m + ":" + second;
								time_clcok.setText(time);
							}
						}, Integer.valueOf(time.substring(0, 2)).intValue(),
						Integer.valueOf(time.substring(3, 5)).intValue(), true);
				timePickerDialog.show();
			}
		});
		setTimeText();// 设置时间日期
		getAddIntent();// 获取参数
		Button cancel = (Button) view.findViewById(R.id.button1);
		Button ok = (Button) view.findViewById(R.id.button2);
		buttonClickListener clickListener = new buttonClickListener();
		cancel.setOnClickListener(clickListener);
		ok.setOnClickListener(clickListener);
		return view;
	}

	class buttonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button1:
				getActivity().finish();
				break;
			case R.id.button2:
				new Thread() {
					public void run() {
						PreferencesUtils preferencesUtils = new PreferencesUtils(
								getActivity());
						@SuppressWarnings("unchecked")
						HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
								.getMsg("login");
						MySQLiteOpenHelper helper = new MySQLiteOpenHelper(
								getActivity(), DateModel.DB_VERSION);
						SQLiteDatabase database = helper.getWritableDatabase();
						ContentValues values = new ContentValues();

						if (map != null && !map.isEmpty()) {
							values.put("username", map.get("username")
									.toString());
						}
						if (!flag) {
							edit = editText.getText().toString();
							remarks = remarks_text.getText().toString();
						}
						if (flag && !edit.equals(editText.getText().toString())) {
							edit = editText.getText().toString();
						}
						if (flag
								&& !remarks.equals(remarks_text.getText()
										.toString())) {
							remarks = remarks_text.getText().toString();
						}
						if (edit.isEmpty()) {
							edit = "0";
						}
						double i=Double.valueOf(edit).doubleValue();
						DecimalFormat df = new DecimalFormat("#####0.0");
						values.put("sum", df.format(i));
						values.put(
								"project",
								spinner.getItemAtPosition(
										(int) spinner.getSelectedItemId())
										.toString());
						values.put("year", year);
						values.put("date", date2);
						if (getActivity().getIntent() != null) {
							Intent add_intent = getActivity().getIntent();
							if (add_intent.getStringExtra("add_date") != null) {
								String date = add_intent
										.getStringExtra("add_date");
								values.put("year", add_intent.getIntExtra(
										"add_year", 2014));
								values.put("date", date);
							}
						}
						values.put("time", time);
						values.put("remarks", remarks);
						values.put("category", "收入");
						if (!flag) {
							database.insert("budget", null, values);
						} else {
							database.update("budget", values, "time=?",
									new String[] { time });
							flag = false;
						}
					};
				}.start();
				getActivity().finish();
				break;

			default:
				break;
			}
		}

	}

	@SuppressLint("SimpleDateFormat")
	public void setTimeText() {
		new Thread() {
			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date(System.currentTimeMillis());
				time = format.format(date);
				Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				day = c.get(Calendar.DATE);
				date2 = month + "月" + day + "日";
				myhandler.sendEmptyMessage(1);
			};
		}.start();

	}

	public void getAddIntent() {
		new Thread() {
			public void run() {
				Intent intent = getActivity().getIntent();
				if (intent != null) {
					if (intent.getStringExtra("category") != null) {
						if (intent.getStringExtra("category").equals("支出")) {
							String project = intent.getStringExtra("project");
							spinner.setSelection(adapter.getPosition(project));
						}
						edit = intent.getStringExtra("sum");
						editText.setText(edit);
						editText.setSelection(edit.length());
						remarks = intent.getStringExtra("remarks");
						remarks_text.setText(remarks);
						year = intent.getIntExtra("year", 2014);
						date2 = intent.getStringExtra("date");
						time = intent.getStringExtra("time");
						time_clcok.setText(time);
						flag = true;
					}

				}
			};
		}.start();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onFragmentStart("add-income-fragment");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onFragmentEnd("add-income-fragment");
	}

}