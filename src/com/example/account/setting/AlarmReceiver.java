package com.example.account.setting;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "----------->接收到了");
		Intent intent3 = new Intent(context, SendService.class);
		context.startService(intent3);
		
	}

}
