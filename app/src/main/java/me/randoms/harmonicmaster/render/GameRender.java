package me.randoms.harmonicmaster.render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.Sound;
import me.randoms.harmonicmaster.models.ToneModel;
import me.randoms.harmonicmaster.shapes.ActiveTone;
import me.randoms.harmonicmaster.shapes.Background;
import me.randoms.harmonicmaster.shapes.BottomBackground;
import me.randoms.harmonicmaster.shapes.Title;
import me.randoms.harmonicmaster.shapes.Tone;
import me.randoms.harmonicmaster.utils.MGLUtils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.render
 */
public class GameRender implements GLSurfaceView.Renderer {

    /**
     * shapes
     */
    private Background mBg;
    private Title title;
    private BottomBackground mBottomBg;
    private ArrayList<Tone> toneList = new ArrayList<>();
    private ToneModel[] instrumentTones;
    private ArrayList<ActiveTone> activeToneList = new ArrayList<>();
    private int currentActiveTone = -1;

    /**
     * music file related
     */
    private ArrayList<Sound> sounds;
    private long musicLength;

    /**
     * game time
     */
    private long lastUpdateTime = 0;
    private long gameTime = 0;
    private boolean paused = false;

    private boolean isMatched = false;
    private boolean isGameOver = false;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mStaticViewMatrix = new float[16];
    private GameActivity mContext;


    public GameRender(GameActivity mContext, Music music, ToneModel[] instrumentTones){
        this.mContext = mContext;
        this.sounds =  music.getSounds();
        musicLength = music.getLength();
        this.instrumentTones = instrumentTones;
    }


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mBg = Background.create(mContext);
        mBottomBg = BottomBackground.create(mContext);
        title = Title.create(mContext);
        // create tones
        /*Bitmap toneBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tone);
        Bitmap tonedBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.toned);
        Bitmap toneActiveBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.activetone1);
        Bitmap[] toneTextures = new Bitmap[]{toneBitmap, tonedBitmap, toneActiveBitmap};
        for(Sound sound : sounds){
            int speed = 128;
            float top = -speed*(float)sound.getEnd()/1000f;
            float bottom = -speed*(float)sound.getStart()/1000f;
            int soundIndex = sound.getName()-1;
            if(sound.getName()-1 > 0 && sound.getName()-1 < instrumentTones.length && instrumentTones[soundIndex] != null){
                float left =  80*(instrumentTones[soundIndex].positon) + 4; // name begins from 1
                toneList.add(Tone.create(left, top, bottom - top, sound.getName(), instrumentTones[soundIndex].isD, toneTextures));
            }else {
                toneList.add(null);
            }
        }
        for(Bitmap image: toneTextures){
            image.recycle();
        }*/

        // create active tones
        Bitmap activeToneImage = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.activetone);
        for(ToneModel instrumentTone : instrumentTones) {
            if (instrumentTone != null) {
                float top = 600;
                float left = 80 * instrumentTone.positon + 4;
                activeToneList.add(new ActiveTone(activeToneImage,left, top));
            } else {
                activeToneList.add(null);
            }
        }
        activeToneImage.recycle();

    }

    public void onDrawFrame(GL10 unused) {

        // Set the camera position (View matrix)
        if(lastUpdateTime == 0 || paused){
            lastUpdateTime = SystemClock.uptimeMillis();
            return;
        }

        gameTime += SystemClock.uptimeMillis() - lastUpdateTime;
        lastUpdateTime = SystemClock.uptimeMillis();
        float cameraPoxY = gameTime / 1000.0f * MGLUtils.transformCoordinateY(128);
        float cameraPoxYinPix = -gameTime / 1000.0f * 128 + 720/2;
        if( - (musicLength + 1000) * 128 /1000f > (cameraPoxYinPix + 720/2)){
            // last tone has outof screen
            isGameOver = true;
            return;
        }

        Matrix.setLookAtM(mViewMatrix, 0, 0f, cameraPoxY, 1f, 0f, cameraPoxY, 0f, 0f, 1.0f, 0.0f);
        Matrix.setLookAtM(mStaticViewMatrix, 0, 0f, 0, 1f, 0f, 0, 0f, 0f, 1.0f, 0.0f);

        // Draw triangle
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mBg.draw(mStaticViewMatrix);
        title.draw(mStaticViewMatrix);
        /*boolean noToneFlag = true;
        for(int count = 0; count < toneList.size();count ++){
            if(toneList.get(count) == null){
                mContext.updateCurrentTone(-1);
                continue;
            }
            if(toneList.get(count).getTop() > (cameraPoxYinPix + 720/2) || toneList.get(count).getBottom() < (cameraPoxYinPix - 720/2)){
                continue; // out of camera range
            }
            Tone currentTone = toneList.get(count);
            float checkLinePosYinPix = 512f;
            if(currentTone.getToneName() == currentActiveTone
                    && currentTone.getBottom() > cameraPoxYinPix + (checkLinePosYinPix - 720/2)
                    && currentTone.getTop() < cameraPoxYinPix + (checkLinePosYinPix - 720/2)){
                currentTone.setActive(true);
                isMatched = true;
            }

            // draw tone name string
            if(currentTone.getBottom() > cameraPoxYinPix + (checkLinePosYinPix - 720/2)
                    && currentTone.getTop() < cameraPoxYinPix + (checkLinePosYinPix - 720/2)){
                mContext.updateCurrentTone(currentTone.getToneName() - 1);
                noToneFlag = false;
            }
            else{
                currentTone.setActive(false);
                isMatched = false;
            }

            toneList.get(count).draw(mViewMatrix);
        }*/
        mBottomBg.draw(mStaticViewMatrix);

        //if(noToneFlag)
        //    mContext.updateCurrentTone(-1);
        // draw active tone
        if(currentActiveTone != -1){
            if(activeToneList.get(currentActiveTone - 1) != null)
                activeToneList.get(currentActiveTone - 1).draw(mStaticViewMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void setCurrentActiveTone(int currentActiveTone){
        this.currentActiveTone = currentActiveTone;
    }

    public void pause(){
        paused = true;
    }

    public  void resume(){
        paused = false;
    }

    public boolean isPaused(){
        return paused;
    }

    public boolean isMatched(){
        return isMatched;
    }

    public boolean isGameOver(){
        return isGameOver;
    }
}
