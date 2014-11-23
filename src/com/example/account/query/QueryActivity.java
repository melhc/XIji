package com.example.account.query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.avos.avoscloud.AVAnalytics;
import com.example.account.add.SpinnerAdapter;
import com.example.account.main.MainActivity;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class QueryActivity extends Activity {
	private TextView time1, time2, query_tag;
	private Calendar c;
	private int year, year2, month, month2, day, day2;
	private String date1, date2;
	private Spinner sp1, sp3;
	private LinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_tag);
		intView();
		intTitleView();
		intSpinner();
		intDate();
		initBackView();
	}

	public void intView() {
		time1 = (TextView) this.findViewById(R.id.textView2);
		time2 = (TextView) this.findViewById(R.id.textView3);
		query_tag = (TextView) this.findViewById(R.id.query_tag);
		sp1 = (Spinner) this.findViewById(R.id.spinner1);
		sp3 = (Spinner) this.findViewById(R.id.spinner3);
		linearLayout = (LinearLayout) this.findViewById(R.id.foot2);
	}

	public void intTitleView() {
		TextView textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("统计");
	}

	public void intDate() {
		c = Calendar.getInstance();
		year = year2 = c.get(Calendar.YEAR);
		month = month2 = c.get(Calendar.MONTH) + 1;
		day = day2 = c.get(Calendar.DAY_OF_MONTH);
		date1 = year + " 年 " + month + " 月 " + day + " 日 ";
		date2 = year + 1 + " 年 " + month + " 月 " + day + " 日 ";
		time1.setText(date1);
		time2.setText(date2);
		query_tag.setText("确定");
		onTimeListener timeListener = new onTimeListener();
		time1.setOnClickListener(timeListener);
		time2.setOnClickListener(timeListener);
		linearLayout.setOnClickListener(timeListener);
	}

	public void intSpinner() {
		String[] mStringArray1 = getResources().getStringArray(
				R.array.category_array);
		SpinnerAdapter adapter = new SpinnerAdapter(this, mStringArray1);
		sp3.setAdapter(adapter);
		String[] mStringArray2 = getResources().getStringArray(
				R.array.project_income);
		SpinnerAdapter adapter2 = new SpinnerAdapter(this, mStringArray2);
		sp1.setAdapter(adapter2);
		sp3.setOnItemSelectedListener(new onSpinnerListener());

	}

	public class onSpinnerListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (position == 1) {
				String[] mStringArray2 = getResources().getStringArray(
						R.array.project_income);
				SpinnerAdapter adapter2 = new SpinnerAdapter(
						QueryActivity.this, mStringArray2);
				sp1.setAdapter(adapter2);
			} else if (position == 2) {
				String[] mStringArray3 = getResources().getStringArray(
						R.array.project_outcome);
				SpinnerAdapter adapter3 = new SpinnerAdapter(
						QueryActivity.this, mStringArray3);
				sp1.setAdapter(adapter3);
			} else if (position == 0) {
				String[] mStringArray = getResources().getStringArray(
						R.array.project_all);
				SpinnerAdapter adapter = new SpinnerAdapter(QueryActivity.this,
						mStringArray);
				sp1.setAdapter(adapter);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	public class onTimeListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.textView2:
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						QueryActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year2,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								year = year2;
								month = monthOfYear + 1;
								day = dayOfMonth;
								date1 = year + " 年 " + month + " 月 " + day
										+ " 日 ";
								time1.setText(date1);
							}
						}, year, month - 1, day);
				datePickerDialog.show();

				break;

			case R.id.textView3:
				DatePickerDialog datePickerDialog2 = new DatePickerDialog(
						QueryActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year3,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								year2 = year3;
								month2 = monthOfYear + 1;
								day2 = dayOfMonth;
								date2 = year2 + " 年 " + month2 + " 月 " + day2
										+ " 日 ";
								time2.setText(date2);
							}
						}, year2 + 1, month2 - 1, day2);
				datePickerDialog2.show();

				break;

			case R.id.foot2:
				Intent intent = new Intent(QueryActivity.this,
						QueryActivity2.class);
				intent.putExtra("query_start", changeMonth(year, month, day));
				intent.putExtra("query_end", changeMonth(year2, month2, day2));
				if (sp3.getSelectedItemId() == 0) {
					intent.putExtra("category", "不限");
					startActivity(intent);
				} else if (sp3.getSelectedItemId() == 1) {

					intent.putExtra("category", "收入");
					intent.putExtra(
							"project2",
							sp1.getItemAtPosition((int) sp1.getSelectedItemId())
									.toString());
					startActivity(intent);
				} else if (sp3.getSelectedItemId() == 2) {

					intent.putExtra("category", "支出");
					intent.putExtra(
							"project2",
							sp1.getItemAtPosition((int) sp1.getSelectedItemId())
									.toString());
					startActivity(intent);
				}

				break;
			}
		}
	}

	public void initBackView() {
		RelativeLayout back = (RelativeLayout) this.findViewById(R.id.r1_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getIntent() != null) {
					if (getIntent().getBooleanExtra("query_back", false) == true) {
						Intent intent = new Intent(QueryActivity.this,
								MainActivity.class);
						startActivity(intent);
					} else {
						finish();
					}
				}

			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onResume(this);
		TestinAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TestinAgent.onStop(this);// 此行必须放在super.onStop后
	}

	@SuppressLint("SimpleDateFormat")
	public static String changeMonth(int year, int month, int day) {
		@SuppressWarnings("deprecation")
		Date date3 = new Date(year - 1900, month - 1, day);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date2 = format.format(date3);
		return date2;
	}
}
