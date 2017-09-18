package xrz.mobilehelper.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xrz.mobilehelper.R;
import xrz.view.utils.AppManagerAdapter;
import android.app.TabActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class AppManager extends TabActivity{

	private TabHost tabHost;
	private RadioGroup rg=null;
	/**非系统应用*/
	private List<Map<String,Object>> userApp=null;
	/**系统应用*/
	private List<Map<String,Object>> sysApp=null;
	/**全部应用*/
	private List<Map<String,Object>> allApp=null;
	/**显示数据的ListView*/
	private ListView lv=null;
	/**屏幕上方的文字*/
	private TextView topText=null;
	private AppManagerAdapter adapter;
	private Thread getAppThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		((LinearLayout)findViewById(R.id.activity_app_linearLayout)).setVisibility(View.VISIBLE);
		init();
		getAppThread=new getAppThread();
		getAppThread.start();
	}
	/**初始化*/
	private void init(){
		tabHost=getTabHost();
		tabHost.addTab(tabHost.newTabSpec("all").setIndicator("全部应用").setContent(R.id.activity_app_listview));
		tabHost.addTab(tabHost.newTabSpec("user").setIndicator("非系统应用").setContent(R.id.activity_app_listview));
		tabHost.addTab(tabHost.newTabSpec("sys").setIndicator("系统应用").setContent(R.id.activity_app_listview));
		//显示可用内存大小
		viewMemfree();
		userApp=new ArrayList<Map<String,Object>>();
		sysApp=new ArrayList<Map<String,Object>>();
		allApp=new ArrayList<Map<String,Object>>();
		lv=(ListView)findViewById(R.id.activity_app_listview);
		topText=(TextView) findViewById(R.id.top_tv);
		topText.setText("应用管理");
		tabHost.setCurrentTabByTag("user");
		tabHost.setCurrentTabByTag("all");
		rg=(RadioGroup)findViewById(R.id.app_rg);
		rg.setOnCheckedChangeListener(rgListener);
		adapter=new AppManagerAdapter(allApp, AppManager.this);
		lv.setAdapter(adapter);
	}
	/**显示可用内存*/
	private void viewMemfree() {
//		StatFs mStatFs=new StatFs(Environment.getExternalStorageDirectory().getPath());
		StatFs mStatFs=new StatFs(Environment.getDataDirectory().getPath());
		int blockSize=mStatFs.getBlockSize();
		int availBlock=mStatFs.getAvailableBlocks();
		float freeMem=blockSize*availBlock;
		String free=null;
//		free=Environment.getDataDirectory().getPath();
//		free=String.format("%.2f GB", freeMem/1000/1024/1024);
		if(freeMem/1000/1024>1024){
			free=String.format("%.2f GB", freeMem/1000/1024/1024);
		}else{
			free=String.format("%.2f MB", freeMem/1000/1024);
		}
		((TextView)findViewById(R.id.include_top_tv_freem)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.include_top_tv_freem)).setText("可用內存："+free);
	}
	/**获取全部的已安装应用信息*/
	private void getApps(){
		PackageManager mPackageManager=getPackageManager();
		List<PackageInfo> apps=mPackageManager.getInstalledPackages(0);
		for(PackageInfo mPackage:apps){
			int flag=mPackage.applicationInfo.flags;
			if(((flag & ApplicationInfo.FLAG_SYSTEM) == 0) &&(flag & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0){
				//非系统应用
				Map<String,Object> userMap = null;
				setMap(userMap, mPackage, mPackageManager,userApp,false);
			}else{
				//系统应用
				Map<String,Object> sysMap = null;
				setMap(sysMap, mPackage, mPackageManager, sysApp,true);
			}
		}
	}
	private void setMap(Map<String,Object> map,PackageInfo mPackage,PackageManager mPackageManager,List<Map<String,Object>> list,boolean isSys){
		map=new HashMap<String, Object>();
		map.put("icon", mPackage.applicationInfo.loadIcon(mPackageManager));
		map.put("name", mPackage.applicationInfo.loadLabel(mPackageManager));
		map.put("size", new File(mPackage.applicationInfo.publicSourceDir).length());
		map.put("version", mPackage.versionName);
		map.put("package", mPackage.packageName);
		Log.i("package", mPackage.packageName);
		map.put("isSys", isSys);
		list.add(map);
		allApp.add(map);
	}
	private RadioGroup.OnCheckedChangeListener rgListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.app_rb_all://全部应用
				setAdapter(allApp);
				break;
			case R.id.app_rb_sys://系统应用
				setAdapter(sysApp);
				break;
			case R.id.app_rb_user://非系系统应用
				setAdapter(userApp);
				break;
			}
		}
	}; 
	private void setAdapter(List<Map<String,Object>> list){
		((LinearLayout)findViewById(R.id.activity_app_linearLayout)).setVisibility(View.VISIBLE);
		adapter=new AppManagerAdapter(list, AppManager.this);
		lv.setAdapter(adapter);
		((LinearLayout)findViewById(R.id.activity_app_linearLayout)).setVisibility(View.GONE);
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				adapter.notifyDataSetChanged();
				((LinearLayout)findViewById(R.id.activity_app_linearLayout)).setVisibility(View.GONE);
				break;
			}
		}
	};
	class getAppThread extends Thread{
		@Override
		public void run(){
			getApps();
			handler.sendEmptyMessage(0);
		}
	}
	
}
