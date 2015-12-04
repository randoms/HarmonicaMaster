package me.randoms.harmonicmaster.shapes;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import me.randoms.harmonicmaster.render.GameRender;
import me.randoms.harmonicmaster.utils.MGLUtils;

public abstract class Sprite
{
    static final int COORDS_PER_VERTEX = 3;
    public static final String fragmentShaderCode =
                "precision mediump float;" +
                "varying vec2 v_texCoord;" +
                "uniform sampler2D s_texture;" +
                "void main() {  " +
                    "gl_FragColor = texture2D( s_texture, v_texCoord );" +
                "}";
    static float[] squareCoords = { -0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, 0.5F, 0.0F };
    private static int textureIndex = 0;
    public static final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {  " +
                "gl_Position = uMVPMatrix * vPosition;" +
                "  v_texCoord = a_texCoord;" +
            "}";
    static float[] virtualCoords = { -0.5F, 0.5F, 0.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 0.0F, 0.5F, 0.5F, 0.0F };
    protected ShortBuffer drawListBuffer;
    protected short[] drawOrder = { 0, 1, 2, 0, 2, 3 };
    private float height = 0.0F;
    private float left = 0.0F;
    protected int mProgram;
    protected FloatBuffer textureBuffer;
    private int[] textures = new int[1];
    private float top = 0.0F;
    protected FloatBuffer vertexBuffer;
    private float width = 0.0F;

    public Sprite(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Bitmap paramBitmap)
    {
        this(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramBitmap, null);
    }

    public Sprite(float left, float top, float width, float height, Bitmap textureImage, Rect textureArea)
    {
        this.height = height;
        this.top = top;
        this.left = left;
        this.width = width;
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        virtualCoords = new float[] {
                left, top, 0.0F,
                left, top + height, 0.0F,
                left + width, top + height, 0.0F,
                left + width, top, 0.0F
        };
        squareCoords = MGLUtils.transformCoordinateList(virtualCoords);
        this.vertexBuffer.put(squareCoords);
        this.vertexBuffer.position(0);
        bb = ByteBuffer.allocateDirect(this.drawOrder.length * 2);
        bb.order(ByteOrder.nativeOrder());
        this.drawListBuffer = bb.asShortBuffer();
        this.drawListBuffer.put(this.drawOrder);
        this.drawListBuffer.position(0);
        // texture buffer
        float texture[] = {
                // Mapping coordinates for the vertices
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        ByteBuffer bb1 = ByteBuffer.allocateDirect(texture.length * 4);
        bb1.order(ByteOrder.nativeOrder());
        textureBuffer = bb1.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        if(textureArea != null){
            Bitmap textureBitmap = Bitmap.createBitmap(textureImage, textureArea.left, textureArea.top,
                    textureArea.width(), textureArea.height());
            loadGLTexture(textureBitmap);
            textureBitmap.recycle();
        }else{
            loadGLTexture(textureImage);
        }

        int vertexShader = GameRender.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GameRender.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        GLES20.glUseProgram(mProgram);
    }

    public static void addTextureIndex()
    {
        textureIndex += 1;
    }

    public static int getTextureIndex()
    {
        return textureIndex;
    }

    public void draw(float[] paramArrayOfFloat)
    {
        int mPositionHandler = GLES20.glGetAttribLocation(this.mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandler);
        GLES20.glVertexAttribPointer(mPositionHandler, COORDS_PER_VERTEX
                , GLES20.GL_FLOAT, false, 0, this.vertexBuffer);
        int mTexCoordLoc = GLES20.glGetAttribLocation(this.mProgram, "a_texCoord");
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false, 0, this.textureBuffer);
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(this.mProgram, "uMVPMatrix"),
                1, false, paramArrayOfFloat, 0);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(this.mProgram, "s_texture"),
                getCurrentTextureIndex());
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, this.drawOrder.length, GLES20.GL_UNSIGNED_SHORT
                , this.drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandler);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    abstract int getCurrentTextureIndex();

    public void loadGLTexture(Bitmap bitmap) {
        if ((getCurrentTextureIndex() != -1) || (bitmap == null)) {
            return;
        }
        // loading texture
        GLES20.glGenTextures(1, textures, 0);
        setCurrentTextureIndex(textureIndex);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + getCurrentTextureIndex());
        textureIndex += 1;
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    abstract void setCurrentTextureIndex(int paramInt);
}
