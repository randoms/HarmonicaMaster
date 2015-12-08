package me.randoms.harmonicmaster.models;

import me.randoms.harmonicmaster.shapes.Tone;

/**
 * Created by randoms on 15-12-8.
 * In package me.randoms.harmonicmaster.models
 */
public class Harmonic24 {
    // C C# D D# E F F# G G# A A# B
    public static ToneModel[] mToneList = {
            null, null, null, null, null, null, null, new ToneModel(0, ToneModel.OUT), null, null, null, null,

            new ToneModel(1, ToneModel.IN), null, new ToneModel(0, ToneModel.IN),null,
            new ToneModel(2, ToneModel.OUT), new ToneModel(1, ToneModel.IN), null,
            new ToneModel(3, ToneModel.OUT), null, new ToneModel(2, ToneModel.IN), null,
            new ToneModel(3, ToneModel.IN),

            new ToneModel(4, ToneModel.OUT), null, new ToneModel(4, ToneModel.IN), null,
            new ToneModel(5, ToneModel.OUT), new ToneModel(5, ToneModel.IN), null,
            new ToneModel(6, ToneModel.OUT), null, new ToneModel(6, ToneModel.IN), null,
            new ToneModel(7, ToneModel.IN),

            new ToneModel(7, ToneModel.OUT), null, new ToneModel(8, ToneModel.IN), null,
            new ToneModel(8, ToneModel.OUT), new ToneModel(9, ToneModel.IN), null,
            new ToneModel(9, ToneModel.OUT), null, new ToneModel(10, ToneModel.IN), null,
            new ToneModel(11, ToneModel.IN),

            new ToneModel(10, ToneModel.OUT), null, null, null,
            new ToneModel(11, ToneModel.OUT), null, null,
            null, null, null, null,
            null,

    };

    public static int[] sweetAreas = new int[]{
            12, 14,
            16, 19,
            19, 23,
            24, 26,
            28, 29,
            31, 33,
            36, 35,
            38, 40,
            41, 43,
            45, 48
    };
}
