package me.randoms.harmonicmaster.models;

/**
 * Created by randoms on 15-12-2.
 * In package me.randoms.harmonicmaster.models
 */
public class Track {
    public String trackName;
    public int soundNum;
    public int instrumentId;

    public Track(String trackName, int soundNum, int instrumentId){
        this.trackName = trackName;
        this.soundNum = soundNum;
        this.instrumentId = instrumentId;
    }
}
