package com.melhc.pirechat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.android.dao.MySQLiteOpenHelper;

import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;


import android.text.Html;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;



import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 统计页面
 * 
 * @author tz
 * 
 */
public class StatisticsView extends ViewGroup implements OnClickListener {

	private Context context;

	/** 子View */
	private View view;

	private TextView mLast, mCurrent, mNext;

	/** 保存当前显示的上个月、本月和下个月的月份 几当前年份 */
	private int mLastDate, mCurrDate, mNextDate, mYear;

	private int mMaxMonth, mMaxYear, mMinMonth, mMinYear;

	private String startDate, endDate;

	private OnDateChangedLinstener mDateChangedListener;

	private PieChartView pieChart;
	private String[] colors = { "#20c8b1", "#92c9c3", "#a8efe9", "#c2e9ee",
			"#71cf97", "#d6d6b4", "#d7b97b", "#eed0b4", "#f2afa6", "#fb6571" };
	private float[] items;
	private TextView textInfo;
	private float animSpeed = 7f;
	private double total;
	private String myCategory;
	// 每块扇形代表的类型
	private String[] type;
	private double sum2;
	private PreferencesUtils preferencesUtils;
	private HashMap<String, Object> map;
	@SuppressLint("HandlerLeak")
	private Handler myhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
			   intitPieChart();
			}

		};
	};

	public StatisticsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StatisticsView(Context context, float[] items, double total2,
			String[] type) {
		super(context);
		this.context = context;
		this.items = items;
		this.total = total2;
		this.type = type;
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {

		view = LayoutInflater.from(context).inflate(R.layout.statistics_layout,
				null);
		mLast = (TextView) view.findViewById(R.id.last);
		mCurrent = (TextView) view.findViewById(R.id.curr);
		mNext = (TextView) view.findViewById(R.id.next);
		mLast.setOnClickListener(this);
		mCurrent.setOnClickListener(this);
		mNext.setOnClickListener(this);
		preferencesUtils = new PreferencesUtils(context);
		map = (HashMap<String, Object>) preferencesUtils.getMsg("login");
		myCategory = map.get("category").toString();
		intitPieChart();
		this.addView(view);
		initDate();
	}

	private void intitPieChart() {

		textInfo = (TextView) view.findViewById(R.id.text_item_info);
		pieChart = (PieChartView) view.findViewById(R.id.parbar_view);
		pieChart.setAnimEnabled(true);// 是否开启动画
		pieChart.setItemsColors(colors);// 设置各个块的颜色
		pieChart.setItemsSizes(items);// 设置各个块
		pieChart.setCategory(myCategory);
		if (items.length == 0) {
			
			textInfo.setText("这个月还没有"+myCategory+"数据哦");
		} else {
			pieChart.setVisibility(View.VISIBLE);
		}
		pieChart.setRotateSpeed(animSpeed);// 设置旋转速度
		pieChart.setTotal(100);
		pieChart.setActualTotal(total);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		pieChart.setRaduis((int) (dm.widthPixels / 2.3));// 设置饼状图半径
		pieChart.setOnItemSelectedListener(new OnPieChartItemSelectedLinstener() {
			public void onPieChartItemSelected(PieChartView view, int position,
					String colorRgb, float size, float rate,
					boolean isFreePart, float rotateTime) {

				try {
					if (isFreePart) {
						textInfo.setText("冗余数据");
					} else {
						textInfo.setTextColor(Color.parseColor(pieChart
								.getShowItemColor()));
						float percent = (float) (Math.round(size * 100)) / 100;
						textInfo.setText(Html.fromHtml(type[position]
								+ " 所占比例 " + percent + "%<br>"
								+ "<font color='black'>"
								+ (int) (total * size / 100) + "元</font>"));
					}
					if (total > 0) {
						textInfo.setVisibility(View.VISIBLE);
					}
					Animation myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
					myAnimation_Alpha.setDuration((int) (3 * rotateTime));
					textInfo.startAnimation(myAnimation_Alpha);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onTriggerClicked() {
				int key;
				if (myCategory.equals("支出")) {
					key=1;
				}else{
					key=2;
				}
				
				switch (key) {
				case 1:
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("category", "收入");
					preferencesUtils.saveMsg("login", map1);
					myCategory = "收入";
					new QueryTask(getContext(),mYear + "-" + mCurrDate, myCategory).execute();
					break;
				case 2:
					HashMap<String, Object> map11 = new HashMap<String, Object>();
					map11.put("category", "支出");
					preferencesUtils.saveMsg("login", map11);
					myCategory = "支出";
					new QueryTask(getContext(),mYear + "-" + mCurrDate, myCategory).execute();
				default:
					break;
				}
				
			}

		});
		pieChart.setShowItem(0, true, true);// 设置显示的块

	}

	/**
	 * 初始化日期
	 */
	private void initDate() {
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMaxYear = mYear + 1;
		mMinMonth = mMaxMonth = mCurrDate = c.get(Calendar.MONTH) + 1;
		mLastDate = mCurrDate - 1;
		mNextDate = mCurrDate + 1;
		if(mNextDate==13){
			mNextDate=1;
		}
		mMinYear = mMaxYear - 2;
		freshDate();
	}

	/**
	 * 设置当前日期
	 * 
	 * @param year
	 * @param month
	 */
	public void setCurrDate(int year, int month) {
		mYear = year;
		mMaxYear = year + 1;
		mMinMonth = mMaxMonth = mCurrDate = month;
		mNextDate = mCurrDate + 1;
		if(mNextDate==13){
			mNextDate=1;
		}
		mLastDate = mCurrDate - 1;
		mMinYear = mMaxYear - 2;
		freshDate();
	}

	/**
	 * 设置日期范围
	 * 
	 * @param mMaxMonth
	 * @param mMaxYear
	 * @param mMinMonth
	 * @param mMinYear
	 */
	public void setDateRange(int mMaxMonth, int mMaxYear, int mMinMonth,
			int mMinYear) {
		this.mMaxMonth = mMaxMonth;
		this.mMaxYear = mMaxYear;
		this.mMinMonth = mMinMonth;
		this.mMinYear = mMinYear;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child = getChildAt(0);
		child.layout(l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			int widthSpec = 0;
			int heightSpec = 0;
			LayoutParams params = v.getLayoutParams();
			if (params.width > 0) {
				widthSpec = MeasureSpec.makeMeasureSpec(params.width,
						MeasureSpec.EXACTLY);
			} else if (params.width == -1) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.EXACTLY);
			} else if (params.width == -2) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.AT_MOST);
			}

			if (params.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(params.height,
						MeasureSpec.EXACTLY);
			} else if (params.height == -1) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.EXACTLY);
			} else if (params.height == -2) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.AT_MOST);
			}
			v.measure(widthSpec, heightSpec);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.last:
			if (mDateChangedListener != null) {
				if (mMinYear >= mYear && mLastDate < mMinMonth) {
					Toast.makeText(context, "只能查询一年内的数据哦!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (mLastDate == 1) {
					mLastDate = 12;
					mCurrDate--;
					mNextDate--;
				} else if (mLastDate == 12) {
					mLastDate--;
					mCurrDate = 12;
					mNextDate--;
					mYear--;
				} else if (mLastDate == 11) {
					mLastDate--;
					mCurrDate--;
					mNextDate = 12;
				} else {
					mLastDate--;
					mCurrDate--;
					mNextDate--;
				}

				freshDate();
				@SuppressWarnings("deprecation")
				Date date = new Date(mYear - 1900, mCurrDate - 1, 1);

				String startDate = DateFormat.format("yyyy-MM-dd", date)
						.toString() + "1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-"
						+ "1 00:00:00";
				new QueryThread(startDate.substring(0, 7), myCategory).start();
				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;

		case R.id.next:
			if (mDateChangedListener != null) {

				if (mMaxYear == mYear && mNextDate > mMaxMonth) {
					Toast.makeText(context, "还没有这个月的数据哦!", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (mNextDate == 12) {
					mLastDate++;
					mCurrDate++;
					mNextDate = 1;
				} else if (mNextDate == 1) {
					mLastDate++;
					mCurrDate = 1;
					mNextDate++;
					mYear++;
				} else if (mNextDate == 2) {
					mLastDate = 1;
					mCurrDate++;
					mNextDate++;
				} else {
					mLastDate++;
					mCurrDate++;
					mNextDate++;
				}
				freshDate();
				@SuppressWarnings("deprecation")
				Date date = new Date(mYear - 1900, mCurrDate - 1, 1);
				String startDate = DateFormat.format("yyyy-MM-dd", date)
						.toString() + "1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-1 00:00:00";
				new QueryThread(startDate.substring(0, 7), myCategory).start();
				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;
		default:
			break;
		}
	}

	public void freshDate() {
		mLast.setText(mLastDate + "月");
		mCurrent.setText(mYear + "年" + mCurrDate + "月");
		mNext.setText(mNextDate + "月");
	}

	public float[] getItems() {
		return items;
	}

	public void setItems(float[] items) {
		this.items = items;
		pieChart.setItemsSizes(items);
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
		if (total <= 0) {
			pieChart.setVisibility(View.GONE);
			textInfo.setVisibility(View.GONE);
			pieChart.setTotal(0);
		} else {
			pieChart.setVisibility(View.VISIBLE);
			textInfo.setVisibility(View.VISIBLE);
			pieChart.setTotal(100);
			pieChart.setActualTotal(total);
		}
	}

	public void freshView() {
		pieChart.setShowItem(0, true, true);// 设置显示的块
		pieChart.invalidate();
		this.invalidate();
	}

	public void relaseTotal() {
		pieChart.relaseTotal(0);
	}

	public OnDateChangedLinstener getDateChangedListener() {
		return mDateChangedListener;
	}

	public void setDateChangedListener(
			OnDateChangedLinstener mDateChangedListener) {
		this.mDateChangedListener = mDateChangedListener;
	}

	public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public class QueryTask extends AsyncTask<Void, Void, Boolean> {
		private Context context;
		private ProgressDialog dialog;
		private String querydate;
		private String category;

		public QueryTask(Context context, String querydate, String category) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.querydate = querydate;
			this.category = category;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setTitle("切换数据");
			dialog.setMessage("玩命加载中....");
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext(), 3);
			SQLiteDatabase database = helper.getReadableDatabase();
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String s3 = "category='" + category + "'";
			PreferencesUtils preferencesUtils = new PreferencesUtils(
					getContext());
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map2 = (HashMap<String, Object>) preferencesUtils
					.getMsg("login");
			String s = "username='";
			s = s + map2.get("username").toString() + "'";
			Cursor cursor = database.query("budget", new String[] { "sum",
					"year", "date", "time", "category", "project" },
					"date like'" + querydate + "%'" + " and " + s + " and "
							+ s3, null, null, null, "category");
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
			total = sum2;
			sum2 = 0;
			items = new float[list.size()];
			type = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				items[i] = (float) ((float) Math.round((float) (Double.valueOf(
						list.get(i).get("sum").toString()).intValue() * 100)) / total);
				type[i] = list.get(i).get("project").toString();
			}
			if(total==0){
				return true;
			}else{
				return false;
			}
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			intitPieChart();
			if(!result){
				Toast.makeText(context,
						"数据切换完毕",
						Toast.LENGTH_LONG).show();	
			}
		

		}
	}
	public  class  QueryThread extends Thread{
		private String querydate;
		private String category;
		public QueryThread(String querydate,String category) {
			// TODO Auto-generated constructor stub
			this.querydate = querydate;
			this.category = category;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getContext(), 3);
			SQLiteDatabase database = helper.getReadableDatabase();
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String s3 = "category='" + category + "'";
			PreferencesUtils preferencesUtils = new PreferencesUtils(
					getContext());
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map2 = (HashMap<String, Object>) preferencesUtils
					.getMsg("login");
			String s = "username='";
			s = s + map2.get("username").toString() + "'";
			Cursor cursor = database.query("budget", new String[] { "sum",
					"year", "date", "time", "category", "project" },
					"date like'" + querydate + "%'" + " and " + s + " and "
							+ s3, null, null, null, "category");
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
			total = sum2;
			sum2 = 0;
			items = new float[list.size()];
			type = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				items[i] = (float) ((float) Math.round((float) (Double.valueOf(
						list.get(i).get("sum").toString()).intValue() * 100)) / total);
				type[i] = list.get(i).get("project").toString();
			}
			myhandler.sendEmptyMessage(1);
		}
	}

	public void update(String querydate) {
		new QueryThread(querydate, myCategory).start();
	}

}
