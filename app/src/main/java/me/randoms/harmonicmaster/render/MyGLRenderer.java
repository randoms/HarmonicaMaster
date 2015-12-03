package me.randoms.harmonicmaster.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONObject;
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
public class MyGLRenderer implements GLSurfaceView.Renderer {

    /**
     * shapes
     */
    private Background mBg;
    private Title title;
    private BottomBackground mBottomBg;
    private ArrayList<Tone> toneList = new ArrayList<>();
    private ToneModel[] bottomToneList;
    private ArrayList<ActiveTone> activeToneList = new ArrayList<>();
    private int currentActiveTone = -1;

    /**
     * music file related
     */
    private JSONArray music;
    private long musicLength;

    /**
     * game time
     */
    private long lastUpdateTime = 0;
    private long gameTime = 0;
    private boolean paused = false;

    /**
     * game score
     */
    private float checkLinePosYinPix = 512f;
    private boolean isMatched = false;
    private boolean isGameOver = false;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mStaticViewMatrix = new float[16];
    private GameActivity mContext;
    private JSONObject musicData;


    public MyGLRenderer(GameActivity mContext, JSONObject musicJson, ToneModel[] bottomToneList){
        this.mContext = mContext;
        musicData = musicJson;
        music = musicData.getJSONArray("sounds");
        musicLength = musicData.getLong("length");
        this.bottomToneList = bottomToneList;
    }


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mBg = new Background(mContext);
        mBottomBg = new BottomBackground(mContext);
        title = new Title(mContext);
        // create tones
        for(int count = 0;count<music.length();count ++){
            JSONObject sound = music.getJSONObject(count);
            int speed = 128;
            float top = -speed*(float)sound.getLong("end")/1000f;
            float bottom = -speed*(float)sound.getLong("start")/1000f;
            if(sound.getInt("name")-1 > 0 && sound.getInt("name")-1 < bottomToneList.length && bottomToneList[sound.getInt("name")-1] != null){
                float left =  80*(bottomToneList[sound.getInt("name")-1].positon) + 4; // name begins from 1
                toneList.add(new Tone(mContext, left, top, bottom - top, sound.getInt("name"), bottomToneList[sound.getInt("name")-1].isD));
            }else {
                toneList.add(null);
            }
        }

        // create active tones
        for(int count = 0; count<bottomToneList.length;count++){
            if(bottomToneList[count] != null){
                float top = 600;
                float height = 128;
                float left = 80 * bottomToneList[count].positon + 4;
                activeToneList.add(new ActiveTone(mContext, left, top, height));
            }else {
                activeToneList.add(null);
            }
        }

    }

    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];

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
        boolean noToneFlag = true;
        for(int count = 0; count < toneList.size();count ++){
            if(toneList.get(count) == null){
                mContext.updateCurrentTone(-1);
                continue;
            }
            if(toneList.get(count).getTop() > (cameraPoxYinPix + 720/2) || toneList.get(count).getBottom() < (cameraPoxYinPix - 720/2)){
                continue; // out of camera range
            }
            Tone currentTone = toneList.get(count);
            if(currentTone.getTonename() == currentActiveTone
                    && currentTone.getBottom() > cameraPoxYinPix + (checkLinePosYinPix - 720/2)
                    && currentTone.getTop() < cameraPoxYinPix + (checkLinePosYinPix - 720/2)){
                currentTone.setActive(true);
                isMatched = true;
            }

            // draw tone name string
            if(currentTone.getBottom() > cameraPoxYinPix + (checkLinePosYinPix - 720/2)
                    && currentTone.getTop() < cameraPoxYinPix + (checkLinePosYinPix - 720/2)){
                mContext.updateCurrentTone(currentTone.getTonename() - 1);
                noToneFlag = false;
            }
            else{
                currentTone.setActive(false);
                isMatched = false;
            }

            toneList.get(count).draw(mViewMatrix);
        }
        mBottomBg.draw(mStaticViewMatrix);

        if(noToneFlag)
            mContext.updateCurrentTone(-1);
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
