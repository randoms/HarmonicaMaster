package me.randoms.harmonicmaster;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import me.randoms.harmonicmaster.callback.ProcessAudioHandler;
import me.randoms.harmonicmaster.callback.ProcessCallBack;
import me.randoms.harmonicmaster.json.JSONObject;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;
import me.randoms.harmonicmaster.views.PlaySoundView;
import me.randoms.harmonicmaster.views.SoundResView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends Activity{
	private JSONObject musicJson;
	private static PlaySoundView mView;
	private static SoundResView mResView;
	private static View separator;
	private View playLayout;
	private View scoreLayout;
	private Button exitBtn;
	private TextView scoreView;
	private static PlayActivity that;
	private static long score = 0;

	
	// 更新view的handler
	private static Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int soundName = msg.what;
			mResView.setSoundName(soundName + 1);
			if(soundName == mView.getCurrnetSound()){
				separator.setBackgroundColor(that.getResources().getColor(R.color.separator_active));
				score += 20;
			}else{
				separator.setBackgroundColor(that.getResources().getColor(R.color.separator));
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		that = this;
		score = 0;
		musicJson = getMusic();
		setContentView(R.layout.activity_play);
		separator = findViewById(R.id.separator);
		exitBtn = (Button)findViewById(R.id.exitBtn);
		exitBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		playLayout = findViewById(R.id.playLayout);
		scoreLayout = findViewById(R.id.scoreLayout);
		scoreView = (TextView)findViewById(R.id.score);
		mView = (PlaySoundView)findViewById(R.id.playView);
		mView.setSound(musicJson);
		mView.setCallBack( new ProcessCallBack(){

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProcess(Class<?> obj) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStop() {
				// TODO Auto-generated method stub
				playLayout.setVisibility(View.GONE);
				scoreLayout.setVisibility(View.VISIBLE);
				scoreView.setText(String.valueOf(score));
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		mResView = (SoundResView)findViewById(R.id.resView);

		AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

		PitchDetectionHandler pdh = new PitchDetectionHandler() {
			@Override
			public void handlePitch(PitchDetectionResult result,AudioEvent e) {
				final float pitchInHz = result.getPitch();
				if(pitchInHz < Statics.FREQS[0] - 20 || pitchInHz > Statics.FREQS[6] + 20)
					return;
				for(int i = 0;i< Statics.FREQS.length - 1;i++){
					if(pitchInHz > Statics.FREQS[i] - 20 && pitchInHz < Statics.FREQS[i] + 20){
						// match
						Message msg = Message.obtain();
						msg.what = i;
						mHandler.sendMessage(msg);
					}
				}
			}
		};
		AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
		dispatcher.addAudioProcessor(p);
		new Thread(dispatcher,"Audio Dispatcher").start();
		

	}
	
	public JSONObject getMusic(){
		JSONObject[] musicList = Utils.getMusicSheets(this);
		String uuid = getIntent().getStringExtra("uuid");
		Log.d("Randoms",uuid);
		for(int i=0;i<musicList.length;i++){
			if(musicList[i].getString("id").equals(uuid)){
				return musicList[i];
			}
		}
		finish(); // no valid music found
		return null;
	}
	
	



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//mAudio.close();
	}
	
}
