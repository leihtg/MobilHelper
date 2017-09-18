package xrz.view.utils;

import java.util.List;

import xrz.mobilehelper.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MainGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<Integer> lstDate;
	private int lsPic [];
	private String lsText [];
	private TextView txtAge;
	private int holdPosition;
	private boolean isChanged = false;
	private boolean ShowItem = false;

	public MainGridViewAdapter(Context mContext, List<Integer> lstDate) {
		this.context = mContext;
		this.lstDate = lstDate;
		this.lsPic=new int[]{
				R.drawable.list_0_pressed,
				R.drawable.list_1_pressed,
				R.drawable.list_2_pressed,
				R.drawable.list_3_pressed,
				R.drawable.list_4_pressed,
				R.drawable.list_5_pressed
		};
		this.lsText=new String [] {
				"通讯管理",
				"应用管理",
				"隐私管理",
				"资源管理",
				"电源管理",
				"流量管理"
		};
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void exchange(int startPosition, int endPosition) {
		holdPosition = endPosition;
		Object startObject = getItem(startPosition);
		if(startPosition < endPosition){
		    lstDate.add(endPosition + 1, (Integer) startObject);
		    lstDate.remove(startPosition);
		}
		else{
			lstDate.add(endPosition,(Integer)startObject);
			lstDate.remove(startPosition + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}
	
	public void showDropItem(boolean showItem){
		this.ShowItem = showItem;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = LayoutInflater.from(context).inflate(R.layout.mygridview_item, null);
		int i=lstDate.get(position).intValue();
		txtAge = (TextView) v.findViewById(R.id.txt_userAge);
		txtAge.setText(lsText[i]);
		ImageView im=(ImageView)v.findViewById(R.id.imageView_ItemImage);
		im.setImageResource(lsPic[i]);
		return v;
	}

}
