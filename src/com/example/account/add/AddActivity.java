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
				if (getIntent().getStringExtra("category").equals("����")) {
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
		// ����Tab���Զ��������Ļ��
		tabs.setShouldExpand(true);
		// ����Tab�ķָ�����͸����
		tabs.setDividerColor(Color.TRANSPARENT);
		// ����Tab�ײ��ߵĸ߶�
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// ����Tab Indicator�ĸ߶�
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// ����Tab�������ֵĴ�С
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// ����Tab Indicator����ɫ
		tabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// ����ѡ��Tab���ֵ���ɫ (�������Զ����һ������)
		tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// ȡ�����Tabʱ�ı���ɫ
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {

			super(fm);
		}

		private final String[] titles = { "֧��", "����" };

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
		TestinAgent.onStop(this);// ���б������super.onStop��
	}

}