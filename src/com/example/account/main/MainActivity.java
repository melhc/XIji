package com.example.account.main;

import java.util.ArrayList;

import java.util.List;

import com.avos.avoscloud.AVAnalytics;
import com.example.account.add.AddFragment;
import com.example.account.query.QueryFragment;
import com.example.account.setting.SettingFragment;
import com.example.account.utils.PagerSlidingTabStrip;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;

import android.util.TypedValue;

import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements FragmentListener {
	private MyPagerAdapter adapter;
	private AddFragment addFragment;
	private List<String> tagList;
	private SettingFragment settingFragment;
	private boolean isExit;

	private QueryFragment queryFragment;
	private ViewPager pager;
	private PagerSlidingTabStrip tabs;
	private FragmentManager fm;
	private DisplayMetrics dm;
	private List<Fragment> list;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		initFragment();
		fm = getSupportFragmentManager();
		tagList = new ArrayList<String>();
		adapter = new MyPagerAdapter(fm);
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		setTabsValue();
	}

	public void initFragment() {
		list = new ArrayList<Fragment>();
		addFragment = new AddFragment();
		queryFragment = new QueryFragment();
		settingFragment = new SettingFragment();
		list.add(addFragment);
		list.add(queryFragment);
		list.add(settingFragment);
	}

	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {

			super(fm);
		}

		private final String[] titles = { "记账", "统计", "设置" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		public Object instantiateItem(ViewGroup container, int position) {
			tagList.add(makeFragmentName(container.getId(),
					(int) getItemId(position)));
			return super.instantiateItem(container, position);
		}

		public void update(int item) {
			Fragment fragment = fm.findFragmentByTag(tagList.get(item));
			if (fragment != null) {
				switch (item) {
				case 0:

					break;
				case 1:
					((QueryFragment) fragment).update();
					break;
				case 2:

					break;
				default:
					break;
				}
			}
		}

	}

	public static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	public void onFragmentClickListener(int item) {
		adapter.update(item);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
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