package xrz.view;

import xrz.view.utils.MainGridViewAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class MyView extends GridView {
	/**拖动时要显示的图标*/
	private ImageView dragImageView = null;
	/**要更改的列表项*/
	private ViewGroup dragItemView=null;
	/**拖动的位置*/
	private int dragPosition;
	/**松开的位置*/
	private int dropPosition;
	/**开始的位置*/
	private int startPosition;
	/**控制的位置*/
	private int holdPosition;
	private boolean isMoving=false;//当前拖动状态
	private WindowManager windowManager = null;//窗口管理器，用于附加视图
	private WindowManager.LayoutParams windowParams = null;//用于在代码中为视图设置布局参数
	/**记录拖动点的最新坐标*/
	private int mLastX,mLastY;
	/**记录前前要显示的动画效果*/
	private String lastAnimId;
	
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//此方法设置长按操作，在OnInterceptTouchEvent中被调用
	public boolean setOnItemLongClickListener(final MotionEvent ev){
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int position, long lpos) {
				// TODO Auto-generated method stub
				int x=mLastX=(int) ev.getX();
				int y=mLastY=(int) ev.getY();
				startPosition=dragPosition=holdPosition=position;//记录坐标
				ViewGroup itemView = (ViewGroup) getChildAt(dragPosition
						- getFirstVisiblePosition());//获取Item
				Log.i("getFirstVisiblePosition", getFirstVisiblePosition()+"");
				dragItemView = itemView;
				itemView.destroyDrawingCache();//释放资源图资源
				itemView.setDrawingCacheEnabled(true);//启用绘图缓存
				itemView.setDrawingCacheBackgroundColor(0x000000);//设置背景色
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache(true));//返回一个缓存中的bitmap
				Bitmap bitmap = Bitmap.createBitmap(bm, 8, 8, bm.getWidth()-8, bm.getHeight()-8);
				startDrag(bitmap, x, y);
				itemView.setVisibility(View.INVISIBLE);				
				isMoving = false;
				return false;
			}
		});		
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		if(dragImageView!=null&&dragPosition!=AdapterView.INVALID_POSITION){
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				onDrag(x, y);
				if(!isMoving )
				    onMove(x,y);			
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();
				onDrop(x, y);
				break;
			}
		}
		return super.onTouchEvent(ev);
	}
	//开始拖动
	private void startDrag(Bitmap bm,int x,int y){
		stopDrag();
		windowParams=new WindowManager.LayoutParams();
		windowParams.gravity=Gravity.TOP|Gravity.LEFT;
		windowParams.x=dragItemView.getLeft()+8;
		windowParams.y=dragItemView.getTop()+130;
		windowParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width=WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.alpha=0.8f;
		
		ImageView iv=new ImageView(getContext());
		iv.setImageBitmap(bm);
		windowManager=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(iv,windowParams);
		
		dragImageView=iv;
	}
	//结束拖动
	private void onDrag(int x,int y){
		if (dragImageView != null) {
			windowParams.alpha = 0.8f;
			windowParams.x = (x-mLastX)+dragItemView.getLeft()+8;
			windowParams.y = (y-mLastY)+dragItemView.getTop()+130;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}
	//松开
	private void onDrop(int x,int y){
//		Adapter adapter=(Adapter) getAdapter();
		MainGridViewAdapter adapter=(MainGridViewAdapter) getAdapter();
		adapter.notifyDataSetChanged();
	}
	//拖动
	private void onMove(int x,int y){
		int pos=pointToPosition(x,y);//将GridView上的象素点转化为所对应的元素位置
		if(pos!=AdapterView.INVALID_POSITION&&dropPosition!=dragPosition){
			dropPosition=pos;
		
		int moveNum = dropPosition - dragPosition;
		if(moveNum!=0){
			int count=Math.abs(moveNum);//获取移动的距离
			if(moveNum>0){
				holdPosition=dragPosition+count;
			}
			else{
				holdPosition=dragPosition-count;
			}
			
		}
		ViewGroup item=(ViewGroup)getChildAt(holdPosition);
		Animation anim=getDropAnimation();
		item.startAnimation(anim);
		dragPosition=holdPosition;
		if(dragPosition==dropPosition){
			lastAnimId=anim.toString();
		}
//		final Adapter adapter=(Adapter) getAdapter();
		final MainGridViewAdapter adapter=(MainGridViewAdapter) getAdapter();
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				isMoving=true;
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				if(animation.toString().equalsIgnoreCase(lastAnimId)){
					adapter.exchange(startPosition, dropPosition);
					startPosition=dropPosition;
					isMoving=false;
				}
			}
		});
	}}
	/**长点击图标，如果生成了缩略图，就把之前的图标删除掉*/
	private void stopDrag(){
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			return setOnItemLongClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}
	//动画
	public Animation getDropAnimation(){
		ScaleAnimation scale = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f);
		scale.setDuration(550);//动画持续的时间
		scale.setFillAfter(true);//动画持续进行，一只到结束
		return scale;
	}
}
