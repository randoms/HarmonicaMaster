package me.randoms.harmonicmaster.models;

/**
 * Created by randoms on 15-11-30.
 * In package me.randoms.harmonicmaster.models
 */
public class ToneModel {

    public ToneModel(int positon, boolean isD){
        this.positon = positon;
        this.isD = isD;
    }

    public static boolean OUT = false;
    public static boolean IN = true;

    public int positon;
    public boolean isD = false;
}
