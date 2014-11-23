package com.example.account.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

public class AvosCloud {
	private static String s;
	private static AVQuery<AVObject> query;
	private static SQLiteDatabase database;

	public static void upload(Context context) {
		PreferencesUtils preferencesUtils = new PreferencesUtils(context);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context,
				DateModel.DB_VERSION);
		database = helper.getWritableDatabase();
		if (map != null && !map.isEmpty()) {
			s = map.get("username").toString();
		}
		query = new AVQuery<AVObject>("UserDate");
		query.whereEqualTo("username", s);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Log.i("成功", "---------------------->查询到" + avObjects.size()
							+ " 条符合条件的数据");
					new Thread() {
						public void run() {
							try {
								query.deleteAll();
								Cursor cursor = database.query("budget", null,
										null, null, null, null, null);
								while (cursor.moveToNext()) {

									AVObject gameScore = new AVObject(
											"UserDate");
									gameScore.put("username", s);
									gameScore.put("sum", cursor
											.getString(cursor
													.getColumnIndex("sum")));
									gameScore.put("date", cursor
											.getString(cursor
													.getColumnIndex("date")));
									gameScore.put("time", cursor
											.getString(cursor
													.getColumnIndex("time")));
									gameScore.put(
											"category",
											cursor.getString(cursor
													.getColumnIndex("category")));
									gameScore.put("project", cursor
											.getString(cursor
													.getColumnIndex("project")));
									gameScore.put("remarks", cursor
											.getString(cursor
													.getColumnIndex("remarks")));
									gameScore.saveInBackground();
								}
							} catch (AVException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						};
					}.start();

				} else {
					Log.d("失败", "查询错误: " + e.getMessage());
				}
			}

		});

	}

	public static boolean download(Context context) {
		PreferencesUtils preferencesUtils = new PreferencesUtils(context);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context,
				DateModel.DB_VERSION);
		database = helper.getWritableDatabase();
		
		if (map != null && !map.isEmpty()) {
			s = map.get("username").toString();
		}
		query = new AVQuery<AVObject>("UserDate");
		query.whereEqualTo("username", s);
		try {
			List<AVObject> avObjects = query.find();
			if (avObjects.size() == 0)
				return false;
			else {
				database.delete("budget", null, null);
				for (Iterator<AVObject> iterator = avObjects.iterator(); iterator
						.hasNext();) {
					AVObject avObject = (AVObject) iterator.next();
					ContentValues values = new ContentValues();
					values.put("sum", (String) avObject.get("sum"));
					values.put("username", s);
					values.put("date", (String) avObject.get("date"));
					values.put("time", (String) avObject.get("time"));
					values.put("remarks", (String) avObject.get("remarks"));
					values.put("category", (String) avObject.get("category"));
					values.put("project", (String) avObject.get("project"));
					database.insert("budget", null, values);
				}
				return true;
			}
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;

	}
}
