package com.example.account.login;

import java.util.HashMap;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.example.account.main.MainActivity;
import com.example.account.setting.GueActivity;
import com.example.account.utils.PreferencesUtils;
import com.testin.agent.TestinAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class EntranceActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		statics();

		PreferencesUtils preferencesUtils = new PreferencesUtils(this);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		boolean flag = false;
		if (map != null && !map.isEmpty()) {
			if ((Boolean) map.get("refresh") != null) {
				flag = (Boolean) map.get("refresh");
			}
			if (!flag) {

				map.put("refresh", true);
				preferencesUtils.saveMsg("login", map);
			
			}
           if((Boolean) (map.get("login2"))){
			SharedPreferences shareDate = getSharedPreferences("GUE_PWD",
					Context.MODE_PRIVATE);
			shareDate.edit().putBoolean("IS_SET_FIRST", false).commit();
			if (shareDate.getBoolean("IS_SET", false)) {
				Intent intent4 = new Intent(EntranceActivity.this,
						GueActivity.class);
				shareDate.edit().putBoolean("LOGIN_SET", true).commit();

				startActivity(intent4);
			} else {
				Intent intent = new Intent(EntranceActivity.this,
						MainActivity.class);
				startActivity(intent);

			}
           }else{
        	   Intent intent = new Intent(EntranceActivity.this,
						LoginActivity.class);
				startActivity(intent);
           }
		} else {
			Intent intent = new Intent(EntranceActivity.this,
					MainActivity.class);

			startActivity(intent);
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("login2", true);
			map1.put("login_no", true);
			map1.put("username", "admin");
			map1.put("remind", false);
			map1.put("category", "Ö§³ö");
			map1.put("refresh", true);
			preferencesUtils.saveMsg("login", map1);
		}
	}

	public void statics() {
		try {
			AVOSCloud.initialize(EntranceActivity.this,
					"z19hqayje9zd89zkptx4ppxnp3me5c0si0cx4ps2usanykrx",
					"fygh9fhvj0u3ky1cgdsu03h7t4ekv4zlxtwnar63i8ht5rl8");
			AVAnalytics.enableCrashReport(EntranceActivity.this, true);
			// AVInstallation.getCurrentInstallation().saveInBackground();
			TestinAgent.init(this, "11140fd1cfb3ff8f544e11bf4394e24d");
			new Thread() {
				public void run() {

					FeedbackAgent agent = new FeedbackAgent(
							EntranceActivity.this);
					agent.sync();

				};

			}.start();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
