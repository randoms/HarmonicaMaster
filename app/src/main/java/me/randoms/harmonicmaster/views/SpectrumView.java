package me.randoms.harmonicmaster.views;

import me.randoms.harmonicmaster.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SpectrumView extends View{

	private int lineColor;
	private float lineSize;
	private Paint mFFTPaint;
	private int mSpectrumNum;
	private double[] mFFTBytes;
	private float[] fftPoints;
	
	public SpectrumView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SpectrumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
	            attrs,
	            R.styleable.SpectrumView,
	            0, 0);
        try {
        	lineColor = a.getInteger(R.styleable.SpectrumView_colors, Color.rgb(0xff, 0xcc, 0xff));
        	lineSize = a.getFloat(R.styleable.SpectrumView_line_size, 2);
        	mSpectrumNum = a.getInteger(R.styleable.SpectrumView_spectrum_num, 0);
        } finally {
        	a.recycle();
        }
        init();
    }
	
	public void setData(double[] mdata){
		mFFTBytes = mdata;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(fftPoints ==null || fftPoints.length <mFFTBytes.length*4){
			fftPoints = new float[mFFTBytes.length*4];
		}

		//绘制频谱
		final float baseX = (float)getWidth()/mSpectrumNum;
		final float height = getHeight();

		for (int i = 0; i < mSpectrumNum ; i++){
			final float xi = baseX*i + baseX/2;
			fftPoints[i * 4] = xi;
			fftPoints[i * 4 + 1] = height;
			
			fftPoints[i * 4 + 2] = xi;
			fftPoints[i * 4 + 3] = height - (float)mFFTBytes[i]/1000;
		}
		
		canvas.drawLines(fftPoints,mFFTPaint);
	}
	
	private void init(){
		mFFTPaint = new Paint();
		mFFTPaint.setStrokeWidth(lineSize);
		mFFTPaint.setAntiAlias(true);
		mFFTPaint.setColor(lineColor);
		mFFTBytes = new double[mSpectrumNum];
	}

}
