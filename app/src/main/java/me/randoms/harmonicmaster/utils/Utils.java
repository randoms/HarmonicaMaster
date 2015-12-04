package me.randoms.harmonicmaster.utils;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.randoms.harmonicmaster.models.Music;


public final class Utils {

	
	private static ArrayList<Music> mMusicList; // add music file cache
	public static ArrayList<Music> getMusicSheets(){
		if(mMusicList != null)return mMusicList;
		String root = Environment.getExternalStorageDirectory().getPath();
		File myDir = new File(root+Statics.BASE_DIR+"/music");
		myDir.mkdirs();
		File[] musicFiles = myDir.listFiles();
        // load midi files
        File midiDir = new File(root+Statics.BASE_DIR+"/midi");
		midiDir.mkdirs();

        File[] midiFiles = midiDir.listFiles();
        if(musicFiles.length + midiFiles.length == 0)
            return new ArrayList<Music>(){};
        ArrayList<Music> musicSheetList = new ArrayList<>();
		Gson gson = new Gson();
		for(File musicFile : musicFiles){
			musicSheetList.add(gson.fromJson(readFile(musicFile), Music.class));
		}
		for(File midiFile : midiFiles){
			musicSheetList.add(Midi.midiToJson(midiFile));
		}
		mMusicList = musicSheetList;
		return musicSheetList;
	}
	
	public static String readFile(File mFile){
		String res = "";
		String mLine;
		BufferedReader mReader;
		try{
			mReader = new BufferedReader(new InputStreamReader(new FileInputStream(mFile)));
			mLine = mReader.readLine();
			while(mLine != null){
				res += mLine;
				mLine = mReader.readLine();
			}
			mReader.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
