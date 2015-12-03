package me.randoms.harmonicmaster.utils;

/**
 * Created by randoms on 15-11-26.
 * In package me.randoms.harmonicmaster.utils
 */
public class LogUtils {
    public static<T> String tostring( T[] data){
        String res = "[ ";
        for(int i=0;i<data.length;i++){
            res += data[i].toString() + ", ";
        }
        return res + "]";
    }

    public static<T> String tostring( float[] data){
        String res = "[ ";
        for(int i=0;i<data.length;i++){
            res += Float.toString(data[i]) + ", ";
        }
        return res + "]";
    }
}
