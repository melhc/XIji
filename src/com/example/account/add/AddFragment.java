package com.example.account.add;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVAnalytics;

import com.example.account.main.FragmentListener;
import com.example.account.utils.DateModel;
import com.example.account.utils.MonthUtils;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemLongClickListener;

public class AddFragment extends Fragment implements
		android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

	private ListView listView;
	private List<HashMap<String, String>> list;
	private MyAdapter adapter;
	private android.support.v4.app.LoaderManager loaderManager;
	// private Intent rechargeIntent;
	private FragmentListener listener;
	private AlertDialog.Builder builder;
	private PreferencesUtils preferencesUtils;
	private static HashMap<String, Object> map;
	private String date, date2;
	private TextView income, outlay, budget, month2;
	private int year, month, day;
	private MySQLiteOpenHelper helper;
	private SQLiteDatabase database;
	private TextView textView, textYear;
	private boolean flag, flag2;
	private View view;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				setDataText();// 设置时间日期
			}

		};
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (FragmentListener) activity;
		} catch (Exception e) {

			Log.i("TAG", "------->" + e.getMessage());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout fl = new FrameLayout(getActivity());
		view = inflater.inflate(R.layout.add, null);
		initView();// 获得设置控件
		initData();// 获取时期日期

		setUtils();// 设置数据库、共享参数
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						getActivity(), new OnDateSetListener() {

							@SuppressLint("SimpleDateFormat")
							@Override
							public void onDateSet(DatePicker view, int year2,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								month = monthOfYear + 1;
								date = month + "月" + dayOfMonth + "日";
								year = year2;
								@SuppressWarnings("deprecation")
								Date date3 = new Date(year - 1900, month - 1,
										dayOfMonth);
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyy-MM-dd");
								date2 = format.format(date3);
								setDataText();
								loaderManager.getLoader(1001)
										.onContentChanged();
							}
						}, year, month - 1, day);
				datePickerDialog.show();
			}
		});
		loaderManager = getLoaderManager();
		loaderManager.initLoader(1001, null, this);
		loaderManager.getLoader(1001).onContentChanged();

		/*
		 * listView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, final View
		 * view, int position, long id) { // TODO Auto-generated method stub
		 * final TextView textView = (TextView) view
		 * .findViewById(R.id.textView1); String s = "username='"; Cursor cursor
		 * = null; s = s + map.get("username").toString() + "'"; try { cursor =
		 * database.query("budget", new String[] { "username", "sum", "time",
		 * "category", "project", "remarks" }, s + " and " + "time=" + "'" +
		 * textView.getText().toString() + "'", null, null, null, null); String
		 * category; while (cursor.moveToNext()) { if
		 * (cursor.getString(cursor.getColumnIndex("category")) .equals("收入")) {
		 * category = "收入"; } else { category = "支出"; } String sum =
		 * cursor.getString(cursor .getColumnIndex("sum")); String project =
		 * cursor.getString(cursor .getColumnIndex("project")); String remarks =
		 * null; remarks = cursor.getString(cursor .getColumnIndex("remarks"));
		 * rechargeIntent.putExtra("date", date2);
		 * rechargeIntent.putExtra("year", year);
		 * rechargeIntent.putExtra("category", category);
		 * rechargeIntent.putExtra("sum", sum);
		 * rechargeIntent.putExtra("project", project);
		 * rechargeIntent.putExtra("time", textView.getText() .toString());
		 * rechargeIntent.putExtra("remarks", remarks);
		 * 
		 * } } catch (Exception e) { // TODO: handle exception
		 * 
		 * } rechargeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 * startActivity(rechargeIntent); flag2 = true; }
		 * 
		 * });
		 */
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, int position, long id) {
				// TODO Auto-generated method stub
				builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("提示");
				builder.setMessage("您确定要删除该条记账信息吗？");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						TextView textView = (TextView) view
								.findViewById(R.id.textView1);
						database.delete("budget", "time=?",
								new String[] { textView.getText().toString(), });
						loaderManager.getLoader(1001).onContentChanged();// 若状态改变，触发这个方法
						try {
							if (listener == null) {
								listener = (FragmentListener) getActivity();
								listener.onFragmentClickListener(1);
							} else {
								listener.onFragmentClickListener(1);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						flag2 = true;
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				builder.create().show();
				return true;
			}
		});
		fl.addView(view);
		return fl;
	}

	public void initView() {
		listView = (ListView) view.findViewById(R.id.listView1);
		View head = LayoutInflater.from(getActivity()).inflate(R.layout.header,
				null);
		listView.addHeaderView(head, null, false);
		listView.setCacheColorHint(0);
		textView = (TextView) head.findViewById(R.id.time);
		income = (TextView) view.findViewById(R.id.income_sum);
		outlay = (TextView) view.findViewById(R.id.Outlay_sum);
		budget = (TextView) view.findViewById(R.id.budget_sum);
		ImageView view1 = (ImageView) view.findViewById(R.id.i1);
		ImageView view2 = (ImageView) view.findViewById(R.id.i2);
		ImageviewListener imageviewListener = new ImageviewListener();
		view1.setOnClickListener(imageviewListener);
		view2.setOnClickListener(imageviewListener);
		textYear = (TextView) view.findViewById(R.id.year);
		month2 = (TextView) view.findViewById(R.id.month);
	}

	public void setDataText() {
		textYear.setText(year + "");
		month2.setText(MonthUtils.changeMonth(month - 1));
		textView.setText(date);
	}

	public void initData() {

		new Thread() {
			@SuppressLint("SimpleDateFormat")
			public void run() {
				Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				day = c.get(Calendar.DATE);
				date = month + "月" + day + "日";
				@SuppressWarnings("deprecation")
				Date date3 = new Date(year - 1900, month - 1, day);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				date2 = format.format(date3);
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	public void setUtils() {
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				preferencesUtils = new PreferencesUtils(getActivity());
				map = (HashMap<String, Object>) preferencesUtils
						.getMsg("login");
				helper = new MySQLiteOpenHelper(getActivity(),
						DateModel.DB_VERSION);
				database = helper.getWritableDatabase();
				// rechargeIntent = new Intent(getActivity(),
				// AddActivity.class);
			};
		}.start();
	}

	public class ImageviewListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread() {
				public void run() {
					Intent intent = new Intent(getActivity(), AddActivity.class);
					intent.putExtra("add_date", date2);
					intent.putExtra("add_year", year);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.zoomin,
							R.anim.zoomout);
				};
			}.start();
			flag2 = true;
		}

	}

	public static class MyLoader extends
			android.support.v4.content.AsyncTaskLoader<Cursor> {

		public MyLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onStartLoading() {
			// TODO Auto-generated method stub
			super.onStartLoading();
			if (takeContentChanged()) {
				forceLoad();
			}

		}

		@Override
		public Cursor loadInBackground() {
			// TODO Auto-generated method stub
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext(),
					DateModel.DB_VERSION);
			SQLiteDatabase database = helper.getReadableDatabase();
			String s = "username='";
			s = s + map.get("username").toString() + "'";
			Cursor cursor = database.query("budget", new String[] { "username",
					"year", "date", "sum", "time", "category", "project",
					"remarks" }, s, null, null, null, null);
			return cursor;
		}
	}

	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> arg0,
			Cursor cursor) {
		// TODO Auto-generated method stub
		list = new ArrayList<HashMap<String, String>>();
		double i1 = 0, i2 = 0;
		double i3 = 0, i4 = 0;
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("j1", cursor.getString(cursor.getColumnIndex("sum")));
			map.put("j2", cursor.getString(cursor.getColumnIndex("time")));
			map.put("j3", cursor.getString(cursor.getColumnIndex("category")));
			map.put("j4", cursor.getString(cursor.getColumnIndex("project")));
			map.put("j5", cursor.getString(cursor.getColumnIndex("year")));
			map.put("j6", cursor.getString(cursor.getColumnIndex("remarks")));
			map.put("j7", cursor.getString(cursor.getColumnIndex("date")));
			if (cursor.getString(cursor.getColumnIndex("category"))
					.equals("收入")) {
				if (!cursor.getString(cursor.getColumnIndex("sum")).equals("")) {
					i1 = i1
							+ Double.valueOf(
									cursor.getString(cursor
											.getColumnIndex("sum")))
									.doubleValue();
					if (cursor.getString(cursor.getColumnIndex("date"))
							.startsWith(date2.substring(0, 7))) {
						i3 = i3
								+ Double.valueOf(
										cursor.getString(cursor
												.getColumnIndex("sum")))
										.doubleValue();
					}
				}

			}
			if (cursor.getString(cursor.getColumnIndex("category"))
					.equals("支出")) {
				if (!cursor.getString(cursor.getColumnIndex("sum")).equals("")) {
					i2 = i2
							+ Double.valueOf(
									cursor.getString(cursor
											.getColumnIndex("sum")))
									.doubleValue();
					if (cursor.getString(cursor.getColumnIndex("date"))
							.startsWith(date2.substring(0, 7))) {
						i4 = i4
								+ Double.valueOf(
										cursor.getString(cursor
												.getColumnIndex("sum")))
										.doubleValue();
					}

				}

			}

			if (cursor.getString(cursor.getColumnIndex("date")).equals(date2)) {
				list.add(map);

			}

		}
		cursor.close();
		// 获得屏幕宽度

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		adapter = new MyAdapter(getActivity(), dm.widthPixels);
		adapter.initData(list);
		DecimalFormat df = new DecimalFormat("#####0.0");
		listView.setAdapter(adapter);

		income.setText(df.format(i3) + "");
		outlay.setText(df.format(i4) + "");

		budget.setText(df.format(i1 - i2) + "");
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public android.support.v4.content.Loader<Cursor> onCreateLoader(int arg0,
			Bundle arg1) {
		// TODO Auto-generated method stub
		return new MyLoader(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onFragmentStart("add-fragment");
		if (flag) {
			loaderManager.getLoader(1001).onContentChanged();

		}
		flag = true;
		if (!flag2) {
			initData();
			setDataText();

		}
		flag2 = false;

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onFragmentEnd("add-fragment");
	}

}
