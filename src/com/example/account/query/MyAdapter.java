package com.example.account.query;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import com.melhc.xiji.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, String>> list;

	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	public List<HashMap<String, String>> initData(
			List<HashMap<String, String>> list) {

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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;

		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.query_item,
					null);
		} else {
			view = convertView;
		}

		TextView textView = (TextView) view.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		TextView textView3 = (TextView) view.findViewById(R.id.textView3);
		TextView textView4 = (TextView) view.findViewById(R.id.textView5);

		TextView textView6 = (TextView) view.findViewById(R.id.remarks);
		textView2.setText(list.get(position).get("project").trim());
		textView.setText(list.get(position).get("time"));
		textView4.setText(list.get(position).get("date"));
		textView6.setText(list.get(position).get("remarks"));
		double i=Double.valueOf(list.get(position).get("sum")).doubleValue();
		DecimalFormat df = new DecimalFormat("#####0.0");
		textView3.setText( df.format(i)+ "元");
		if (list.get(position).get("category").equals("收入")) {
			textView3.setTextColor(Color.parseColor("#FF3030"));
		}
		if (list.get(position).get("category").equals("支出")) {
			textView3.setTextColor(Color.parseColor("#43CD80"));
		}

		return view;
	}

}
