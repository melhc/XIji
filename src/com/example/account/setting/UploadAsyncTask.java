package com.example.account.setting;

import com.example.account.main.FragmentListener;
import com.example.account.utils.AvosCloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UploadAsyncTask extends AsyncTask<Void, Void, Boolean> {
	private Context context;
	private ProgressDialog dialog;
	private FragmentListener listener;

	public UploadAsyncTask(Context context, FragmentListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setTitle("同步");
		dialog.setMessage("玩命加载中....");
		dialog.show();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		AvosCloud.upload(context);
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		listener.onFragmentClickListener(1);
		Toast.makeText(context,
				"恭喜您同步成功，您的数据已妥善同步到云端上",
				Toast.LENGTH_LONG).show();

	}
}