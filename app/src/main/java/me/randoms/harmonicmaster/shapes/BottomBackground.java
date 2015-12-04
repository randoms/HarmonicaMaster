package me.randoms.harmonicmaster.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import me.randoms.harmonicmaster.R;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class BottomBackground extends Sprite{

    private static int currentTextureIndex = -1;

    public BottomBackground(Bitmap textureImage){
        super(0, 512, 1280, 227, textureImage, null, true);
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

