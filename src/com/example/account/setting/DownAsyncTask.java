package com.example.account.setting;

import com.example.account.main.FragmentListener;
import com.example.account.utils.AvosCloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownAsyncTask extends AsyncTask<Void, Void, Boolean> {
	private Context context;
	private ProgressDialog dialog;
	private FragmentListener listener;

	public DownAsyncTask(Context context, FragmentListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setTitle("下载");
		dialog.setMessage("玩命加载中....");
		dialog.show();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		boolean flag2 = AvosCloud.download(context);
		return flag2;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		listener.onFragmentClickListener(1);
		if (result) {
			Toast.makeText(context, "同步成功了，记账信息已回到你手机的怀抱里了！",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "抱歉同步失败了，云端没有关于您的账号的数据记录！",
					Toast.LENGTH_LONG).show();
		}

	}
}