package xrz.view.utils;

import java.util.List;
import java.util.Map;

import xrz.mobilehelper.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivaryManagerAdapter extends BaseAdapter{

	private Context c;
	private List<Map<String,Object>> list;
	public PrivaryManagerAdapter(Context c,List<Map<String,Object>> list){
		this.c=c;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		v=LayoutInflater.from(c).inflate(R.layout.item_privacymanager, null);
		ImageView im=(ImageView)v.findViewById(R.id.item_pri_im);
		ImageView suo=(ImageView)v.findViewById(R.id.item_pri_suo);
		TextView tv=(TextView)v.findViewById(R.id.item_pri_name);
		im.setImageDrawable((Drawable) list.get(position).get("icon"));
		tv.setText((CharSequence) list.get(position).get("name"));
		return v;
	}

}
