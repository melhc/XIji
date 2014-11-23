package com.example.account.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVAnalytics;
import com.example.account.utils.DateModel;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class QueryActivity2 extends Activity {

	private ListView listView;
	private SQLiteOpenHelper helper;
	private SQLiteDatabase database;
	private List<HashMap<String, String>> list;
	private QueryAdapter adapter;
	private PreferencesUtils preferencesUtils;
	private HashMap<String, Object> map;
	private double sum1, sum2;
	private Cursor cursor;
	private String date1, date2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query);
		initTitleView();
		initBackView();
		listView = (ListView) this.findViewById(R.id.listView1);
		listView.setCacheColorHint(0);
		
		adapter = new QueryAdapter(this);
		new AsyncDate().execute();
	}
	public class AsyncDate extends
			AsyncTask<Void, Void, List<HashMap<String, String>>> {
		@SuppressWarnings("unchecked")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			preferencesUtils = new PreferencesUtils(QueryActivity2.this);
			map = (HashMap<String, Object>) preferencesUtils.getMsg("login");
			helper = new MySQLiteOpenHelper(QueryActivity2.this,
					DateModel.DB_VERSION);
			database = helper.getReadableDatabase();
			list = new ArrayList<HashMap<String, String>>();
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String s = "username='";
			s = s + map.get("username").toString() + "'";
			if (getIntent() != null) {
				if (getIntent().getStringExtra("category") != null) {
					Intent query = getIntent();
					date1 = query.getStringExtra("query_start");
					date2 = query.getStringExtra("query_end");
					String project = query.getStringExtra("project2");
					String date = "date Between'" + date1 + "'" + " and " + "'"
							+ date2 + "'";
					if (getIntent().getStringExtra("category").equals("不限")) {
						cursor = database.query("budget", new String[] { "sum",
								"year", "date", "time", "category", "project",
								"remarks" }, s + " and " + date, null, null,
								null, "date");

					} else {
						cursor = database.query("budget", new String[] { "sum",
								"year", "date", "time", "category", "project",
								"remarks" }, "project='" + project + "'"
								+ " and " + s + " and " + date, null, null,
								null, "date");
					}
				}
			}
			while (cursor.moveToNext()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("time", cursor.getString(cursor.getColumnIndex("time")));
				map.put("sum", cursor.getString(cursor.getColumnIndex("sum")));
				map.put("year", cursor.getString(cursor.getColumnIndex("year")));
				map.put("date", cursor.getString(cursor.getColumnIndex("date")));
				map.put("project",
						cursor.getString(cursor.getColumnIndex("project")));
				map.put("category",
						cursor.getString(cursor.getColumnIndex("category")));
				map.put("remarks", cursor.getString(cursor.getColumnIndex("remarks")));
					if (cursor.getString(cursor.getColumnIndex("category"))
							.equals("收入")) {
						if (!cursor.getString(cursor.getColumnIndex("sum"))
								.isEmpty()) {
							sum1 = sum1
									+ Double.valueOf(
											cursor.getString(cursor
													.getColumnIndex("sum")))
											.doubleValue();
						}
					}
					if (cursor.getString(cursor.getColumnIndex("category"))
							.equals("支出")) {
						if (!cursor.getString(cursor.getColumnIndex("sum"))
								.isEmpty()) {
							sum2 = sum2
									+ Double.valueOf(
											cursor.getString(cursor
													.getColumnIndex("sum")))
											.doubleValue();
						}

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
			return list;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("TAG", "--------------->"+list.size());
			Log.i("TAG", "--------------->"+sum1);
			Log.i("TAG", "--------------->"+sum2);
			adapter.initData(list);
		    adapter.initData2(sum1, sum2);
			listView.setAdapter(adapter);
		}

	}

	public void initTitleView() {
		TextView textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("统计");
	}

	public void initBackView() {
		RelativeLayout back = (RelativeLayout) this.findViewById(R.id.r1_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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

}
