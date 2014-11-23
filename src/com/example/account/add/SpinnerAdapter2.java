package com.example.account.add;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter2 extends ArrayAdapter<String> {
	private Context mContext;

	private List<String> list;

	public SpinnerAdapter2(Context context, List<String> list) {
		super(context, android.R.layout.simple_spinner_item, list);
		mContext = context;
		this.list = list;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// 修改Spinner展开后的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 我们也可以加载自己的Layout布局
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
		}
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		if (!list.isEmpty()) {

			tv.setText(list.get(position));

		}
		tv.setTextSize(15);
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 修改Spinner选择后结果的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}
		// 此处text1是Spinner系统的用来显示文字的TextView
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		if (!list.isEmpty()) {
			tv.setText(list.get(position));
		}
		tv.setTextSize(15);
		return convertView;
	}

}
