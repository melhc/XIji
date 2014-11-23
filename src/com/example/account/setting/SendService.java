package com.example.account.setting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.android.dao.MySQLiteOpenHelper;
import com.example.account.login.EntranceActivity;
import com.example.account.utils.DateModel;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class SendService extends Service {
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String NoMessage = null;
			if (msg.what == 1) {
				NoMessage = "您今天还未记账噢！工作再忙，事情再多也要记账噢！";
				NotificationMessage(NoMessage);
				stopSelf();
			}
			if (msg.what == 2) {
				NoMessage = "您今天支出" + msg.arg2 + "元" + "收入" + msg.arg1 + "元！";
				NotificationMessage(NoMessage);
				stopSelf();
			}

		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getBaseContext(),
					DateModel.DB_VERSION);
			SQLiteDatabase database = helper.getReadableDatabase();
			PreferencesUtils preferencesUtils = new PreferencesUtils(
					getBaseContext());
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
					.getMsg("login");
			String s = "username='";
			s = s + map.get("username").toString() + "'";
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			@SuppressWarnings("deprecation")
			Date date3 = new Date(year - 1900, month - 1, day);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date2 = format.format(date3);
			String date = "date='" + date2 + "'";
			Cursor cursor = database.query("budget", new String[] { "username",
					"year", "date", "sum", "time", "category", "project" }, s
					+ " and " + date, null, null, null, null);
			double i1 = 0, i2 = 0;
			while (cursor.moveToNext()) {

				if (cursor.getString(cursor.getColumnIndex("category"))
						.equals("收入")) {

					i1 = i1
							+ Double.valueOf(
									cursor.getString(cursor.getColumnIndex("sum")))
									.doubleValue();

				}
				if (cursor.getString(cursor.getColumnIndex("category"))
						.equals("支出")) {

					i2 = i2
							+ Double.valueOf(
									cursor.getString(cursor.getColumnIndex("sum")))
									.doubleValue();
				}

			}
			if (i1 == 0 && i2 == 0) {
				handler.sendEmptyMessage(1);
			} else {
				Message message = Message.obtain();
				message.what = 2;
				message.arg1 = (int) i1;
				message.arg2 = (int) i2;
				handler.sendMessage(message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);

	}

	public void NotificationMessage(String NoMessage) {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getBaseContext());
		builder.setSmallIcon(R.drawable.logo1);
		builder.setContentText(NoMessage);
		builder.setContentTitle("奚记");
		builder.setDefaults(Notification.DEFAULT_ALL);
		Intent intent2 = new Intent(getBaseContext(), EntranceActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				getBaseContext(), 1, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setAutoCancel(true);
		manager.notify((int) System.currentTimeMillis(), builder.build());

	}

}
