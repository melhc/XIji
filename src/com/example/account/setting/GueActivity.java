package com.example.account.setting;

import com.avos.avoscloud.AVAnalytics;
import com.example.account.utils.NinePointLineView;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GueActivity extends Activity {

	private boolean isExit;
	TextView showInfo;
	SharedPreferences shareDate;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			isExit = false;
			switch (msg.what) {
			case 1:
				showInfo.setText("请设置手势密码");
				break;
			case 2:
				showInfo.setText("请再次确认密码");
				break;
			case 3:
				showInfo.setText("请设置手势密码");
				break;
			case 4:
				showInfo.setText("手势密码不一致，请重新输入");
				break;
			case 5:
				showInfo.setText("密码错误，请重新输入");
				break;
			case 6:
				showInfo.setText("请输入手势密码");
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gue);
		intTitleView();
		nineView();

		getSetPwd();
		initBackView();
	}

	public void getSetPwd() {
		new Thread() {
			public void run() {
				shareDate = getSharedPreferences("GUE_PWD", 0);
				boolean isSetFirst = shareDate
						.getBoolean("IS_SET_FIRST", false);
				boolean isCancel = shareDate.getBoolean("CANCEL_SET", false);
				boolean isLogin = shareDate.getBoolean("LOGIN_SET", false);
				boolean is_second_error = shareDate.getBoolean("SECOND_ERROR",
						false);
				if (!isSetFirst && !isLogin && !isCancel) {
					mHandler.sendEmptyMessage(1);
					shareDate.edit().clear().commit();
				} else if (isSetFirst) {
					mHandler.sendEmptyMessage(2);
				} else if (!isSetFirst) {
					mHandler.sendEmptyMessage(3);
				}
				if (is_second_error) {
					mHandler.sendEmptyMessage(4);
				}
				if (isLogin || isCancel) {
					if (is_second_error) {
						mHandler.sendEmptyMessage(5);
					} else {
						mHandler.sendEmptyMessage(6);
					}
				}

			};
		}.start();

	}

	public void intTitleView() {
		TextView textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("账号保护");
	}

	public void nineView() {
		NinePointLineView nv = new NinePointLineView(GueActivity.this);
		LinearLayout nine_con = (LinearLayout) findViewById(R.id.nine_con);
		nine_con.addView(nv);
		showInfo = (TextView) findViewById(R.id.show_set_info);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (shareDate.getBoolean("LOGIN_SET", false)) {
				exit();
			} else {
				finish();
			}

			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
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

	public void initBackView() {
		RelativeLayout back = (RelativeLayout) this.findViewById(R.id.r1_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareDate.getBoolean("LOGIN_SET", false)) {
					exit();
				} else {
					finish();
				}

			}
		});
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

}
