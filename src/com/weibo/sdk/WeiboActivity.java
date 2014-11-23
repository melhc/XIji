package com.weibo.sdk;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.widget.Toast;

import com.android.dao.OverrideDatebase;
import com.avos.avoscloud.AVObject;
import com.example.account.login.LoginActivity;
import com.example.account.main.MainActivity;
import com.example.account.utils.PreferencesUtils;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class WeiboActivity extends Activity {

	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "2624798385";
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";
	SsoHandler mSsoHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
		mSsoHandler = new SsoHandler(WeiboActivity.this, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener());

	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");

			WeiboActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (WeiboActivity.accessToken.isSessionValid()) {
				AccessTokenKeeper.keepAccessToken(WeiboActivity.this,
						accessToken);
				new SaveThread( uid).start();
				AVObject gameScore = new AVObject("GameScore");
				gameScore.put("username", uid);
				gameScore.saveInBackground();
				Toast.makeText(WeiboActivity.this, "认证成功", Toast.LENGTH_SHORT)
						.show();
				startActivity(new Intent(WeiboActivity.this, MainActivity.class));
				Toast.makeText(WeiboActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"抱歉，微博认证失败！", Toast.LENGTH_LONG).show();
			finish();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "成功取消第三方登陆",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"抱歉，微博认证失败！" + e.getMessage(), Toast.LENGTH_LONG)
					.show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);

		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent =new Intent(this,LoginActivity.class);
			startActivity(intent);
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	public class SaveThread extends Thread {
		String username;
		public SaveThread(String username) {
			// TODO Auto-generated constructor stub
			this.username=username;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			PreferencesUtils preferencesUtils = new PreferencesUtils(
					WeiboActivity.this);
			// 登陆成功后将flag设置为ture存入共享参数中
			HashMap<String, Object> map = new HashMap<String, Object>();
			String oldName =preferencesUtils.getMsg("login").get("username").toString();
			String newName = username;
			map.put("login2", true);
			map.put("username", newName);
			map.put("remind", false);
			map.put("refresh", true);
			map.put("login_no", false);
			map.put("category", "支出");
			preferencesUtils.saveMsg("login", map);
			OverrideDatebase.Override(getBaseContext(), 3, oldName, newName);
			
		}
	}

}
