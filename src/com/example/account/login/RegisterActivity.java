package com.example.account.login;

import com.android.dao.MySQLiteOpenHelper;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.example.account.utils.DateModel;
import com.melhc.xiji.R;
import com.testin.agent.TestinAgent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText e1, e2, e3;
	private SQLiteOpenHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		TextView textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("奚记");
		initBackView();
		helper = new MySQLiteOpenHelper(this, DateModel.DB_VERSION);
		e1 = (EditText) this.findViewById(R.id.editText1);
		e2 = (EditText) this.findViewById(R.id.editText2);
		e3 = (EditText) this.findViewById(R.id.editText3);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!e1.getText().toString().isEmpty()
						&& !e2.getText().toString().isEmpty()
						&& !e3.getText().toString().isEmpty()) {

					String rgex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
					boolean flag = e1.getText().toString().matches(rgex);
					boolean flag2 = e2.getText().toString()
							.equals(e3.getText().toString());
					// flag=true;
					if (flag || flag2) {
						if (!flag) {
							Toast.makeText(RegisterActivity.this,
									"您输入的帐号邮箱有问题，请重新输入", Toast.LENGTH_SHORT)
									.show();
						}

						if (!flag2) {
							Toast.makeText(RegisterActivity.this,
									"您两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT)
									.show();
						}
					}
					if (!flag && !flag2) {

						Toast.makeText(RegisterActivity.this,
								"您填写的信息有误，请核对后重新填写", Toast.LENGTH_SHORT).show();
					}
					if (flag && flag2) {
						SQLiteDatabase database = helper.getWritableDatabase();
						ContentValues contentValues = new ContentValues();
						contentValues.put("username", e1.getText().toString());
						contentValues.put("password", e2.getText().toString());
						long id = database.insert("user", null, contentValues);
						AVObject gameScore = new AVObject("UserAccount");
						gameScore.put("username", e1.getText()
								.toString());
						gameScore.put("password", e2.getText()
								.toString());
						gameScore.saveInBackground();
						if (id != -1) {
							Intent intent = new Intent(RegisterActivity.this,
									LoginActivity.class);
							intent.putExtra("username", e1.getText().toString());
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin,
									R.anim.zoomout);
							Toast.makeText(RegisterActivity.this, "注册成功",
									Toast.LENGTH_SHORT).show();
						}

					}

				} else {
					Toast.makeText(RegisterActivity.this, "您未填写指定的注册信息，请正确填写",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

	}

	public void initBackView() {
		ImageView back = (ImageView) this.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
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
		 TestinAgent.onStop(this);//此行必须放在super.onStop后
	}
}
