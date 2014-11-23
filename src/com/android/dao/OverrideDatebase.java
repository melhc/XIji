package com.android.dao;


import java.util.HashMap;

import com.example.account.utils.PreferencesUtils;

import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class OverrideDatebase {
	@SuppressLint("SimpleDateFormat")
	public static void Delete(Context context, int db_id) {
		SQLiteOpenHelper helper = new MySQLiteOpenHelper(context, db_id);
		SQLiteDatabase database = helper.getWritableDatabase();
		PreferencesUtils preferencesUtils = new PreferencesUtils(context);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		String s = "username='";
		s = s + map.get("username").toString() + "'";
		Cursor cursor = database.query("budget", new String[] { "sum", "year",
				"date", "time", "category", "project" }, s, null, null, null,
				null);
		while (cursor.moveToNext()) {
			database.delete("budget", null, null);
			
		}
	}
	public static void Override(Context context, int db_id,String oldName,String newName ) {
		if(oldName.equals("admin")){

			SQLiteOpenHelper helper = new MySQLiteOpenHelper(context, db_id);
			SQLiteDatabase database = helper.getWritableDatabase();	
			String s = "username='"+oldName+"'";
			Cursor cursor = database.query("budget", new String[] { "sum", "year",
					"date", "time", "category", "project" }, s, null, null, null,
					null);
			while (cursor.moveToNext()) {
				ContentValues values = new ContentValues();
				String time = cursor.getString(cursor.getColumnIndex("time"));
				values.put("username", newName);
				database.update("budget", values, "time=?", new String[] { time });
			}
	
		}

	}
}
