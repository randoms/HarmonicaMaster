package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.render.GameRender;
import me.randoms.harmonicmaster.utils.MGLUtils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class BottomBackground extends Sprite{

    private static int currentTextureIndex = -1;

    public BottomBackground(Bitmap textureImage){
        super(0, 512, 1280, 227, textureImage);
    }

    @Override
    int getCurrentTextureIndex() {
        return currentTextureIndex;
    }

    @Override
    void setCurrentTextureIndex(int textureIndex) {
        currentTextureIndex = textureIndex;
    }

    public static BottomBackground create(Context context){
        Bitmap textureImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bgbottom);
        BottomBackground bottomBackground = new BottomBackground(textureImage);
        textureImage.recycle();
        return bottomBackground;
    }
}

