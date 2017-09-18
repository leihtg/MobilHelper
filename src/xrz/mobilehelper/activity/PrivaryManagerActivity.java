package xrz.mobilehelper.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xrz.mobilehelper.R;
import xrz.view.utils.PrivaryManagerAdapter;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 隐私管理
 * */
public class PrivaryManagerActivity extends Activity {

	private List<Map<String,Object>> apps;
	private PrivaryManagerAdapter adapter;
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacymanager);
		init();
		getApps();
	}
	private void init(){
		((TextView)findViewById(R.id.top_tv)).setText("隐私管理");
		((ProgressBar)findViewById(R.id.activity_pri_pro)).setVisibility(View.VISIBLE);
		lv=(ListView)findViewById(R.id.activity_privacy_lv);
		apps=new ArrayList<Map<String,Object>>();
		adapter=new PrivaryManagerAdapter(this, apps);
		lv.setAdapter(adapter);
	}
	/**获取全部应用*/
	private void getApps(){
		new Thread(){
			@Override
			public void run(){
				getAllApps();
				handler.sendEmptyMessage(0);
			}

			private void getAllApps() {
				PackageManager pm=getPackageManager();
				List<PackageInfo> list=pm.getInstalledPackages(0);
				for(PackageInfo pi:list){
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("icon", pi.applicationInfo.loadIcon(pm));
					map.put("name", pi.applicationInfo.loadLabel(pm));
					map.put("package", pi.packageName);
					apps.add(map);
				}
			}
		}.start();
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				((ProgressBar)findViewById(R.id.activity_pri_pro)).setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};
}
