package com.example.account.add;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] mStringArray;
	

	public SpinnerAdapter(Context context, String[] stringArray) {
		super(context, android.R.layout.simple_spinner_item, stringArray);
		mContext = context;
		mStringArray = stringArray;

	}



	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// �޸�Spinnerչ�����������ɫ
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// ����Ҳ���Լ����Լ���Layout����
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
		}
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);

		tv.setText(mStringArray[position]);

		tv.setTextSize(15);
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// �޸�Spinnerѡ�������������ɫ
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}
		// �˴�text1��Spinnerϵͳ��������ʾ���ֵ�TextView
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(mStringArray[position]);
		tv.setTextSize(15);
		return convertView;
	}

}
