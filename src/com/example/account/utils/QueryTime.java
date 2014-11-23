package com.example.account.utils;

import java.util.Calendar;


/*
 * 设置查询月份
 */
public class QueryTime {
	public static String setQueryTime(String queryTime) {
		int year = Integer.valueOf(queryTime.substring(0, 4)).intValue();
		int month = Integer.valueOf(queryTime.substring(7, queryTime.length()-3)).intValue();
		String q = year + "-" + changeMonth(month);
		
		return q;

	}

	public static String setQueryTime() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        String q = year + "-" + changeMonth(month);
        
		return q;

	}

	public static String changeMonth(int i) {
		if (i >= 10) {
			return String.valueOf(i).toString();
		} else {
			String s = "0" + String.valueOf(i).toString();
			return s;
		}

	}
}
