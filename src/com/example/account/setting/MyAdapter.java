package com.example.account.setting;

import java.util.List;

import com.melhc.xiji.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Context context;

	private List<String> list;

	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	public List<String> initData(List<String> list) {

		this.list = list;
		return this.list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			view = LayoutInflater.from(context)
					.inflate(R.layout.setting_item, null);
		} else {
			view = convertView;
		}
		TextView textView = (TextView) view.findViewById(R.id.t1);
		textView.setText(list.get(position).toString());
		
	
		return view;
	}

}
