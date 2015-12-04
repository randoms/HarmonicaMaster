package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import me.randoms.harmonicmaster.R;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class Background extends Sprite {

    private static int currentTextureIndex = -1;

    public Background(Bitmap textureImage){
        super(0, 30, 1280, 512, textureImage, null, true);
    }

    @Override
    int getCurrentTextureIndex() {
        return currentTextureIndex;
    }

    @Override
    void setCurrentTextureIndex(int textureIndex) {
        currentTextureIndex = textureIndex;
    }

    public static Background create(Context context){
        Bitmap textureImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bg);
        Background res = new Background(textureImage);
        textureImage.recycle();
        return res;
    }
}
