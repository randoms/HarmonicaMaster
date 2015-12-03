package me.randoms.harmonicmaster.models;

/**
 * Created by randoms on 15-12-2.
 * In package me.randoms.harmonicmaster.models
 */
public class Tracks {
    public String trackName;
    public int soundNum;
    public int instrumentId;

    public Tracks (String trackName, int soundNum, int instrumentId){
        this.trackName = trackName;
        this.soundNum = soundNum;
        this.instrumentId = instrumentId;
    }
}
