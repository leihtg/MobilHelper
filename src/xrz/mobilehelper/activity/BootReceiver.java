package xrz.mobilehelper.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	public static long time=0;
	private Handler handler=new Handler();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
			handler.removeCallbacks(run);
			time=0;
		}else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			handler.post(run);
		}
	}
	private Runnable run=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.postDelayed(this, 1000);
			time+=1000;
			Log.i("time", time+"");
		}
		
	};

}
