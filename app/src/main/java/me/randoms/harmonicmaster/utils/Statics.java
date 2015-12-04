package me.randoms.harmonicmaster.utils;

import me.randoms.harmonicmaster.models.ProgramRange;

public final class Statics {

	public static String BASE_DIR = "/Randoms/HarmonicMaster";
	// from G4 to A6
    // C C# D D# E F F# G G# A A# B
	public static float [] FREQS = new float[]{
            130.81f, 138.59f, 146.83f, 155.56f, 164.81f, 174.61f, 185.00f, 196.00f, 207.65f, 220.00f, 233.08f, 246.94f,
            261.63f, 277.18f, 293.66f, 311.13f, 329.63f, 349.23f, 369.99f, 392.00f, 415.30f, 440.00f, 466.16f, 493.88f,
            523.25f, 554.37f, 587.33f, 622.25f, 659.26f, 698.46f, 739.99f, 783.99f, 830.61f, 880.00f, 932.33f, 987.77f, //center
            1046.5f, 1108.7f, 1174.7f, 1244.5f, 1318.5f, 1396.9f, 1480.0f, 1568.0f, 1661.2f, 1760.0f, 1864.7f, 1975.5f,
            2093.0f, 2217.5f, 2349.3f, 2489.0f, 2637.0f, 2793.8f, 2960.0f, 3136.0f, 3322.4f, 3520.0f, 3729.3f, 3951.1f,
	};

    public static String HARMONIC_TYPE = "harmonic_type";
    public static int TEN = 10;
    public static int TWELVE = 12;
    public static int TWENTY_FOUR = 24;
    public static String FIRST_TIME_FLAG = "first_time_start_flag";

    public static String[] TONE_NAME = {
            "1", "1#", "2", "2#", "3", "4", "4#", "5", "5#", "6", "6#", "7"
    };

    public static ProgramRange[] Programs = {
            new ProgramRange("钢琴", 1, 8), new ProgramRange("固定音高敲击乐器", 9, 16), new ProgramRange("风琴", 17, 24),
            new ProgramRange("吉他", 25, 32), new ProgramRange("贝斯", 33, 40), new ProgramRange("弦乐器", 41, 48),
            new ProgramRange("合奏", 49, 56), new ProgramRange("铜管乐器", 57, 65), new ProgramRange("簧乐器", 65, 72),
            new ProgramRange("吹管乐器", 73, 80), new ProgramRange("合成音主旋律", 81, 88), new ProgramRange("合成音和弦衬底", 89, 96),
            new ProgramRange("合成音效果", 97, 104), new ProgramRange("民族乐器", 105, 112), new ProgramRange("打击乐器", 113, 120),
            new ProgramRange("特殊 音效", 121, 128)
    };

    public static String getProgramName(int num){
        for(ProgramRange range:Programs){
            if(num >= range.start && num <= range.end)
                return range.name;
        }
        return "未知乐器";
    }
}
