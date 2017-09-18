package xrz.view;

import xrz.mobilehelper.R;
import xrz.mobilehelper.activity.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WelcomeSurfaceView extends SurfaceView implements SurfaceHolder.Callback{


	/**左门*/
	private Drawable left;
	/**右门*/
	private Drawable right;
	/**背景*/
	private Drawable bg;
	/**屏幕的宽*/
	private int disWidth;
	/**屏幕的高*/
	private int disHight;
	private SurfaceHolder sh;
	/**移动的距离*/
	private int x;
	/**控制开门的线程*/
	public static Thread drawThread;
	private Handler handler;
	private Context c;
	
	public WelcomeSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sh=getHolder();
		sh.addCallback(this);
		handler=new Handler();
		drawThread=new drawThread();
		this.c=context;
		/**获取图片资源*/
		getDrawable(context);
		/**获取屏幕的宽高*/
		getMobilSize(context);
	}

	private void getMobilSize(Context context) {
		DisplayMetrics me=new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(me);
		disWidth=me.widthPixels;
		disHight=me.heightPixels;
	}

	private void getDrawable(Context c) {
		left=c.getResources().getDrawable(R.drawable.anim_left);
		right=c.getResources().getDrawable(R.drawable.anim_right);
		bg=c.getResources().getDrawable(R.drawable.bg);
	}
	/**画图*/
	private void draw(int x){
		Canvas canvas=sh.lockCanvas();
		bg.setBounds(0, 0, disWidth, disHight);
		bg.draw(canvas);
		left.setBounds(-x, 0, disWidth/2-x, disHight);
		left.draw(canvas);
		right.setBounds(disWidth/2+x, 0, disWidth+x, disHight);
		right.draw(canvas);
		sh.unlockCanvasAndPost(canvas);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		handler.post(drawThread);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	class drawThread extends Thread{
		@Override
		public void run(){
			handler.postDelayed(this, 100);
			x+=10;
			draw(x);
			if(x==disWidth/2){
				handler.removeCallbacks(this);
				this.interrupt();
//				WelcomeActivity wel=new WelcomeActivity();
//				wel.handler.sendEmptyMessage(0);
				Intent intent=new Intent(c,MainActivity.class);
				c.startActivity(intent);
				((Activity) c).finish();
			}
		}
	}
}
