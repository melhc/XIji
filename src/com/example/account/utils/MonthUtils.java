package com.example.account.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MonthUtils {
	public static String changeMonth(int i) {
		String s = null;
		switch (i) {
		case 0:
			s = "Jan";
			break;

		case 1:
			s = "Feb";
			break;
		case 2:
			s = "Mar";
			break;
		case 3:
			s = "Apr";
			break;
		case 4:
			s = "May";
			break;
		case 5:
			s = "Jun";
			break;
		case 6:
			s = "July";
			break;
		case 7:
			s = "Aug";
			break;
		case 8:
			s = "Sep";
			break;
		case 9:
			s = "Oct";
			break;
		case 10:
			s = "Nov";
			break;
		case 11:
			s = "Dec";
			break;
		}
		return s;

	}

	public static List<String> spinnerMonth(int i) {
		List<String> list = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		switch (i) {
		case 0:
			for (int j = 9; j <= 12; j++) {
				list.add(year - 1 + " 年 " + j + " 月 ");
			}
			for (i = 1; i <= 4; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;

		case 1:
			for (int j = 10; j <= 12; j++) {
				list.add(year - 1 + " 年 " + j + " 月 ");
			}
			list.add(year + " 年 " + 1 + " 月 ");
			for (i = 2; i <= 5; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 2:
			for (int j = 11; j <= 12; j++) {
				list.add(year - 1 + " 年 " + j + " 月 ");
			}
			list.add(year + " 年 " + 1 + " 月 ");
			list.add(year + " 年 " + 2 + " 月 ");
			for (i = 3; i <= 6; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 3:
			list.add(year - 1 + " 年 " + 12 + " 月 ");
			for (i = 1; i <= 7; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 4:
			for (i = 1; i <= 8; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 5:
			for (i = 2; i <= 9; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 6:
			for (i = 3; i <= 10; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 7:
			for (i = 4; i <= 11; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			
			return list;
		case 8:
			for (i = 5; i <= 12; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			return list;
		case 9:
			for (i = 6; i <= 12; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			list.add(year + 1 + " 年 " + 1 + " 月 ");
			return list;
		case 10:
			for (i = 7; i <= 12; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			list.add(year + 1 + " 年 " + 1 + " 月 ");
			list.add(year + 1 + " 年 " + 2 + " 月 ");
			return list;
		case 11:
			for (i = 8; i <= 12; i++) {
				list.add(year + " 年 " + i + " 月 ");
			}
			list.add(year + 1 + " 年 " + 1 + " 月 ");
			list.add(year + 1 + " 年 " + 2 + " 月 ");
			list.add(year + 1 + " 年 " + 3 + " 月 ");
			return list;
		}

		return list;
	}

}
