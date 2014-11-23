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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryActivity3 extends Activity {

	private ListView listView;
	private SQLiteOpenHelper helper;
	private SQLiteDatabase database;
	private List<HashMap<String, String>> list;
	private MyAdapter adapter;
	private PreferencesUtils preferencesUtils;
	private HashMap<String, Object> map;
	private double sum1, sum2;
	private TextView query2_sum_text;
	private Cursor cursor;
	private String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query2);
		initTitleView();
		initBackView();
		query2_sum_text = (TextView) this.findViewById(R.id.query2_sum_text);
		listView = (ListView) this.findViewById(R.id.listView1);
		listView.setCacheColorHint(0);
		adapter = new MyAdapter(this);
		new AsyncDate().execute();
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						QueryActivity3.this);

				builder.setTitle("提示");
				builder.setMessage("您确定要删除该条记账记录吗");
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						TextView textView = (TextView) view
								.findViewById(R.id.textView1);
						database.delete("budget", "time=?",
								new String[] { textView.getText().toString(), });
						new AsyncDate().execute();
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
	}

	public class AsyncDate extends
			AsyncTask<Void, Void, List<HashMap<String, String>>> {
		@SuppressWarnings("unchecked")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			preferencesUtils = new PreferencesUtils(QueryActivity3.this);
			map = (HashMap<String, Object>) preferencesUtils.getMsg("login");
			helper = new MySQLiteOpenHelper(QueryActivity3.this,
					DateModel.DB_VERSION);
			database = helper.getReadableDatabase();
			list = new ArrayList<HashMap<String, String>>();
			if (getIntent().getStringExtra("date") != null) {
				date = getIntent().getStringExtra("date").toString()
						.substring(0, 7);

			}
		}

		@Override
		protected List<HashMap<String, String>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String s = "username='";
			s = s + map.get("username").toString() + "'";
			if (getIntent().getStringExtra("date") != null) {
				cursor = database.query("budget", new String[] { "sum", "year",
						"date", "time", "category", "project", "remarks" },
						"project='"
								+ getIntent().getStringExtra("project")
										.toString() + "'" + " and " + s
								+ " and " + "date like'" + date + "%'", null,
						null, null, null);
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
					list.add(map);
				}
			cursor.close();
			return list;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			adapter.initData(result);
			if (sum1 > sum2) {
				query2_sum_text.setText(sum1 - sum2 + " 元  ");
				query2_sum_text.setTextColor(Color.parseColor("#FF3030"));
			} else {
				query2_sum_text.setText(sum2 - sum1 + " 元  ");
				query2_sum_text.setTextColor(Color.parseColor("#43CD80"));
			}
			sum1 = sum2 = 0;
			if (result.isEmpty()) {
				Toast.makeText(QueryActivity3.this, "赶快开始记账吧！",
						Toast.LENGTH_SHORT).show();
			}
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
