package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

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
public class Title extends Sprite {

    private static int currentTextureIndex = -1;

    public Title(Bitmap textureBitmap){
        /**
         * left 418
         * top 192
         * width 512
         * height 64
         */
        super(418, 192, 512, 64, textureBitmap);
        textureBitmap.recycle();
    }

    @Override
    int getCurrentTextureIndex() {
        return currentTextureIndex;
    }

    @Override
    void setCurrentTextureIndex(int textureIndex) {
        currentTextureIndex = textureIndex;
    }

    public static Title create(Context context){
        Bitmap textureBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.title);
        Title res = new Title(textureBitmap);
        textureBitmap.recycle();
        return res;
    }
}
