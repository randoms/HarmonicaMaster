package me.randoms.harmonicmaster.shapes;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class Tone extends Sprite{


    // game related
    private boolean isActive = false;
    private int toneName = 0;
    private boolean isD = false;


    // texture related
    private static int textureList[] = null;


    public Tone(float left, float top, float height, int toneName, boolean isD){
        // initialize vertex byte buffer for shape coordinates
        super(left, top, 64, height, null);
        this.toneName = toneName;
        this.isD = isD;
    }

    public void draw(float[] mvpMatrix) {
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        int mTexCoordLoc = GLES20.glGetAttribLocation(mProgram,
                "a_texCoord" );
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );
        GLES20.glVertexAttribPointer (mTexCoordLoc , 2, GLES20.GL_FLOAT,
                false,
                0, textureBuffer);

        int mtrxhandle = GLES20.glGetUniformLocation(mProgram,
                "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mvpMatrix, 0);

        int mSamplerLoc = GLES20.glGetUniformLocation (mProgram,
                "s_texture" );
        if(isActive)
            GLES20.glUniform1i ( mSamplerLoc, textureList[2]);
        else if(isD)
            GLES20.glUniform1i ( mSamplerLoc, textureList[1]);
        else
            GLES20.glUniform1i ( mSamplerLoc, textureList[0]);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static void loadGLTexture(Bitmap[] textureImages, boolean reload) {
        if(textureList != null && !reload || textureImages == null)
            return; // already loaded
        int[] textures = new int[1];
        boolean firstTimeLoadFlag = false;
        if(textureList == null){
            firstTimeLoadFlag = true;
            textureList = new int[3];
        }
        // loading texture
        int count = 0;
        for(Bitmap image: textureImages){
            GLES20.glGenTextures(1, textures, 0);
            if(!reload || firstTimeLoadFlag){
                textureList[count] = getTextureIndex();
                addTextureIndex();
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureList[count]);
            }else{
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureList[count]);
            }
            count ++;
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
        }
    }

    public float getTop(){
        return this.top;
    }

    public float getBottom(){
        return this.top + this.height;
    }

    public int getToneName(){return this.toneName;}

    private static int currentTextureIndex = -1;
    @Override
    int getCurrentTextureIndex() {
        return currentTextureIndex;
    }

    @Override
    void setCurrentTextureIndex(int textureIndex) {
        currentTextureIndex = textureIndex;
    }

    public static Tone create(float left, float top, float height, int toneName, boolean isD, Bitmap[] textures){
        if(textures != null)
            Tone.loadGLTexture(textures, false);
        return new Tone(left, top, height, toneName, isD);
    }
}
