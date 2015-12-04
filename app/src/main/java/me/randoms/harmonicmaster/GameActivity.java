package me.randoms.harmonicmaster;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import me.randoms.harmonicmaster.models.Harmonic10;
import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.ToneModel;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;
import me.randoms.harmonicmaster.views.MyGLSurfaceView;

public class GameActivity extends Activity {

    private MyGLSurfaceView mGLView;
    private TextView scoreView;
    private Music music;
    private AudioDispatcher dispatcher;
    private ToneModel[] toneList;
    private int previousTone = -1;
    private Handler mHandler;

    // current Views
    private View currentToneView;
    private View firstPointView;
    private View secondPointView;
    private View thirdPointView;
    private View forthPointView;
    private TextView toneView;

    // pauseViews
    private View pauseView;
    private TextView pauseTitleView;

    // game over view
    private View gameOverView;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        music = getMusic();
        mGLView = (MyGLSurfaceView)findViewById(R.id.game);
        scoreView = (TextView)findViewById(R.id.score);
        mGLView.setUpdateScoreHandler(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoreView.setText(String.valueOf(mGLView.getScore()));
                    }
                });
            }
        });


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mHandler = new Handler();

        // init views;
        currentToneView = findViewById(R.id.currentTone);
        firstPointView = findViewById(R.id.firstPoint);
        secondPointView = findViewById(R.id.secondPoint);
        thirdPointView = findViewById(R.id.thirdPoint);
        forthPointView = findViewById(R.id.forthPoint);
        toneView = (TextView) findViewById(R.id.tone);

        pauseView = findViewById(R.id.pauseView);
        ImageView pauseStartView = (ImageView)findViewById(R.id.start);
        pauseTitleView = (TextView)findViewById(R.id.title);

        gameOverView = findViewById(R.id.gameOverView);
        ImageView gameOverStart = (ImageView) findViewById(R.id.gameOverStart);
        ImageView gameOverExit = (ImageView) findViewById(R.id.gameOverExit);


        gameOverExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameOverStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameOverView.setVisibility(View.GONE);
                        mGLView.restart();
                    }
                });
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch (prefs.getInt(Statics.HARMONIC_TYPE, 10)){
            case 10:
                toneList = Harmonic10.mToneList;
                break;
            default:
                toneList = Harmonic10.mToneList;
        }

        mGLView.setPauseHandler(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pauseTitleView.setText(music.getName());
                        pauseView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        mGLView.setGameOverHandler(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameOverView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        pauseStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseView.setVisibility(View.GONE);
                mGLView.resume();
            }
        });

        mGLView.start(music, toneList, this);

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                if(pitchInHz < Statics.FREQS[0] * 0.97 || pitchInHz > Statics.FREQS[Statics.FREQS.length - 1] * 1.03){
                    mGLView.setCurrentTone(-1);
                    return;
                }
                for(int i = 0;i< Statics.FREQS.length;i++){
                    if(pitchInHz > Statics.FREQS[i]*0.97 && pitchInHz < Statics.FREQS[i]*1.03){
                        mGLView.setCurrentTone(i + 1);
                        Log.d("Tone", i + "");
                    }
                }
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }


    public Music getMusic(){
        ArrayList<Music> musicList = Utils.getMusicSheets();
        String uuid = getIntent().getStringExtra("uuid");
        for(Music music : musicList){
            if(music.getId().equals(uuid)){
                return music;
            }
        }
        finish(); // no valid music found
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        dispatcher.stop();
    }

    public void updateCurrentTone(final int tonename){
        if(previousTone != tonename){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(tonename == -1){
                        currentToneView.setVisibility(View.GONE);
                        previousTone = tonename;
                        return;
                    }
                    final String tonenameText = Statics.TONE_NAME[tonename % 12];
                    final int level = tonename/12;
                    switch (level){
                        case 0:
                            firstPointView.setVisibility(View.INVISIBLE);
                            secondPointView.setVisibility(View.INVISIBLE);
                            thirdPointView.setVisibility(View.VISIBLE);
                            forthPointView.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            firstPointView.setVisibility(View.INVISIBLE);
                            secondPointView.setVisibility(View.INVISIBLE);
                            thirdPointView.setVisibility(View.VISIBLE);
                            forthPointView.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            firstPointView.setVisibility(View.INVISIBLE);
                            secondPointView.setVisibility(View.INVISIBLE);
                            thirdPointView.setVisibility(View.INVISIBLE);
                            forthPointView.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            firstPointView.setVisibility(View.INVISIBLE);
                            secondPointView.setVisibility(View.VISIBLE);
                            thirdPointView.setVisibility(View.INVISIBLE);
                            forthPointView.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            firstPointView.setVisibility(View.VISIBLE);
                            secondPointView.setVisibility(View.VISIBLE);
                            thirdPointView.setVisibility(View.INVISIBLE);
                            forthPointView.setVisibility(View.INVISIBLE);
                            break;
                    }

                    toneView.setText(tonenameText);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(metrics.widthPixels/16 - 16, ViewGroup.LayoutParams.MATCH_PARENT);
                    if(toneList[tonename] != null){
                        int left = toneList[tonename].positon * metrics.widthPixels / 16 + 4;
                        if(left < 0){
                            currentToneView.setVisibility(View.GONE);
                            previousTone = tonename;
                            return;
                        }
                        lp.setMargins(left, 0, 0, 0);
                    }
                    currentToneView.setLayoutParams(lp);
                    currentToneView.setVisibility(View.VISIBLE);
                    previousTone = tonename;
                }
            });
        }
    }
}
