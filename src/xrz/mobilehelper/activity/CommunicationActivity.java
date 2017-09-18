package xrz.mobilehelper.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import xrz.mobilehelper.R;
import xrz.view.ProgressDialogUtils;
import xrz.view.utils.SmsDate;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

public class CommunicationActivity extends Activity{

	private FileOutputStream fos=null;
	private List<SmsDate> smsList=null;
	private ProgressDialog dialog;
	private int nums=0;//短信的条数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communication);
	}
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				dialog=ProgressDialogUtils.createProgressDialog(CommunicationActivity.this, "正在还原短信");
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setMax(smsList.size());
				dialog.show();
				break;
			case 1:
				Toast.makeText(CommunicationActivity.this, "短信恢复成功", 3).show();
				dialog.dismiss();
				break;
			case 3:
				dialog.incrementProgressBy(1);
				break;
			case 4:
				dialog=ProgressDialogUtils.createProgressDialog(CommunicationActivity.this, "正在备份短信");
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setMax(nums);
				dialog.show();
				break;
			case 6:
				dialog.dismiss();
				Toast.makeText(CommunicationActivity.this, "备份成功", 3).show();
				break;
			case 7:
				Toast.makeText(CommunicationActivity.this, "备份失败", 3).show();
				break;
			}
		}
	};
	public void btnLis(View v){
		switch(v.getId()){
		case R.id.com_beifen://备份短信
			init();
			backupSms();
			break;
		case R.id.com_huanyuan://还原短信
			redMsg();
			break;
		}
	}
	private void init(){
		smsList=new ArrayList<SmsDate>();
		File file=new File("mnt/sdcard/mobilhelp/");
		if(!file.exists()){
			file.mkdirs();
		}
		File f=new File("mnt/sdcard/mobilhelp/sms_back_up.xml");
		try {
			fos=new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**备份短信*/
	private void backupSms(){
		new Thread(){
			@Override
			public void run(){
				ContentResolver res=getContentResolver();
				Cursor cursor=res.query(Uri.parse("content://sms/inbox"), null, null, null, null);
				nums=cursor.getCount();
				handler.sendEmptyMessage(4);
				XmlSerializer xml=Xml.newSerializer();
				try {
					xml.setOutput(fos, "UTF-8");
					xml.startDocument(null, Boolean.valueOf(true));
					xml.startTag(null, "SMSS");
				while(cursor.moveToNext()){
					xml.startTag(null, "sms");
					xml.attribute(null, "id", cursor.getString(cursor.getColumnIndex("_id")));
					xml.startTag(null, "address");
					xml.text(cursor.getString(cursor.getColumnIndex("address")));
					xml.endTag(null, "address");
					xml.startTag(null, "body");
					xml.text(cursor.getString(cursor.getColumnIndex("body")));
					xml.endTag(null, "body");
					xml.startTag(null, "date");
					xml.text(cursor.getString(cursor.getColumnIndex("date")));
					xml.endTag(null, "date");
					xml.endTag(null, "sms");
					SystemClock.sleep(200);
					handler.sendEmptyMessage(3);
				}
				xml.endTag(null, "SMSS");
				xml.endDocument();
				xml.flush();
				fos.close();
				handler.sendEmptyMessage(6);
				} catch (Exception e) {
					handler.sendEmptyMessage(7);
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	/**还原项目*/
	private void redMsg(){
		
		new Thread(){
			@Override
			public void run(){
				handler.sendEmptyMessage(0);
				smsList=jxXml();
				hySms(smsList);
				handler.sendEmptyMessage(1);
			}
		}.start();
	}
	protected void hySms(List<SmsDate> smsList2) {
		Uri uri=Uri.parse("content://sms");
		getContentResolver().delete(uri, null, null);
		for(SmsDate d:smsList2){
			ContentValues value=new ContentValues();
			value.put("address", d.getAddress());
			value.put("date", d.getDate());
			value.put("body", d.getBody());
			getContentResolver().insert(uri, value);
			SystemClock.sleep(200);
			handler.sendEmptyMessage(3);
		}
	}
	/**解析XML文件*/
	private List<SmsDate> jxXml() {
		List<SmsDate> list=new ArrayList<SmsDate>();
		XmlPullParser parser=Xml.newPullParser();
		try {
			SmsDate sm=null;
			FileInputStream is=new FileInputStream(new File("mnt/sdcard/mobilhelp/sms_back_up.xml"));
			parser.setInput(is, "UTF-8");
			int event=parser.getEventType();
			while(event != XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if("sms".equals(parser.getName())){
						sm=new SmsDate();
						int id=Integer.parseInt(parser.getAttributeValue(0));
						sm.setId(id);
					}
					if("address".equals(parser.getName())){
						sm.setAddress(parser.nextText());
					}
					if("body".equals(parser.getName())){
						sm.setBody(parser.nextText());
					}
					if("date".equals(parser.getName())){
						sm.setDate(parser.nextText());
					}
					if("SMSS".equals(parser.getName())){
						break;
					}
					break;
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						list.add(sm);
						sm=null;
					}
					break;
					
				}
				event=parser.next();
			}
		} catch(Exception e) {
			Log.i("111111111111111111", e.toString());
			e.printStackTrace();
		}
		return list;
	}
}
