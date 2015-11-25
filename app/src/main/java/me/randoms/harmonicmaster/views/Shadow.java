package me.randoms.harmonicmaster.views;

import me.randoms.harmonicmaster.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Shadow extends View{

	private float[][] shadowParams ={
			{24,0,1,1,12,0,1,1.5f},
			{23,0,3,3,16,0,3,3},
			{23,0,6,3,19,0,10,10},
			{22,0,10,5,25,0,14,14},
			{22,0,15,6,30,0,19,19},
	}; 
	
	private int leftZindex;
	private int rightZindex;
	private int topZindex;
	private int bottomZindex;
	
	
	private Paint shadowPaint;
	
	public Shadow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.Shadow,
	            0, 0);
		leftZindex = a.getInt(R.styleable.Shadow_shadow_left_zindex, 0);
		rightZindex = a.getInt(R.styleable.Shadow_shadow_right_zindex, 0);
		topZindex = a.getInt(R.styleable.Shadow_shadow_top_zindex, 0);
		bottomZindex = a.getInt(R.styleable.Shadow_shadow_bottom_zindex, 0);
		init();
	}

	private void init(){
		
		shadowPaint = new Paint();
		shadowPaint.setAntiAlias(true);
		if(leftZindex != 0 && rightZindex != 0){
			if(leftZindex > rightZindex){
				/*zindex = leftZindex-rightZindex;
				Log.d("RandomsZ",String.valueOf(zindex));
				int color = (int)(255*shadowParams[zindex][4]/100);
				LinearGradient gradient = new LinearGradient(0, 0, shadowParams[zindex][7], 0,
						Color.argb(color, 0, 0, 0), Color.argb(0, 0, 0, 0), android.graphics.Shader.TileMode.CLAMP);
				shadowPaint.setDither(true);
				shadowPaint.setShader(gradient);*/
				int zindex = leftZindex - rightZindex;
				int color = (int)(255*shadowParams[zindex][4]/100);
				
				shadowPaint.setAntiAlias(true);
				shadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
				shadowPaint.setColor(Color.WHITE);
			}else{
				/*zindex = rightZindex -leftZindex;
				Log.d("RandomsZ",String.valueOf(zindex));
				int color = (int)(255*shadowParams[zindex][4]/100);
				LinearGradient gradient = new LinearGradient(0, 0, shadowParams[zindex][7], 0,
						 Color.argb(0, 0, 0, 0),Color.argb(color, 0, 0, 0), android.graphics.Shader.TileMode.CLAMP);
				shadowPaint.setDither(true);
				shadowPaint.setShader(gradient);*/
				int zindex = rightZindex - leftZindex;
				int color = (int)(255*shadowParams[zindex][4]/100);
				shadowPaint.setAntiAlias(true);
				shadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
				shadowPaint.setColor(Color.WHITE);
			}
		}
		if(topZindex != 0 && bottomZindex != 0){
			if(topZindex > bottomZindex){
				int zindex = topZindex - bottomZindex;
				int color = (int)(255*shadowParams[zindex][4]/100);
				shadowPaint.setAntiAlias(true);
				shadowPaint.setShadowLayer(shadowParams[zindex][7], 
						shadowParams[zindex][5], shadowParams[zindex][6], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
				shadowPaint.setColor(Color.WHITE);
			}else{
				
				int zindex = bottomZindex - topZindex;
				Log.d("Randoms",String.valueOf(zindex));
				int color = (int)(255*shadowParams[zindex][0]/100);
				shadowPaint.setAntiAlias(true);
				shadowPaint.setShadowLayer(shadowParams[zindex][3], 
						shadowParams[zindex][1], -shadowParams[zindex][2], 
						Color.argb(color, 0, 0, 0));
				setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
				shadowPaint.setColor(Color.WHITE);
			}
		}
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(leftZindex > rightZindex){
			canvas.translate(-40, 0);
			canvas.drawRect(0, 0, 40, getHeight(), shadowPaint);
			
		}
		
		if(rightZindex > leftZindex){
			canvas.drawRect(getWidth(), 0, getWidth()+40, getHeight(), shadowPaint);
		}
		
		if(topZindex >bottomZindex){
			canvas.translate(0, -40);
			canvas.drawRect(0, 0, getWidth(), 40, shadowPaint);
		}
		if(topZindex <bottomZindex){
			canvas.drawRect(0, getHeight(), getWidth(), getHeight()+40, shadowPaint);
		}
	}
	
	
}
