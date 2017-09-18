package xrz.mobilehelper.activity;

import java.text.SimpleDateFormat;

import xrz.mobilehelper.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PowerManagerActivity extends Activity{

	private BroadcastReceiver re;
	/**电量满的时候的图片宽度*/
	private int maxWidth;
	/**当前电量的比例*/
	private float scale;
	/**显示电量多少的图片*/
	private ImageView im;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				String fo=new SimpleDateFormat("HH:mm:ss").format(time);
				((TextView)findViewById(R.id.power_tv_6)).setText("电池运行时间:"+fo);
				break;
			case 2:
				Bitmap bitmap;
				if(scale>0.1){
					bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.battery_full);
				}else{
					bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.battery_full_yellow);
				}
				Matrix m=new Matrix();
				m.setScale(scale, 1);
				Bitmap b=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				im.setImageBitmap(b);
				break;
			}
		}
	};
	private long time;
	private Runnable run=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.postDelayed(this, 1000);
			time=BootReceiver.time;
			handler.sendEmptyMessage(1);
		}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_powermanager);
		powerReceiver();
		init();
		handler.post(run);
	}
	private  void init(){
		((TextView)findViewById(R.id.top_tv)).setText("电源管理");
		Drawable d=getResources().getDrawable(R.drawable.battery_empty);
		maxWidth=d.getIntrinsicWidth();
		im=(ImageView)findViewById(R.id.activity_power_im_2);
		Log.i("1111111111111111", "宽度："+maxWidth);
		
	}
	private void powerReceiver(){
		
		re=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
					
				/**电池是否在充电状态*/
				int status = intent.getIntExtra("status", -1);
				switch(status){
				case BatteryManager.BATTERY_STATUS_CHARGING://正在充电
					((ImageView)findViewById(R.id.activity_power_im_3)).setVisibility(View.VISIBLE);
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING://正在放电
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"正在放电");
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING://没有充电
					((ImageView)findViewById(R.id.activity_power_im_3)).setVisibility(View.GONE);
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"正常");
					break;
				case BatteryManager.BATTERY_STATUS_FULL://充满电
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"充满电");
					break;
				case BatteryManager.BATTERY_STATUS_UNKNOWN://未知状态
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"未知状态");
					break;
					default:
						break;
				}
				
				/**电池的温度*/
				int battayTemp=intent.getIntExtra("temperature", -1);
				float tem=battayTemp/10;
				((TextView)findViewById(R.id.power_tv_4)).setText("电池温度："+String.format("%.1f C", tem));
				/**电池的健康状况*/
				int health=intent.getIntExtra("health", -1);
				switch(health){
				case BatteryManager.BATTERY_HEALTH_DEAD://电池没有电
					((TextView)findViewById(R.id.power_tv_2)).setText("电池运行状态："+"没有电");
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD://电池状态良好
					((TextView)findViewById(R.id.power_tv_2)).setText("电池运行状态："+"状态良好");
					break;
				case BatteryManager.BATTERY_HEALTH_UNKNOWN://未知错误
					((TextView)findViewById(R.id.power_tv_2)).setText("电池运行状态："+"未知错误");
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE://电池电压过高
					((TextView)findViewById(R.id.power_tv_2)).setText("电池运行状态："+"电压过高");
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT://电池温度过高
					((TextView)findViewById(R.id.power_tv_2)).setText("电池运行状态："+"温度过高");
					break;
				}
				/**充电的方式*/
				int plugged=intent.getIntExtra("plugged", -1);
				switch(plugged){
				case BatteryManager.BATTERY_PLUGGED_AC://AC充电
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"AC充电中...");
					break;
				case BatteryManager.BATTERY_PLUGGED_USB://USB充电
					((TextView)findViewById(R.id.power_tv_1)).setText("电池状态："+"USB充电中...");
					break;
				}
				
				/**当前电量*/
				int nowPower = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				/**总电量*/
				float totalPower = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				float x=(nowPower * 100)/totalPower;
				scale=nowPower/totalPower;
				Log.i("33333333333333", nowPower+"~~~~~~"+totalPower);
				handler.sendEmptyMessage(2);
				((TextView)findViewById(R.id.activity_power_dianliang)).setText(String.valueOf(x)+"%");
	             /**电池电压*/
	            int battayV=intent.getIntExtra("voltage", -1);
	            ((TextView)findViewById(R.id.power_tv_3)).setText("电池电压:"+battayV+"mv");
	            
	            /**电池的技术*/
	            ((TextView)findViewById(R.id.power_tv_5)).setText("电池技术："+intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
			}
		};
		IntentFilter in=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		in.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(re, in);
	}
	@Override
	public void onDestroy(){
		unregisterReceiver(re);
		super.onDestroy();
	}
}
