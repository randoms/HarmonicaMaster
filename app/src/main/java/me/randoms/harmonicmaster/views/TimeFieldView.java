package me.randoms.harmonicmaster.views;

import me.randoms.harmonicmaster.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TimeFieldView extends View{
	private int lineColor;
	private float lineSize;
	private short[] mBytes;
	private float[] mPoints;
	private Paint mPaint = new Paint();

	public TimeFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.SpectrumView,
	            0, 0);
        try {
        	lineColor = a.getInteger(R.styleable.SpectrumView_colors, Color.rgb(0x66, 0xcc, 0xff));
        	lineSize = a.getFloat(R.styleable.SpectrumView_line_size, 2);
        } finally {
        	a.recycle();
        }
        init();
	}
	
	private void init(){
		mBytes = null;
		mPaint.setStrokeWidth(lineSize);
		mPaint.setAntiAlias(true);
		mPaint.setColor(lineColor);
	}

	public void setData(short[] mdata){
		mBytes = mdata;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mBytes == null){
			return;
		}

		if (mPoints == null || mPoints.length < mBytes.length * 4){
			mPoints = new float[mBytes.length * 4];
		}
		
		//绘制波形
		for (int i = 0; i < mBytes.length - 1; i++) {
		 mPoints[i * 4] = getWidth() * i / (mBytes.length - 1);
		 mPoints[i * 4 + 1] = getHeight() / 2
		 + (short) ((getHeight() / 2)*mBytes[i]/Short.MAX_VALUE);
		 mPoints[i * 4 + 2] = getWidth() * (i + 1) / (mBytes.length - 1);
		 mPoints[i * 4 + 3] = getHeight() / 2
		 + (short) ((getHeight() / 2)*mBytes[i+1]/Short.MAX_VALUE);
		 }
		canvas.drawLines(mPoints, mPaint);
	}
	
	
	
}
