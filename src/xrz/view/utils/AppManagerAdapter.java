package xrz.view.utils;

import java.util.List;
import java.util.Map;

import xrz.mobilehelper.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AppManagerAdapter extends BaseAdapter{

	private List<Map<String,Object>> list;
	private Context context;
	public AppManagerAdapter(List<Map<String,Object>> list, Context context){
		this.list=list;
		this.context=context;
	}
	public AppManagerAdapter(){}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		v=LayoutInflater.from(context).inflate(R.layout.item_appmanager, null);
		ImageView im=(ImageView)v.findViewById(R.id.item_appmanager_im);
		TextView name=(TextView)v.findViewById(R.id.item_app_appname);
		TextView verson=(TextView)v.findViewById(R.id.item_app_appbanben);
		TextView size=(TextView)v.findViewById(R.id.item_app_appsize);
		Drawable a=(Drawable) list.get(position).get("icon");
		im.setImageDrawable(a);
		name.setText((CharSequence) list.get(position).get("name"));
		verson.setText("∞Ê±æ:"+list.get(position).get("version"));
		float s=(Long) list.get(position).get("size");
		String str=null;
		if(s/1000>1024){
			str=String.format("%.2f MB", s/1000/1024);
		}else{
			str=String.format("%.2f KB", s/1000);
		}
		size.setText("¥Û–°:"+str);
		Button open=(Button)v.findViewById(R.id.item_app_dakai);
		Button xiezai=(Button)v.findViewById(R.id.item_app_xiezai);
		boolean isSys=(Boolean) list.get(position).get("isSys");
		if(isSys){
			xiezai.setEnabled(false);
			xiezai.setBackgroundResource(R.drawable.btn_disabled);
		}
		final String pack=(String) list.get(position).get("package");
		final PackageManager pm=context.getPackageManager();
		open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=pm.getLaunchIntentForPackage(pack);
				context.startActivity(intent);
			}
		});
		xiezai.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri=Uri.parse("package:"+pack);
				Intent in=new Intent(Intent.ACTION_DELETE,uri);
				context.startActivity(in);
				list.remove(position);
				notifyDataSetChanged();
			}
		});
		return v;
	}
	

}
