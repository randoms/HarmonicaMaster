package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.render.MyGLRenderer;
import me.randoms.harmonicmaster.riGraphicTools;
import me.randoms.harmonicmaster.utils.LogUtils;
import me.randoms.harmonicmaster.utils.MGLUtils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class Tone {

    private Context mContext;
    private final int mProgram;
    private int mPositionHandle;
    private final int vertexCount = 4;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // game related
    private boolean isActive = false;
    private int tonename = 0;
    private boolean isD = false;

    public static final String vertexShaderCode =
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String fragmentShaderCode =
                    "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";


    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private float width = 64;
    private float height = 128;
    private float top = 0;
    private float left = 0;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right
    static float virtualCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    // textures
    private FloatBuffer textureBuffer;	// buffer holding the texture coordinates
    private float texture[] = {
            // Mapping coordinates for the vertices
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };




    public Tone(Context mContext, float left, float top, float height, int tonename, boolean isD){
        // initialize vertex byte buffer for shape coordinates
        this.mContext = mContext;
        this.height = height;
        this.top = top;
        this.left = left;
        this.tonename = tonename;
        this.isD = isD;

        ByteBuffer bb = ByteBuffer.allocateDirect(
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        // calculate virtual position
        virtualCoords = new float[]{
                left, top, 0f,
                left, top + height, 0f,
                left + width, top + height, 0f,
                left + width, top, 0f,
        };
        // transform to gl coordinate
        squareCoords = MGLUtils.transformCordinateList(virtualCoords);
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // texture buffer
        ByteBuffer bb1 = ByteBuffer.allocateDirect(texture.length * 4);
        bb1.order(ByteOrder.nativeOrder());
        textureBuffer = bb1.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        loadGLTexture(mContext);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        GLES20.glUseProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
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
            GLES20.glUniform1i ( mSamplerLoc, 5);
        else if(isD)
            GLES20.glUniform1i ( mSamplerLoc, 6);
        else
            GLES20.glUniform1i ( mSamplerLoc, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /** The texture pointer */
    private int[] textures = new int[1];

    public void loadGLTexture(Context context) {
        // loading texture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.tone);
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        // Clean up
        bitmap.recycle();

        // load active texture
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.activetone1);
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE5);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap1, 0);
        // Clean up
        bitmap1.recycle();

        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.toned);
        GLES20.glGenTextures(1, textures, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE6);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap2, 0);
        // Clean up
        bitmap2.recycle();
    }

    public float getTop(){
        return this.top;
    }

    public float getBottom(){
        return this.top + this.height;
    }

    public int getTonename(){return this.tonename;}

}
