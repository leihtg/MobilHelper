package xrz.mobilehelper.activity;

import java.util.ArrayList;
import java.util.List;

import xrz.mobilehelper.R;
import xrz.view.MyView;
import xrz.view.utils.MainGridViewAdapter;
import xrz.view.utils.MainGridViewConfigure;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity{

	private GridView gridView;
	
	private SharedPreferences share;
	private List<Integer> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getOrder();
		init();
	}
	private void getOrder(){
		list=new ArrayList<Integer>();
		share=getPreferences(0);
		for(int i=0;i<6;i++){
			if(share.getInt(i+"", -1) == -1){
				list.add(i);
			}else{
				list.add(share.getInt(i+"", -1));
			}
		}
	}
	@Override
	public void onStop(){
		super.onStop();
		share=getPreferences(0);
		SharedPreferences.Editor edit=share.edit();
		for(int i=0;i<6;i++){
			edit.putInt(i+"", list.get(i));
		}
		edit.commit();
	}
	private void init(){
		/**��ʼ������ȡ��Ļ�Ŀ��*/
		MainGridViewConfigure.init(this);
//		gridView = (MainGridView) findViewById(R.id.gridview);
		gridView =(MyView)findViewById(R.id.gridview);
		MainGridViewAdapter adapter = new MainGridViewAdapter(MainActivity.this,list );
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new GridViewListener());
	}
	class GridViewListener implements GridView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int i=list.get(arg2);
			switch(i){
			case 0://ͨѶ����
				Toast.makeText(MainActivity.this, "ͨѶ����", 3).show();
				startActivity(new Intent(MainActivity.this,CommunicationActivity.class));
				break;
			case 1://Ӧ�ù���
				Toast.makeText(MainActivity.this, "Ӧ�ù���", 3).show();
				startActivity(new Intent(MainActivity.this,AppManager.class));
				break;
			case 2://��˽����
				Toast.makeText(MainActivity.this, "��˽����", 3).show();
				startActivity(new Intent(MainActivity.this,PrivaryManagerActivity.class));
				break;
			case 3://��Դ����
				Toast.makeText(MainActivity.this, "��Դ����", 3).show();
				startActivity(new Intent(MainActivity.this,FileManager.class));
				break;
			case 4://��Դ����
				Toast.makeText(MainActivity.this, "��Դ����", 3).show();
				startActivity(new Intent(MainActivity.this,PowerManagerActivity.class));
				break;
			case 5://��������
				Toast.makeText(MainActivity.this, "��������", 3).show();
				startActivity(new Intent(MainActivity.this,FluxManagerActivity.class));
				break;
			}
		}}
}
