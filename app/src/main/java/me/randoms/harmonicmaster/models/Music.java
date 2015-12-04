package me.randoms.harmonicmaster.models;

import java.util.ArrayList;

/**
 * Created by randoms on 15-12-3.
 * In package me.randoms.harmonicmaster.models
 */
public class Music {
    private String author;
    private long time;
    private String name;
    private String id;
    private ArrayList<Sound> sounds;
    private long length;
    private ArrayList<Track> tracks;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setTracks(ArrayList<Track> tracks){
        this.tracks = tracks;
    }

    public ArrayList<Track> getTracks(){
        return this.tracks;
    }
}
