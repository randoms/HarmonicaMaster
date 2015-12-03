package me.randoms.harmonicmaster.models;

import com.leff.midi.event.ProgramChange;

/**
 * Created by randoms on 15-12-2.
 * In package me.randoms.harmonicmaster.models
 */
public class ProgramRange {
    public int start;
    public int end;
    public String name;

    public ProgramRange(String name, int start, int end){
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
