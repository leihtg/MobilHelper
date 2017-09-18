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
		((TextView)findViewById(R.id.top_tv)).setText("流量管理");
	}
	
	public void flux(View view){
		switch(view.getId()){
		case R.id.activity_flux_day://查询当日流量
			break;
		case R.id.activity_flux_mouth://查询当月流量
			break;
		case R.id.activity_flux_search://短信查询
			break;
		}
	}
	
	
}
