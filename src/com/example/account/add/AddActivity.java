package com.example.account.add;

import java.util.ArrayList;

import java.util.List;

import com.avos.avoscloud.AVAnalytics;
import com.example.account.utils.PagerSlidingTabStrip;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import android.util.DisplayMetrics;

import android.util.TypedValue;

import android.view.ViewGroup;

public class AddActivity extends FragmentActivity {
	private MyPagerAdapter adapter;
	private ViewPager pager;
	private PagerSlidingTabStrip tabs;
	private FragmentManager fm;
	private DisplayMetrics dm;
	private List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_activity);
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		initFragment();
		fm = getSupportFragmentManager();
		adapter = new MyPagerAdapter(fm);
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		if (getIntent() != null) {
			if(getIntent().getStringExtra("category")!=null){
				if (getIntent().getStringExtra("category").equals("收入")) {
					pager.setCurrentItem(1,true);
					
				}
			}
			
		}
		setTabsValue();
	}

	public void initFragment() {
		list = new ArrayList<Fragment>();
		Add_income income = new Add_income();
		Add_outcome outcome = new Add_outcome();

		list.add(outcome);
		list.add(income);

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

		private final String[] titles = { "支出", "收入" };

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

			return super.instantiateItem(container, position);
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