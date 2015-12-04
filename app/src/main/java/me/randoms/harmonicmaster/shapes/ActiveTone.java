package me.randoms.harmonicmaster.shapes;

import android.graphics.Bitmap;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.shapes
 */
public class ActiveTone extends Sprite{

    private static int currentTextureIndex = -1;
    public ActiveTone(Bitmap textureImage, float left, float top){
        super(left, top, 64, 128,textureImage);
    }

    @Override
    int getCurrentTextureIndex() {
        return currentTextureIndex;
    }

    @Override
    void setCurrentTextureIndex(int textureIndex) {
        currentTextureIndex = textureIndex;
    }
}
