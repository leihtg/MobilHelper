package xrz.view.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 这个类暂时没用
 * @author xinruzhou
 *
 */
public class MainGridViewConfigure {

	/**屏幕的宽*/
	public static int screenWidth=0;
	/**屏幕的高*/
	public static int screenHight=0;
	/**密度*/
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
