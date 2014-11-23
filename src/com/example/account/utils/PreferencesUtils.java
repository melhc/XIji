package com.example.account.utils;

import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesUtils {
	private Context context;

	public PreferencesUtils(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	// �����ݴ洢���빲�����
	public boolean saveMsg(String fileName, Map<String, Object> map) {
		boolean flag = false;
		// һ��Mode��ʹ��private,�Ƚϰ�ȫ
		
		SharedPreferences preferences = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		// Map���ṩ��һ����ΪentrySet()�ķ����������������һ��Map.Entryʵ������Ķ��󼯡�
		// ���ţ�Map.Entry���ṩ��һ��getKey()������һ��getValue()������
		// ��ˣ�����Ĵ�����Ա���֯�ø������߼�
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object object = entry.getValue();
			// ����ֵ�ò�ͬ���ͣ����
			if (object instanceof Boolean) {
				Boolean new_name = (Boolean) object;
				editor.putBoolean(key, new_name);
			} else if (object instanceof Integer) {
				Integer integer = (Integer) object;
				editor.putInt(key, integer);
			} else if (object instanceof Float) {
				Float f = (Float) object;
				editor.putFloat(key, f);
			} else if (object instanceof Long) {
				Long l = (Long) object;
				editor.putLong(key, l);
			} else if (object instanceof String) {
				String s = (String) object;
				editor.putString(key, s);
			}
			
		}
		flag = editor.commit();
		return flag;

	}

	// ��ȡ����
	public Map<String, ?> getMsg(String fileName) {
		Map<String, ?> map = null;
		// ��ȡ�����ò���edit
		SharedPreferences preferences =  context.getSharedPreferences(fileName,
				Context.MODE_APPEND);
		// Context.MODE_APPEND���Զ��Ѵ��ڵ�ֵ�����޸�
		map = preferences.getAll();
		return map;
	}

}
