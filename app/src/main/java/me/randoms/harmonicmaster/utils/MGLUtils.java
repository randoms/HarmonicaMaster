package me.randoms.harmonicmaster.utils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.utils
 */
public class MGLUtils {
    /**
     * screen 1280x720 virtual resolution
     * transfrom single point
     * @return
     */
    public static float[] transformCordinate(float[] pos){
        float x = pos[0]/1280*2 - 1;
        float y = -pos[1]/720*2 +1;
        return new float[]{x,y};
    };

    /**
     * transform a list of points, leave z index away
     * @param pos
     * @return
     */
    public static float[] transformCordinateList(float[] pos){
        float [] res = new float[pos.length];
        for(int i =0;i<pos.length/3;i++){
            float[] newPos = transformCordinate(new float[]{pos[3*i], pos[3*i+1]});
            res[3*i] = newPos[0];
            res[3*i + 1] = newPos[1];
            res[3*i + 2] = 0f;
        }
        return res;
    }

    public static float transformCoordinateY(float y){
        return y/720*2;
    }
}
