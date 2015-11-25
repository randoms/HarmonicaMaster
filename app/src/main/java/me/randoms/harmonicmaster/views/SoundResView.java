package me.randoms.harmonicmaster.views;


import me.randoms.harmonicmaster.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SoundResView extends View{
	private int name =-1;
	private Paint mPaint;
	private Paint mEraser;
	private int soundColor;
	private float baseWidth;

	public SoundResView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.SpectrumView,
	            0, 0);
		try {
        	soundColor = a.getInteger(R.styleable.SpectrumView_colors, Color.rgb(0x66, 0xcc, 0xff));
        } finally {
        	a.recycle();
        }
        init();
	}
	
	private void init(){
		mPaint = new Paint();
		mPaint.setColor(soundColor);
		mPaint.setStrokeWidth(3f);
		mPaint.setAntiAlias(true);
		
		mEraser = new Paint();
		mEraser.setColor(Color.WHITE);
		mEraser.setStrokeWidth(3f);
		mEraser.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		baseWidth = (float)getWidth()/7;
		if(name == -1)return;
		float left = baseWidth*(name -1); // name starts from 1
		float right = baseWidth*name;
		canvas.drawRect(left,0, right,getHeight(), mEraser);
		canvas.drawRect(left,0, right,getHeight(), mPaint);
	}
	
	public void setSoundName(int mName){
		name = mName;
		invalidate();
	}
	

}
