package com.example.account.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVAnalytics;

import com.example.account.utils.DateModel;

import com.example.account.utils.PreferencesUtils;

import com.melhc.pirechat.OnDateChangedLinstener;
import com.melhc.pirechat.StatisticsView;
import com.melhc.xiji.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class QueryFragment extends Fragment {

	private SQLiteOpenHelper helper;
	private SQLiteDatabase database;
	private List<HashMap<String, Object>> list;
	private int month, year;
	private boolean flag;
	private Cursor cursor;
	private String s;
	private double sum2;
	private com.melhc.pirechat.StatisticsView mView;
	private double total = 1000;
	private float[] items;
	private String[] type;
	private FrameLayout f1;
	private String myCategory;
	@SuppressLint("HandlerLeak")
	private Handler myhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				mView = new StatisticsView(getActivity(), items, total, type);
				mView.setCurrDate(year, month);
				mView.setDateChangedListener(new dateSet());
				initFoot(mView);
				f1.addView(mView);
			}

		};
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initUtils();// 设置数据库、共享参数
		f1 = new FrameLayout(getActivity());
		new QueryTask(year + "-" + month, myCategory).start();
		return f1;
	}

	public void update() {

		mView.update(year + "-" + month);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onFragmentStart("query-fragment");
		if (flag) {
			mView.update(year + "-" + month);
		}
		flag = true;

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onFragmentEnd("query-fragment");
	}

	public void initFoot(View view) {
		LinearLayout query_tag = (LinearLayout) view.findViewById(R.id.foot2);
		query_tag.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), QueryActivity.class);
				startActivity(intent);
				flag = false;
			}
		});
	}

	public void initUtils() {

		PreferencesUtils preferencesUtils = new PreferencesUtils(getActivity());
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		s = "username='";
		s = s + map.get("username").toString() + "'";
		if (map.get("category") == null) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("category", "支出");
			preferencesUtils.saveMsg("login", map1);
			myCategory = "支出";
		} else {
			myCategory = map.get("category").toString();
		}

		helper = new MySQLiteOpenHelper(getActivity(), DateModel.DB_VERSION);
		database = helper.getReadableDatabase();
		Calendar calendar = Calendar.getInstance();
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);

	}

	public class dateSet implements OnDateChangedLinstener {

		@Override
		public void onDateChanged(String startDate, String endDate) {
			// TODO Auto-generated method stub

		}

	}

	public class QueryTask extends Thread {
		private String querydate;
		private String category;

		public QueryTask(String querydate, String category) {
			// TODO Auto-generated constructor stub
			this.querydate = querydate;
			this.category = category;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			list = new ArrayList<HashMap<String, Object>>();
			String s3 = "category='" + category + "'";
			cursor = database.query("budget", new String[] { "sum", "year",
					"date", "time", "category", "project" }, "date like'"
					+ querydate + "%'" + " and " + s + " and " + s3, null,
					null, null, "category");

			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("time", cursor.getString(cursor.getColumnIndex("time")));
				map.put("sum", cursor.getString(cursor.getColumnIndex("sum")));
				map.put("year", cursor.getString(cursor.getColumnIndex("year")));
				map.put("date", cursor.getString(cursor.getColumnIndex("date")));
				map.put("project",
						cursor.getString(cursor.getColumnIndex("project")));
				map.put("category",
						cursor.getString(cursor.getColumnIndex("category")));

				if (!cursor.getString(cursor.getColumnIndex("sum")).isEmpty()) {
					sum2 = sum2
							+ Double.valueOf(
									cursor.getString(cursor
											.getColumnIndex("sum")))
									.doubleValue();
				}

				boolean flag = false;
				for (int i = 0; i < list.size(); i++) {
					if (map.get("project").toString()
							.equals(list.get(i).get("project").toString())) {
						if (!map.get("sum").toString().isEmpty()) {
							double j = Double
									.valueOf(map.get("sum").toString())
									.doubleValue()
									+ Double.valueOf(
											list.get(i).get("sum").toString())
											.doubleValue();
							list.get(i).put("sum", j + "");
							flag = true;
						}

					}
				}

				if (!flag) {

					list.add(map);

				}

			}
			cursor.close();
			total = sum2;
			sum2 = 0;
			items = new float[list.size()];
			type = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				items[i] = (float) ((float) Math.round((float) (Double.valueOf(
						list.get(i).get("sum").toString()).intValue() * 100)) / total);
				type[i] = list.get(i).get("project").toString();
			}
			Message message = Message.obtain();
			message.what = 1;
			myhandler.sendMessage(message);

		}
	}

}
