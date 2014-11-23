package com.example.account.setting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.android.dao.OverrideDatebase;
import com.avos.avoscloud.AVAnalytics;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.example.account.login.LoginActivity;
import com.example.account.main.FragmentListener;
import com.example.account.utils.PreferencesUtils;
import com.melhc.xiji.R;
import com.niceapp.lib.tagview.widget.TagActivity;
import com.weibo.sdk.AccessTokenKeeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	private List<String> list;
	private GridView gridView;
	public static final long DAY = 1000L * 60 * 60 * 24;
	private boolean flag;
	private FragmentListener listener;
	private PreferencesUtils preferencesUtils;
	private SharedPreferences shareDate;
	private MyAdapter adapter;
	private ListView listView;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				adapter.initData(list);
				listView.setAdapter(adapter);
			}
		};
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (FragmentListener) activity;
		} catch (Exception e) {

			Log.i("TAG", "------->" + e.getMessage());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting, null);

		FrameLayout fl = new FrameLayout(getActivity());
		preferencesUtils = new PreferencesUtils(getActivity());

		initView(view);
		initListDate();
		tListener listener = new tListener();
		listView.setOnItemClickListener(listener);
		listView.setCacheColorHint(0);
		fl.addView(view);
		return fl;
	}

	public void initView(View view) {
		listView = (ListView) view.findViewById(R.id.l1);
		adapter = new MyAdapter(getActivity());

	}

	class tListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
						.getMsg("login");
				if (map != null && !map.isEmpty()) {
					if (!(Boolean) map.get("remind")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("����");
						builder.setMessage("����Ҫ�ɼ�ÿ��10�����Ѽ�����");
						builder.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								flag = true;
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("remind", flag);
								preferencesUtils.saveMsg("login", map);
								Intent intent = new Intent(getActivity(),
										AlarmReceiver.class);
								PendingIntent sender = PendingIntent
										.getBroadcast(getActivity(), 0, intent,
												0);
								long firstTime = SystemClock.elapsedRealtime();
								long systemTime = System.currentTimeMillis();
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.setTimeZone(TimeZone
										.getTimeZone("GMT+8"));
								// int mHour =
								// calendar.get(Calendar.HOUR_OF_DAY);
								// int mMinute = calendar.get(Calendar.MINUTE);
								calendar.set(Calendar.MINUTE, 0);
								// calendar.set(Calendar.HOUR_OF_DAY, mHour);
								calendar.set(Calendar.HOUR_OF_DAY, 22);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								long selectTime = calendar.getTimeInMillis();
								if (systemTime > selectTime) {
									calendar.add(Calendar.DAY_OF_MONTH, 1);
									selectTime = calendar.getTimeInMillis();
								}

								long time = selectTime - systemTime;
								firstTime += time;

								AlarmManager manager = (AlarmManager) getActivity()
										.getSystemService(Context.ALARM_SERVICE);
								manager.setRepeating(
										AlarmManager.ELAPSED_REALTIME_WAKEUP,
										firstTime, DAY, sender);
								Toast.makeText(getActivity(), "�������ѳɹ�",
										Toast.LENGTH_LONG).show();
							}
						});
						builder.setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
						builder.create().show();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("����");
						builder.setMessage("��Ҫȡ������������");
						builder.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								flag = false;
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("remind", flag);
								preferencesUtils.saveMsg("login", map);
								Intent intent = new Intent(getActivity(),
										AlarmReceiver.class);
								PendingIntent sender = PendingIntent
										.getBroadcast(getActivity(), 0, intent,
												0);
								AlarmManager am = (AlarmManager) getActivity()
										.getSystemService(Context.ALARM_SERVICE);
								am.cancel(sender);
							}
						});
						builder.setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
						builder.create().show();
					}
				}
				break;

			case 1:

				shareDate = getActivity().getSharedPreferences("GUE_PWD",
						Context.MODE_PRIVATE);
				if (shareDate.getBoolean("IS_SET", false)) {
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							getActivity());
					builder2.setTitle("��ʾ");
					builder2.setMessage("�˺ű����ѿ�������Ҫȡ����");
					builder2.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent4 = new Intent(getActivity(),
									GueActivity.class);
							shareDate.edit().putBoolean("CANCEL_SET", true)
									.commit();
							Toast.makeText(getActivity(), "��������֤���",
									Toast.LENGTH_SHORT).show();
							startActivity(intent4);
						}
					});
					builder2.setNegativeButton("ȡ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
					builder2.create().show();
				} else {
					Intent intent4 = new Intent(getActivity(),
							GueActivity.class);
					startActivity(intent4);
				}
				break;
			case 2:
				FeedbackAgent agent = new FeedbackAgent(getActivity());
				agent.startDefaultThreadActivity();
				break;
			case 3:
				showPopView();
				gridItem();
				break;
			case 4:

				break;

			}
		}
	}

	public void initListDate() {
		new Thread() {
			public void run() {
				list = new ArrayList<String>();
				list.add("��������");
				list.add("�˺ű���");
				list.add("�������");
				list.add("���๦��");
				list.add("");
				handler.sendEmptyMessage(1);
			};

		}.start();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVAnalytics.onFragmentStart("setting-fragment");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVAnalytics.onFragmentEnd("setting-fragment");
	}

	public List<HashMap<String, Object>> intUiDate() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("g_image", R.drawable.upload);
		map.put("g_text", "�ϴ�����");
		list.add(map);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("g_image", R.drawable.download);
		map2.put("g_text", "��������");
		list.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("g_image", R.drawable.delete);
		map3.put("g_text", "һ�����");
		list.add(map3);
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("g_image", R.drawable.custom);
		map5.put("g_text", "�Զ����ǩ");
		list.add(map5);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("g_image", R.drawable.log);
		map4.put("g_text", "ע����¼");
		list.add(map4);

		return list;
	}

	public void showPopView() {
		View shareView = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_more, null);
		gridView = (GridView) shareView.findViewById(R.id.gridview);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), intUiDate(),
				R.layout.dialog_item, new String[] { "g_image", "g_text" },
				new int[] { R.id.imageView1, R.id.textView1 });
		gridView.setAdapter(adapter);
		PopupWindow pop = new PopupWindow(shareView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		pop.setBackgroundDrawable(new ColorDrawable(0));
		pop.setOutsideTouchable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.setFocusable(true);
		pop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
		pop.showAtLocation(shareView, Gravity.BOTTOM, 0, 0);

	}

	public void gridItem() {

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:

					if (!isLoginUser()) {
						AlertDialog.Builder builder3 = new AlertDialog.Builder(
								getActivity());

						builder3.setTitle("��ʾ");
						builder3.setMessage("�������ݽ����Ʊ������ƿռ��ϣ������ַ������κ���˽��");
						builder3.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (isNetworkConnected(getActivity())) {
									UploadAsyncTask asyncTask = new UploadAsyncTask(
											getActivity(), listener);
									asyncTask.execute();
								} else {
									Toast.makeText(getActivity(),
											"û�����磬���������ˣ�أ�",
											Toast.LENGTH_SHORT).show();
								}

							}
						});
						builder3.setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
						builder3.create().show();
					}
					break;
				case 1:

					if (!isLoginUser()) {
						AlertDialog.Builder builder4 = new AlertDialog.Builder(
								getActivity());

						builder4.setTitle("��ʾ");
						builder4.setMessage("����Ҫ���ƶ��������ص����ֻ�����");
						builder4.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (isNetworkConnected(getActivity())) {
									DownAsyncTask asyncTask = new DownAsyncTask(
											getActivity(), listener);
									asyncTask.execute();
								} else {
									Toast.makeText(getActivity(),
											"û�����磬���������ˣ�أ�",
											Toast.LENGTH_SHORT).show();
								}

							}
						});
						builder4.setNegativeButton("ȡ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
						builder4.create().show();
					}
					break;
				case 2:
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							getActivity());

					builder2.setTitle("��ʾ");
					builder2.setMessage("��ȥ�ļ������ݽ��޷���أ�����˼��");
					builder2.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							OverrideDatebase.Delete(getActivity(), 3);
							Toast.makeText(getActivity(), "��ϲ��������ȫ����գ������¿�ʼ��",
									Toast.LENGTH_SHORT).show();
							try {
								if (listener == null) {
									listener = (FragmentListener) getActivity();
									listener.onFragmentClickListener(1);
								} else {
									listener.onFragmentClickListener(1);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});
					builder2.setNegativeButton("ȡ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
					builder2.create().show();
					break;
				case 3:
					Intent intent = new Intent(getActivity(), TagActivity.class);
					startActivity(intent);
					break;
				case 4:
					@SuppressWarnings("unchecked")
					HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
							.getMsg("login");
					flag = (Boolean) map.get("login_no");
					if (flag) {
						Toast.makeText(getActivity(), "���˱��ɼǣ�����û��¼�����ע������",
								Toast.LENGTH_SHORT).show();
					}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());

					builder.setTitle("��ʾ");
					builder.setMessage("�ɼǲ����뿪�㣬���ٸ�С��һ�λ���ɣ�");
					builder.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							startActivity(intent);
							new Thread() {
								public void run() {
									try {
										HashMap<String, Object> map2 = new HashMap<String, Object>();
										map2.put("login2", false);
										map2.put("remind", false);
										preferencesUtils.saveMsg("login", map2);
										shareDate = getActivity()
												.getSharedPreferences("GUE_PWD",
														Context.MODE_PRIVATE);
										shareDate.edit().clear().commit();
										AccessTokenKeeper.clear(getActivity());	
									} catch (Exception e) {
										// TODO: handle exception
									}
									

								};

							}.start();

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
					break;
				default:
					break;
				}
			}
		});
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public boolean isLoginUser() {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) preferencesUtils
				.getMsg("login");
		try {
			flag = (Boolean) map.get("login_no");
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		if (flag) {
			LoginUserDialog();
		}

		return flag;
	}

	public void LoginUserDialog() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());

		builder3.setTitle("��ʾ");
		builder3.setMessage("ֻ��һ��ע���˺žͿ���ȥ�ƶ�ͬ������Ŷ��");
		builder3.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		builder3.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder3.create().show();

	}
}
