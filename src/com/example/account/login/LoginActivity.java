package com.example.account.login;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import com.avos.avoscloud.AVException;
import com.android.dao.MySQLiteOpenHelper;
import com.android.dao.OverrideDatebase;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import com.example.account.main.MainActivity;
import com.example.account.utils.DateModel;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.testin.agent.TestinAgent;
import com.weibo.sdk.WeiboActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText e1, e2;
	private boolean isExit;
	private Tencent mTencent;
	public static QQAuth mQQAuth;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initTitleView();
		initEditView();
		RelativeLayout foot_weibo = (RelativeLayout) this
				.findViewById(R.id.r_weibo);
		foot_weibo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						WeiboActivity.class);
				startActivity(intent);
			}
		});
		RelativeLayout foot_qq = (RelativeLayout) this.findViewById(R.id.r_qq);
		foot_qq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTencent = Tencent.createInstance("1101818462",
						LoginActivity.this);
				mQQAuth = QQAuth.createInstance("1101818462",
						LoginActivity.this.getApplicationContext());
				if (!mQQAuth.isSessionValid()) {
					BaseUiListener listener = null;

					listener = new BaseUiListener() {
						@Override
						protected void doComplete(JSONObject values) {
							new SaveThread(mQQAuth.getQQToken().getOpenId())
									.start();
							AVObject gameScore = new AVObject("GameScore");
							gameScore.put("username", mQQAuth.getQQToken()
									.getOpenId());
							gameScore.saveInBackground();
							Toast.makeText(LoginActivity.this, "认证成功",
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(LoginActivity.this,
									MainActivity.class));
							Toast.makeText(LoginActivity.this, "登录成功",
									Toast.LENGTH_SHORT).show();
						}
					};
					mQQAuth.login(LoginActivity.this, "all", listener);
					mTencent.login(LoginActivity.this, "all", listener);
				} else {
					mQQAuth.logout(LoginActivity.this);
				}
			}
		});
		Button button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!e1.getText().toString().isEmpty()
						&& !e2.getText().toString().isEmpty()) {
					AVQuery<AVObject> query = new AVQuery<AVObject>(
							"UserAccount");
					query.whereEqualTo("username", e1.getText().toString());
					query.findInBackground(new FindCallback<AVObject>() {
						public void done(List<AVObject> avObjects, AVException e) {
							boolean flag = false;
							boolean flag2 = false;

							if (e == null) {
								boolean flag4 = false;

								if (avObjects.size() > 0) {
									flag4 = e2
											.getText()
											.toString()
											.equals(avObjects.get(0).get(
													"password"));
								}
								if (flag4) {
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									overridePendingTransition(R.anim.zoomin,
											R.anim.zoomout);
									Toast.makeText(LoginActivity.this, "登录成功",
											Toast.LENGTH_SHORT).show();
									new SaveThread(e1.getText().toString())
											.start();
								} else {
									SQLiteOpenHelper helper = new MySQLiteOpenHelper(
											LoginActivity.this,
											DateModel.DB_VERSION);

									SQLiteDatabase database = helper
											.getReadableDatabase();
									Cursor cursor = database.query("user",
											new String[] { "username",
													"password" }, null, null,
											null, null, null);
									while (cursor.moveToNext()) {
										try {

											flag = e1
													.getText()
													.toString()
													.equals(cursor.getString(cursor
															.getColumnIndex("username")));
											flag2 = e2
													.getText()
													.toString()
													.equals(cursor.getString(cursor
															.getColumnIndex("password")));

											if (flag && flag2) {
												Intent intent = new Intent(
														LoginActivity.this,
														MainActivity.class);
												startActivity(intent);
												overridePendingTransition(
														R.anim.zoomin,
														R.anim.zoomout);
												Toast.makeText(
														LoginActivity.this,
														"登录成功",
														Toast.LENGTH_SHORT)
														.show();
												new SaveThread(e1.getText()
														.toString()).start();
											}

										} catch (Exception e1) {
											// TODO: handle exception
										}
									}
									cursor.close();
									if (!flag || !flag2) {
										Toast.makeText(LoginActivity.this,
												"请正确输入您的帐号密码",
												Toast.LENGTH_SHORT).show();
									}
								}

							} else {
								Log.d("失败", "查询错误: " + e.getMessage());
							}
						}
					});

				} else {
					Toast.makeText(LoginActivity.this, "请正确输入您的帐号密码",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		Button button2 = (Button) findViewById(R.id.button1);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);

				startActivity(intent);
			}

		});

	}

	public void initTitleView() {
		TextView textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("奚记");

	}

	public void initEditView() {
		e1 = (EditText) this.findViewById(R.id.editText1);
		e2 = (EditText) this.findViewById(R.id.editText2);
		if (getIntent() != null) {
			e1.setText(getIntent().getStringExtra("username"));
			if (!e1.getText().toString().equals("")) {
				e2.requestFocus();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onResume(this);
		TestinAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TestinAgent.onStop(this);// 此行必须放在super.onStop后
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次将退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	private class BaseUiListener implements IUiListener {

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			showResult("code:" + e.errorCode + ", msg:" + e.errorMessage
					+ ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showResult("取消QQ账号登录");
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			doComplete((JSONObject) response);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	public void showResult(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}

	public class SaveThread extends Thread {
		String username;

		public SaveThread(String username) {
			// TODO Auto-generated constructor stub
			this.username = username;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			PreferencesUtils preferencesUtils = new PreferencesUtils(
					LoginActivity.this);
			// 登陆成功后将flag设置为ture存入共享参数中
			HashMap<String, Object> map = new HashMap<String, Object>();
			String oldName = preferencesUtils.getMsg("login").get("username")
					.toString();
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
