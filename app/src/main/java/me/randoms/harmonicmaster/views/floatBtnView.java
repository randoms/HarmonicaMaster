package me.randoms.harmonicmaster.views;

import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.utils.DisplayUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class floatBtnView extends View{
	
	private int topZindex;
	private int bottomZindex;
	private int leftZindex;
	private int rightZindex;
	
	private int fabColor;
	private Paint mBottomShadowPaint;
	private Paint mTopShadowPaint;
	
	private int shadowRadus = 40;
	
	private Drawable icon;
	
	private float[][] shadowParams ={
			{24,0,1,1,12,0,1,1.5f},
			{23,0,3,3,16,0,3,3},
			{23,0,6,3,19,0,10,10},
			{22,0,10,5,25,0,14,14},
			{22,0,15,6,30,0,19,19},
	}; 

	public floatBtnView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.floatBtnView,
	            0, 0);
        try {
        	topZindex = a.getInt(R.styleable.floatBtnView_fab_top_zindex, 0);
        	bottomZindex = a.getInt(R.styleable.floatBtnView_fab_bottom_zindex, 0);
        	leftZindex = a.getInt(R.styleable.floatBtnView_fab_left_zindex, 0);
        	rightZindex = a.getInt(R.styleable.floatBtnView_fab_right_zindex, 0);
        	
        	icon = a.getDrawable(R.styleable.floatBtnView_fab_icon);
        	fabColor = a.getColor(R.styleable.floatBtnView_fab_color,Color.WHITE);
        } finally {
        	a.recycle();
        }
		init();
	}
	
	private void init(){
		
		mTopShadowPaint = new Paint();
		mBottomShadowPaint = new Paint();
		// 上下形式的 fab
		if(topZindex != 0 && bottomZindex !=0){
			if(topZindex>bottomZindex){
				
				mTopShadowPaint.setAntiAlias(true);
				int color = (int)(255*shadowParams[0][0]/100);
				mTopShadowPaint.setShadowLayer(shadowParams[0][3],
						shadowParams[0][1], shadowParams[0][2], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mTopShadowPaint);
				mTopShadowPaint.setColor(fabColor);
				
				int zindex = topZindex - bottomZindex;
				color = (int)(255*shadowParams[zindex][4]/100);
				
				mBottomShadowPaint.setAntiAlias(true);
				mBottomShadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mBottomShadowPaint);
				mBottomShadowPaint.setColor(fabColor);
			}else{
				mBottomShadowPaint.setAntiAlias(true);
				int color = (int)(255*shadowParams[0][0]/100);
				mBottomShadowPaint.setShadowLayer(shadowParams[0][3],
						shadowParams[0][1], shadowParams[0][2], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mBottomShadowPaint);
				mBottomShadowPaint.setColor(fabColor);
				
				int zindex = bottomZindex - topZindex;
				color = (int)(255*shadowParams[zindex][4]/100);
				
				mTopShadowPaint = new Paint();
				mTopShadowPaint.setAntiAlias(true);
				mTopShadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mTopShadowPaint);
				mTopShadowPaint.setColor(fabColor);
			}
		}
		
		if(leftZindex != 0 && rightZindex != 0){
			if(leftZindex > rightZindex){
				mTopShadowPaint.setAntiAlias(true);
				int color = (int)(255*shadowParams[0][0]/100);
				mTopShadowPaint.setShadowLayer(shadowParams[0][3],
						shadowParams[0][1], shadowParams[0][2], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mTopShadowPaint);
				mTopShadowPaint.setColor(fabColor);
				
				int zindex = leftZindex - rightZindex;
				color = (int)(255*shadowParams[zindex][4]/100);
				
				mBottomShadowPaint.setAntiAlias(true);
				mBottomShadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mBottomShadowPaint);
				mBottomShadowPaint.setColor(fabColor);
			}else{
				mBottomShadowPaint.setAntiAlias(true);
				int color = (int)(255*shadowParams[0][0]/100);
				mBottomShadowPaint.setShadowLayer(shadowParams[0][3],
						shadowParams[0][1], shadowParams[0][2], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mBottomShadowPaint);
				mBottomShadowPaint.setColor(fabColor);
				
				int zindex = rightZindex - leftZindex;
				color = (int)(255*shadowParams[zindex][4]/100);
				
				mTopShadowPaint = new Paint();
				mTopShadowPaint.setAntiAlias(true);
				mTopShadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, mTopShadowPaint);
				mTopShadowPaint.setColor(fabColor);
			}
		}
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// draw bottom shadow
		canvas.save();
		if(topZindex != 0 && bottomZindex != 0){
			canvas.clipRect(0, getHeight()/2, getWidth(), getHeight());
			canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2- shadowRadus, mBottomShadowPaint);
			canvas.restore();
			canvas.clipRect(0, 0, getWidth(), getHeight()/2);
			canvas.drawCircle(getWidth()/2,getHeight()/2 , getWidth()/2 - shadowRadus, mTopShadowPaint);
		}else{
			canvas.clipRect(0, 0, getWidth()/2, getHeight());
			canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2- shadowRadus, mBottomShadowPaint);
			canvas.restore();
			canvas.clipRect(getWidth()/2, 0, getWidth(), getHeight());
			canvas.drawCircle(getWidth()/2,getHeight()/2 , getWidth()/2 - shadowRadus, mTopShadowPaint);
		}
		// draw icon
		
		if(icon != null){
			int widthPx = (int)DisplayUtils.convertDpToPixel(12, this.getContext());
			//icon.setBounds(getWidth()/2-widthPx, getHeight()/2-widthPx, getWidth()+widthPx, getHeight()+widthPx);
			icon.draw(canvas);
		}
		
	}
	
}
