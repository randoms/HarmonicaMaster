package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import me.randoms.harmonicmaster.R;

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
        super(418, 192, 512, 64, textureBitmap, null, true);
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
