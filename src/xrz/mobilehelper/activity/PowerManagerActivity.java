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
	/**��������ʱ���ͼƬ���*/
	private int maxWidth;
	/**��ǰ�����ı���*/
	private float scale;
	/**��ʾ�������ٵ�ͼƬ*/
	private ImageView im;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				String fo=new SimpleDateFormat("HH:mm:ss").format(time);
				((TextView)findViewById(R.id.power_tv_6)).setText("�������ʱ��:"+fo);
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
		((TextView)findViewById(R.id.top_tv)).setText("��Դ����");
		Drawable d=getResources().getDrawable(R.drawable.battery_empty);
		maxWidth=d.getIntrinsicWidth();
		im=(ImageView)findViewById(R.id.activity_power_im_2);
		Log.i("1111111111111111", "��ȣ�"+maxWidth);
		
	}
	private void powerReceiver(){
		
		re=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
					
				/**����Ƿ��ڳ��״̬*/
				int status = intent.getIntExtra("status", -1);
				switch(status){
				case BatteryManager.BATTERY_STATUS_CHARGING://���ڳ��
					((ImageView)findViewById(R.id.activity_power_im_3)).setVisibility(View.VISIBLE);
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING://���ڷŵ�
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"���ڷŵ�");
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING://û�г��
					((ImageView)findViewById(R.id.activity_power_im_3)).setVisibility(View.GONE);
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"����");
					break;
				case BatteryManager.BATTERY_STATUS_FULL://������
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"������");
					break;
				case BatteryManager.BATTERY_STATUS_UNKNOWN://δ֪״̬
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"δ֪״̬");
					break;
					default:
						break;
				}
				
				/**��ص��¶�*/
				int battayTemp=intent.getIntExtra("temperature", -1);
				float tem=battayTemp/10;
				((TextView)findViewById(R.id.power_tv_4)).setText("����¶ȣ�"+String.format("%.1f C", tem));
				/**��صĽ���״��*/
				int health=intent.getIntExtra("health", -1);
				switch(health){
				case BatteryManager.BATTERY_HEALTH_DEAD://���û�е�
					((TextView)findViewById(R.id.power_tv_2)).setText("�������״̬��"+"û�е�");
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD://���״̬����
					((TextView)findViewById(R.id.power_tv_2)).setText("�������״̬��"+"״̬����");
					break;
				case BatteryManager.BATTERY_HEALTH_UNKNOWN://δ֪����
					((TextView)findViewById(R.id.power_tv_2)).setText("�������״̬��"+"δ֪����");
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE://��ص�ѹ����
					((TextView)findViewById(R.id.power_tv_2)).setText("�������״̬��"+"��ѹ����");
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT://����¶ȹ���
					((TextView)findViewById(R.id.power_tv_2)).setText("�������״̬��"+"�¶ȹ���");
					break;
				}
				/**���ķ�ʽ*/
				int plugged=intent.getIntExtra("plugged", -1);
				switch(plugged){
				case BatteryManager.BATTERY_PLUGGED_AC://AC���
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"AC�����...");
					break;
				case BatteryManager.BATTERY_PLUGGED_USB://USB���
					((TextView)findViewById(R.id.power_tv_1)).setText("���״̬��"+"USB�����...");
					break;
				}
				
				/**��ǰ����*/
				int nowPower = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				/**�ܵ���*/
				float totalPower = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				float x=(nowPower * 100)/totalPower;
				scale=nowPower/totalPower;
				Log.i("33333333333333", nowPower+"~~~~~~"+totalPower);
				handler.sendEmptyMessage(2);
				((TextView)findViewById(R.id.activity_power_dianliang)).setText(String.valueOf(x)+"%");
	             /**��ص�ѹ*/
	            int battayV=intent.getIntExtra("voltage", -1);
	            ((TextView)findViewById(R.id.power_tv_3)).setText("��ص�ѹ:"+battayV+"mv");
	            
	            /**��صļ���*/
	            ((TextView)findViewById(R.id.power_tv_5)).setText("��ؼ�����"+intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
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
