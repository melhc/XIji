package com.example.account.query;

import java.util.HashMap;
import java.util.List;

import com.melhc.xiji.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.TextView;

public class QueryAdapter extends BaseAdapter {
	private Context context;

	private List<HashMap<String, String>> list;
	private double[] sum;

	public QueryAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	public List<HashMap<String, String>> initData(
			List<HashMap<String, String>> list) {

		this.list = list;
		return this.list;

	}

	public double[] initData2(double sum1, double sum2) {
		sum = new double[2];
		this.sum[0] = sum1;
		this.sum[1] = sum2;
		return this.sum;

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.query_item2,
					null);
		} else {
			view = convertView;
		}
		TextView textView2 = (TextView) view.findViewById(R.id.query_project);
		TextView textView3 = (TextView) view.findViewById(R.id.textView3);
		TextView textView4 = (TextView) view.findViewById(R.id.textView4);
		textView2.setText((String) list.get(position).get("project"));
		TextView query_sum = (TextView) view.findViewById(R.id.query_sum);
		query_sum.setText(Double.valueOf(
				list.get(position).get("sum").toString()).doubleValue()
				+ "");
		ProgressBar seekBar = (ProgressBar) view.findViewById(R.id.seekBar1);

		if (list.get(position).get("category").equals("收入")) {
			seekBar.setMax((int) sum[0]);
			textView3.setText("占总收入");
			if (!list.get(position).get("sum").toString().isEmpty()) {
				textView4.setText(""
						+ (int) ((float) Math.round((float) (Double.valueOf(
								list.get(position).get("sum").toString())
								.intValue() * 100)) / sum[0]));
			}
			textView4.setTextColor(Color.parseColor("#FF3030"));
		}
		if (list.get(position).get("category").equals("支出")) {
			seekBar.setMax((int) sum[1]);
			if (!list.get(position).get("sum").toString().isEmpty()) {
				textView4.setText(""
						+ (int) ((float) Math.round((float) (Double.valueOf(
								list.get(position).get("sum").toString())
								.intValue() * 100)) / sum[1]));
			}
			textView3.setText("占总支出");
			textView4.setTextColor(Color.parseColor("#43CD80"));
		}

		seekBar.setProgress(Double.valueOf(
				list.get(position).get("sum").toString()).intValue());

		RelativeLayout relativeLayout = (RelativeLayout) view
				.findViewById(R.id.query_Id);
		relativeLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, QueryActivity3.class);
				intent.putExtra("project",
						(String) list.get(position).get("project"));
				intent.putExtra("date", (String) list.get(position).get("date"));
				context.startActivity(intent);
			}
		});
		return view;
	}
}
