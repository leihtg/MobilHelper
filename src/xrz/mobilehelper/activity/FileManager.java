package xrz.mobilehelper.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xrz.mobilehelper.R;
import xrz.view.utils.MIMEType;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FileManager extends Activity {

	private ListView lv;
	/** 是否在根目录下 */
	private boolean isRoot = true;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> list;
	/** SD卡的根目录 */
	public static final String SDCARD_ROOT_PATH = Environment
			.getExternalStorageDirectory().getPath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemanager);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.top_tv)).setText("文件管理");
		lv = (ListView) findViewById(R.id.file_lv);
		list = new ArrayList<Map<String, Object>>();
		setListViewAdapter(SDCARD_ROOT_PATH);
		String[] from = { "img", "text" };
		int[] to = { R.id.item_file_im, R.id.item_file_tv };
		adapter = new SimpleAdapter(this, list, R.layout.item_filemanager,
				from, to);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(lvLis);
	}

	private AdapterView.OnItemClickListener lvLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			File file = new File((String) list.get(arg2).get("path"));
			if (file.isDirectory()) {
				setListViewAdapter(file.getPath());
				adapter.notifyDataSetChanged();
			}else{
				openFile(file);
			}
		}
	};

	private void setListViewAdapter(String filepath) {
		isRoot = true;
		list.removeAll(list);
		File mfile = new File(filepath);
		File[] files = mfile.listFiles();
		if (files == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.lastdir);
			map.put("text", "返回上级目录");
			map.put("isback", "true");
			map.put("path", mfile.getParent());
			list.add(map);
			return;
		}
		if (files.length != 0) {
			for (File file : files) {
				if (isRoot) {
					if ((file.getParent() != null)
							&& (!SDCARD_ROOT_PATH.equals(file.getParent()))) {
						setMap(R.drawable.lastdir, "返回上级目录", new File(file.getParent()).getParent(), true);
						isRoot = false;
					} else {
						isRoot = true;
					}
				}

				if (file.isDirectory()) {
					setMap(R.drawable.folder, file.getName(), file.getPath(), false);
				} else if(file.getPath().equals(".mp3")){
					setMap(R.drawable.back_to_player_pressed, file.getName(), file.getPath(), false);
				}else if(file.getPath().endsWith(".jpg") || file.getPath().endsWith(".png") || file.getPath().endsWith(".bmp")){
					setMap(R.drawable.image, file.getName(), file.getPath(), false);
				}else if(file.getPath().endsWith(".rmvb") || file.getPath().endsWith(".3gp") || file.getPath().endsWith(".mp4s")){
					setMap(R.drawable.movie, file.getName(), file.getPath(), false);
				}else{
					setMap(R.drawable.ic_launcher, file.getName(), file.getPath(), false);
				}
			}
		} else {
			setMap(R.drawable.lastdir, "返回上级目录", mfile.getParent(), true);
		}
	}

	private void setMap(int img,String text,String path,boolean isback){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", img);
		map.put("text", text);
		map.put("isback", isback+"");
		map.put("path", path);
		list.add(map);
	}
	protected void openFile(File file) {
		Intent intent=new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type=getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);
	}
	/**
	 * 根据文件后缀获取MIME类型
	 * @param file
	 * @return
	 */
	private String getMIMEType(File file) {
		String type="*/*";
		String fName=file.getName();
		int dotIndex=fName.indexOf(".");
		if(dotIndex<0){
			return type;
		}
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end == ""){
			return type;
		}
		for(int i=0;i<MIMEType.MIME.length;i++){
			if(end.equals(MIMEType.MIME[i][0])){
				type=MIMEType.MIME[i][1];
			}
			return type;
		}
		return null;
	}
}
