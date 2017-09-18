package xrz.view.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * �������ʱû��
 * @author xinruzhou
 *
 */
public class MainGridViewConfigure {

	/**��Ļ�Ŀ�*/
	public static int screenWidth=0;
	/**��Ļ�ĸ�*/
	public static int screenHight=0;
	/**�ܶ�*/
	public static float screenDensity=0;
	
	public static void init(Activity context){
		if(screenWidth == 0 || screenHight == 0 || screenDensity == 0){
			DisplayMetrics dm=new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenDensity = dm.density;
			screenHight = dm.heightPixels;
			screenWidth = dm.widthPixels;
		}
	}
}
