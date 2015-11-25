package me.randoms.harmonicmaster;

import me.randoms.harmonicmaster.callback.ProcessAudioHandler;
import me.randoms.harmonicmaster.utils.Utils;
import me.randoms.harmonicmaster.views.SpectrumView;
import me.randoms.harmonicmaster.views.TimeFieldView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	private AudioStream mAudio;
	private ProcessAudioHandler mAudioHandler;
	private static SpectrumView mSpectView;
	private static TimeFieldView mTimeView;
	private int insertSound = -1;
	private static TextView resText;
	private static TextView statusText;
	
	private static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == 0){
				mSpectView.setData(AudioProcesser.getFFT());
				mTimeView.setData(AudioProcesser.getSoundData());
				statusText.setText(AudioProcesser.getRes());
				resText.setText(String.valueOf(AudioProcesser.getSoundName()));
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent mIntent = getIntent();
		insertSound = mIntent.getIntExtra("insertSound", -1);
		// set tasks
		AudioProcesser.setInsertSound(insertSound);
		AudioProcesser.stopRecognize();
		mSpectView = (SpectrumView)findViewById(R.id.spectrumView);
		mTimeView = (TimeFieldView)findViewById(R.id.timeView);
		resText = (TextView)findViewById(R.id.recognize_res);
		statusText = (TextView)findViewById(R.id.sound_status);
		
		mAudioHandler = new ProcessAudioHandler(){

			
			@Override
			public void onProcess(short[] buffer) {
				// TODO Auto-generated method stub
				//begin process
				if(!AudioProcesser.isProcessing()){ // 尚未处理完
					AudioProcesser.process(buffer);
					if(insertSound != -1 && !AudioProcesser.isStatic()){
						// static over
						Log.d("RandomsRes",Utils.arrayToString(AudioProcesser.soundDb[insertSound]));
						mAudio.close();
						Utils.saveDB(AudioProcesser.soundDb,MainActivity.this);
						finish();
					}
					Message msg = Message.obtain();
					msg.what = 0;
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onStop() {
				// TODO Auto-generated method stub
				Log.d("Randoms","Stop");
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		mAudio = new AudioStream(mAudioHandler);
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mAudio.close();
	}
	
	
}