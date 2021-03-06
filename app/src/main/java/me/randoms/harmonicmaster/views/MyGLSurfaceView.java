package me.randoms.harmonicmaster.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.ToneModel;
import me.randoms.harmonicmaster.render.GameRender;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.views
 */
public class MyGLSurfaceView extends GLSurfaceView{

    private GameRender mRenderer;
    private int score = 0;
    private boolean gameoverFlag = false;
    private Runnable gameoverHandler = null;
    private Runnable updateScoreHandler = null;
    private Runnable pauseHandler = null;

    public MyGLSurfaceView(Context context){
        super(context);
    }

    //XML inflation/instantiation
    public MyGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void start(Music musicJson, ToneModel[] tonelist, GameActivity gameActivity){
        mRenderer = new GameRender(gameActivity, musicJson, tonelist);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);

        startScoreThread();
    }

    public void setCurrentTone(int Tone){
        mRenderer.setCurrentActiveTone(Tone);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mRenderer.isPaused() && !gameoverFlag){
            if(pauseHandler != null)
                pauseHandler.run();
            mRenderer.pause();
        }
        return super.onTouchEvent(event);
    }

    public void resume(int gametime){
        if(mRenderer.isPaused()){
            mRenderer.resume(gametime);
        }
    }

    public void gameover(){
        gameoverFlag = true;
    }

    public void setGameOverHandler(Runnable gameOverHandler){
        this.gameoverHandler = gameOverHandler;
    }

    public void setUpdateScoreHandler(Runnable updateScoreHandler){
        this.updateScoreHandler = updateScoreHandler;
    }

    public int getScore(){
        return score;
    }

    public void setPauseHandler(Runnable pauseHandler){
        this.pauseHandler = pauseHandler;
    }

    public void restart(){

        mRenderer.restart();
        startScoreThread();
        gameoverFlag = false;
        score = 0;
    }

    private void startScoreThread(){
        // score thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(!gameoverFlag){
                        Thread.sleep(30);
                        if(mRenderer.isMatched() && !mRenderer.isPaused()){
                            score = score + 1;
                        }

                        if(updateScoreHandler != null){
                            updateScoreHandler.run();
                        }
                        if(mRenderer.isGameOver()){
                            gameover();
                            if(gameoverHandler != null)
                                gameoverHandler.run();
                        }

                    }
                }catch (InterruptedException e){e.printStackTrace();}
            }
        }).start();
    }
}
