package com.example.account.setting;

import java.util.HashMap;
import com.android.dao.MySQLiteOpenHelper;
import com.example.account.login.LoginActivity;
import com.example.account.utils.DateModel;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RechargeActivity extends Activity {
	private TextView textView;
	private EditText e1, e2, e3;
	private Button button;
	private PreferencesUtils preferencesUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		textView = (TextView) this.findViewById(R.id.textView1);
		textView.setText("修改密码");
		initBackView();
		preferencesUtils = new PreferencesUtils(this);
		e1 = (EditText) this.findViewById(R.id.editText1);
		e1.requestFocus();
		e2 = (EditText) this.findViewById(R.id.editText2);
		e3 = (EditText) this.findViewById(R.id.editText3);
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String t1, t2, t3;
				boolean flag = false;
				t1 = e1.getText().toString();
				t2 = e2.getText().toString();
				t3 = e3.getText().toString();
				if (!t1.isEmpty() && !t2.isEmpty() && !t3.isEmpty()) {
					MySQLiteOpenHelper helper = new MySQLiteOpenHelper(
							RechargeActivity.this, DateModel.DB_VERSION);
					SQLiteDatabase database = helper.getWritableDatabase();
					Cursor cursor = database.query("user",
							new String[] { "password" }, null, null, null,
							null, null);
					while (cursor.moveToNext()) {
						if (t1.equals(cursor.getString(cursor
								.getColumnIndex("password")))) {
							flag = true;
							if (t2.equals(t3)) {
								ContentValues values = new ContentValues();
								values.put("password", t2);
								database.update("user", values, "password=?",
										new String[] { t1 });
								Toast.makeText(RechargeActivity.this,
										"恭喜您，修改密码成功！", Toast.LENGTH_SHORT)
										.show();
								Intent intent = new Intent(
										RechargeActivity.this,
										LoginActivity.class);
								startActivity(intent);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("login2", false);
								preferencesUtils.saveMsg("login", map);
							} else {
								Toast.makeText(RechargeActivity.this,
										"您两次输入的密码不一致", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
					if (!flag) {
						Toast.makeText(RechargeActivity.this, "您输入的原始密码错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(RechargeActivity.this, "您未填写相应的密码",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void initBackView() {
		RelativeLayout back = (RelativeLayout) this.findViewById(R.id.r1_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}

}
