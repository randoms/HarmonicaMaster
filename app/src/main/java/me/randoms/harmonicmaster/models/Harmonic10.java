package me.randoms.harmonicmaster.models;

import java.util.ArrayList;

import me.randoms.harmonicmaster.shapes.Tone;

/**
 * Created by randoms on 15-11-30.
 * In package me.randoms.harmonicmaster.models
 */
public class Harmonic10 {

    // C C# D D# E F F# G G# A A# B
    public static ToneModel[] mToneList = {
            null, null, null, null, null, null, null, null, null, null, null, null,
            new ToneModel(0, ToneModel.OUT), new ToneModel(0, ToneModel.IN), new ToneModel(0, ToneModel.IN), new ToneModel(0, ToneModel.OUT),
            new ToneModel(1, ToneModel.OUT), new ToneModel(1, ToneModel.IN), new ToneModel(1, ToneModel.IN), new ToneModel(1, ToneModel.IN), new ToneModel(1, ToneModel.OUT),
            new ToneModel(2, ToneModel.IN), new ToneModel(2, ToneModel.IN), new ToneModel(2, ToneModel.IN),

            new ToneModel(3, ToneModel.OUT), new ToneModel(3, ToneModel.IN), new ToneModel(3, ToneModel.IN), new ToneModel(3, ToneModel.OUT),
            new ToneModel(4, ToneModel.OUT), new ToneModel(4, ToneModel.IN), new ToneModel(4, ToneModel.OUT),
            new ToneModel(5, ToneModel.OUT), new ToneModel(5, ToneModel.IN), new ToneModel(5, ToneModel.IN), new ToneModel(5, ToneModel.OUT),
            new ToneModel(6, ToneModel.IN),

            new ToneModel(6, ToneModel.OUT), new ToneModel(6, ToneModel.OUT),
            new ToneModel(7, ToneModel.IN), new ToneModel(7, ToneModel.IN), new ToneModel(7, ToneModel.OUT),
            new ToneModel(8, ToneModel.IN), new ToneModel(8, ToneModel.IN), new ToneModel(8, ToneModel.OUT), new ToneModel(8, ToneModel.OUT),
            new ToneModel(9, ToneModel.IN), new ToneModel(9, ToneModel.IN), new ToneModel(9, ToneModel.IN),

            new ToneModel(9, ToneModel.OUT), new ToneModel(9, ToneModel.OUT),
            null, null, null, null, null, null, null, null, null, null
    };
}
