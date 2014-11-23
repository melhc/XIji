package com.niceapp.lib.tagview.widget;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVAnalytics;
import com.example.account.utils.MyEditText;
import com.melhc.xiji.R;
import com.niceapp.lib.tagview.widget.Tag;
import com.niceapp.lib.tagview.widget.TagListView;
import com.niceapp.lib.tagview.widget.TagListView.OnTagClickListener;
import com.niceapp.lib.tagview.widget.TagView;
import com.testin.agent.TestinAgent;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TagActivity extends Activity {

	private TagListView mTagListView;
	private final List<Tag> mTags = new ArrayList<Tag>();
	private String[] titles = { "����֧��","�������", "��ʳ��ˮ", "��������", "ѧϰ�鿯", "��������", "������Ʒ",
			"���н�ͨ", "����ͨѶ", "��ҽ��ҩ", "����ٻ�","����֧��" };
	private MyEditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_activity);
		Log.i("TAG", "--------------0---->"+titles.length);
		initDate(titles);
		
		initBackView();
		mTagListView = (TagListView) findViewById(R.id.tagview);
		setUpData();
		mTagListView.setTags(mTags);
		mTagListView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(final TagView tagView, final Tag tag) {
				// TODO Auto-generated method stub
				if(titles.length<3){
					Toast.makeText(TagActivity.this, "��ǹ��٣����������Ŷ��",
							Toast.LENGTH_LONG).show();
				}else{
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TagActivity.this);

				builder.setTitle("��ʾ");
				builder.setMessage("��Ҫɾ���ñ�ǩ��");
				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTagListView.removeTag(tag);

						updateTitles();
					}
				});
				builder.setNegativeButton("ȡ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				builder.create().show();

			}
			}
		});

	}

	private void setUpData() {
		
		for (int i = 0; i < titles.length; i++) {
			Tag tag = new Tag();
			tag.setId(i);
			tag.setChecked(true);
			tag.setTitle(titles[i]);
			mTags.add(tag);
		}
	}

	public void initBackView() {
		editText = (MyEditText) this.findViewById(R.id.tag_title);
		RelativeLayout back = (RelativeLayout) this.findViewById(R.id.r1_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		RelativeLayout add = (RelativeLayout) this.findViewById(R.id.r1_add);
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s = editText.getText().toString();
				if (!s.equals("")) {

					editText.setText("");

					if (titles.length < 11) {
						mTagListView.addTag(titles.length, s);
						updateTitles();
					} else {
						Toast.makeText(TagActivity.this, "��ǹ��࣬���������Ŷ��",
								Toast.LENGTH_LONG).show();
					}

				}else{
					Toast.makeText(TagActivity.this, "��Ҫ�ż���������д�µı�ǩ�����ݣ�",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void updateTitles() {

		List<Tag> tags = mTagListView.getTags();

		titles = new String[tags.size()];
		for (int i = 0; i < tags.size(); i++) {
			titles[i] = tags.get(i).getTitle();
		}
				savePreference(titles);


	}

	public void savePreference(String[] titles) {
		setSharedPreference("tag_list", titles);

	}

	public void initDate(String[] titles) {
		if (getSharedPreference("tag_list") != null&&getSharedPreference("tag_list").length!=1) {
			this.titles = getSharedPreference("tag_list");
		}else{
			this.titles=titles;
		}
	}

	public String[] getSharedPreference(String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = this.getSharedPreferences("data2",
				Context.MODE_APPEND);
		String values;
		values = sp.getString(key, "");
		str = values.split(regularEx);

		return str;
	}

	public void setSharedPreference(String key, String[] values) {
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = this.getSharedPreferences("data2",
				Context.MODE_PRIVATE);
		if (values != null && values.length > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			Editor et = sp.edit();
			et.putString(key, str);
			et.commit();
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
		TestinAgent.onStop(this);// ���б������super.onStop��
	}
}
