package me.randoms.harmonicmaster.views;

import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.callback.ProcessCallBack;
import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONException;
import me.randoms.harmonicmaster.json.JSONObject;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class PlaySoundView extends View{
	private int soundColor;
	private JSONArray music;
	private long length;
	
	// current sound related
	private int currentSound = -1;
	
	private static PlaySoundView that;
	
	// draw related
	private Paint mPaint;
	private Paint eraser;
	private float baseWidth;
	private float speed = 100;
	
	
	//callbacks
	private ProcessCallBack mCallBack = null;
	
	private static Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			that.invalidate();
		}
		
	};
	
	private class Timer extends Thread{
		private boolean stop = false;
		private Handler mHandler;
		private long time;
		
		public Timer(Handler mhandler){
			mHandler = mhandler;
			time = 0;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(!stop){
				Message msg = Message.obtain();
				msg.what = 0;
				mHandler.sendMessage(msg);
				try {
					sleep(20);
					time +=20;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}
		
		public void close(){
			stop = true;
		}
		
		public long getTime(){
			return time;
		}
	}
	
	private Timer mTimer = new Timer(mHandler);
	

	public PlaySoundView(Context context, AttributeSet attrs) {
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
        that = this;
        init();
        
	}
	
	private void init(){
		mPaint = new Paint();
		eraser = new Paint();
		
		mPaint.setStrokeWidth(3f);
		mPaint.setColor(soundColor);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(80);
		
		eraser.setStrokeWidth(3f);
		eraser.setColor(Color.WHITE);
		eraser.setAntiAlias(true);
		mTimer.start();
		if(mCallBack != null)mCallBack.onStart();
	}
	
	public void setSound(JSONObject mMusic){
		music = mMusic.getJSONArray("sounds");
		length = mMusic.getLong("length");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		JSONObject sound;
		baseWidth = (float)getWidth()/7;
		float top = 0;
		float left = 0;
		float right = 0;
		float bottom = 0;
		currentSound = -1;
		for(int i =0;i<music.length();i++){
			try {
				sound = music.getJSONObject(i);
				top = -speed*(float)sound.getLong("end")/1000 + mTimer.getTime()/(float)10+getHeight();
				bottom = -speed*(float)sound.getLong("start")/1000 + mTimer.getTime()/(float)10+getHeight();
				left = baseWidth*(sound.getInt("name")-1); // name begins from 1
				right = baseWidth*(sound.getInt("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mCallBack.onError(e);
			}
			
			if(bottom > 0 && top < getHeight()){
				canvas.drawRect(left, top, right, bottom, mPaint);
				if(bottom > getHeight() && top <getHeight()){
					// current active sound
					currentSound = music.getJSONObject(i).getInt("name")-1;
				}
			}
			
			if(mTimer.getTime() > length){
				if(mCallBack != null)mCallBack.onStop();
			}
		}
		
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		mTimer.close();
	}
	
	public void setCallBack(ProcessCallBack cb){
		mCallBack = cb;
	}
	
	public int getCurrnetSound(){
		return currentSound;
	}

}
