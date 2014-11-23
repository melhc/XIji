package com.android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context, int version) {
		super(context, "account", null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL("create table user(id SMALLINT primary key ,username varchar(20),password varchar(32))");
		database.execSQL("create table budget(id SMALLINT primary key ,username varchar(20),sum varchar(8),year varchar(8),date varchar(20),time char(10),category char(10),project char(20),remarks varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub
//		database.execSQL("alter table budget add column year  varchar(8);");
//		database.execSQL("alter table budget add column remarks  varchar(50);");
//		Log.i("info", "----------------->数据库更新成功");
		
	}

}
