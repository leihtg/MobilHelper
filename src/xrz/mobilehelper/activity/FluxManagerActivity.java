package xrz.mobilehelper.activity;

import xrz.mobilehelper.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FluxManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fluxmanager);
		init();
	}

	private void init() {
		((TextView)findViewById(R.id.top_tv)).setText("��������");
	}
	
	public void flux(View view){
		switch(view.getId()){
		case R.id.activity_flux_day://��ѯ��������
			break;
		case R.id.activity_flux_mouth://��ѯ��������
			break;
		case R.id.activity_flux_search://���Ų�ѯ
			break;
		}
	}
	
	
}
