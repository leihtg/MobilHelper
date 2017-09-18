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
	/**�϶�ʱҪ��ʾ��ͼ��*/
	private ImageView dragImageView = null;
	/**Ҫ���ĵ��б���*/
	private ViewGroup dragItemView=null;
	/**�϶���λ��*/
	private int dragPosition;
	/**�ɿ���λ��*/
	private int dropPosition;
	/**��ʼ��λ��*/
	private int startPosition;
	/**���Ƶ�λ��*/
	private int holdPosition;
	private boolean isMoving=false;//��ǰ�϶�״̬
	private WindowManager windowManager = null;//���ڹ����������ڸ�����ͼ
	private WindowManager.LayoutParams windowParams = null;//�����ڴ�����Ϊ��ͼ���ò��ֲ���
	/**��¼�϶������������*/
	private int mLastX,mLastY;
	/**��¼ǰǰҪ��ʾ�Ķ���Ч��*/
	private String lastAnimId;
	
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//�˷������ó�����������OnInterceptTouchEvent�б�����
	public boolean setOnItemLongClickListener(final MotionEvent ev){
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int position, long lpos) {
				// TODO Auto-generated method stub
				int x=mLastX=(int) ev.getX();
				int y=mLastY=(int) ev.getY();
				startPosition=dragPosition=holdPosition=position;//��¼����
				ViewGroup itemView = (ViewGroup) getChildAt(dragPosition
						- getFirstVisiblePosition());//��ȡItem
				Log.i("getFirstVisiblePosition", getFirstVisiblePosition()+"");
				dragItemView = itemView;
				itemView.destroyDrawingCache();//�ͷ���Դͼ��Դ
				itemView.setDrawingCacheEnabled(true);//���û�ͼ����
				itemView.setDrawingCacheBackgroundColor(0x000000);//���ñ���ɫ
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache(true));//����һ�������е�bitmap
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
	//��ʼ�϶�
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
	//�����϶�
	private void onDrag(int x,int y){
		if (dragImageView != null) {
			windowParams.alpha = 0.8f;
			windowParams.x = (x-mLastX)+dragItemView.getLeft()+8;
			windowParams.y = (y-mLastY)+dragItemView.getTop()+130;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
	}
	//�ɿ�
	private void onDrop(int x,int y){
//		Adapter adapter=(Adapter) getAdapter();
		MainGridViewAdapter adapter=(MainGridViewAdapter) getAdapter();
		adapter.notifyDataSetChanged();
	}
	//�϶�
	private void onMove(int x,int y){
		int pos=pointToPosition(x,y);//��GridView�ϵ����ص�ת��Ϊ����Ӧ��Ԫ��λ��
		if(pos!=AdapterView.INVALID_POSITION&&dropPosition!=dragPosition){
			dropPosition=pos;
		
		int moveNum = dropPosition - dragPosition;
		if(moveNum!=0){
			int count=Math.abs(moveNum);//��ȡ�ƶ��ľ���
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
	/**�����ͼ�꣬�������������ͼ���Ͱ�֮ǰ��ͼ��ɾ����*/
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
	//����
	public Animation getDropAnimation(){
		ScaleAnimation scale = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f);
		scale.setDuration(550);//����������ʱ��
		scale.setFillAfter(true);//�����������У�һֻ������
		return scale;
	}
}
