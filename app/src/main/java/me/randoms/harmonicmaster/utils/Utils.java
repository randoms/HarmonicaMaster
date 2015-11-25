package me.randoms.harmonicmaster.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONException;
import me.randoms.harmonicmaster.json.JSONObject;
import android.content.Context;
import android.os.Environment;
import android.util.Log;


public final class Utils {
	
	public static int[] getTopSix(short[] input){
		int[] addedList = {-1,-1,-1,-1,-1,-1};
		
		for(int i=0;i<6;i++){
			short max = Short.MIN_VALUE;
			int max_index = -1;
			for(int j=0;j<input.length;j++){
				if(input[j]>max){
					// 娌℃湁宸茬粡娣诲姞
					boolean addedFlag = false;
					for(int k=0;k<6;k++){
						if(j == addedList[k])addedFlag = true;
					}
					if(!addedFlag){
						max_index = j;
						max = input[j];
					}
				}
				// 娣诲姞鍒癮ddedList
				addedList[i] = max_index;
			}
		}
		return addedList;
	}
	
	public static int[] getTopSix(double[] input){
		int[] addedList = {-1,-1,-1,-1,-1,-1};
		
		for(int i=0;i<6;i++){
			double max = Double.MIN_VALUE;
			int max_index = -1;
			for(int j=0;j<input.length;j++){
				if(input[j]>max){
					// 娌℃湁宸茬粡娣诲姞
					boolean addedFlag = false;
					for(int k=0;k<6;k++){
						if(j == addedList[k])addedFlag = true;
					}
					if(!addedFlag){
						max_index = j;
						max = input[j];
					}
				}
				// 娣诲姞鍒癮ddedList
				addedList[i] = max_index;
			}
		}
		return addedList;
	}
	
	
	public static int[] findPeaks(double[] input){
		// find the top 6 peaks
		int[] addedList = {-1,-1,-1,-1,-1,-1};
		
		for(int i=0;i<input.length;i++){
			// find peaks
			if(i !=0 && i != input.length-1){
				if(input[i-1]<input[i] && input[i]>input[i+1]){
					//check if this peaks if big enough
					boolean bigFlag = false;
					for(int j= addedList.length-1;j>=0;j--){
						if(addedList[j] == -1 || input[i]>input[addedList[j]]){
							bigFlag = true;
							// 缁х画鍜屾洿澶х殑璁板綍姣旇緝
						}else{
							if(bigFlag){
								// 娌℃湁姣旇繖涓ぇ锛屼絾鏄瘮涓嬩竴涓ぇ
								insert(addedList,j+1,i);
								bigFlag = false;
							}
						}
					}
					if(bigFlag){
						addedList = insert(addedList,0,i); //姣旀墍鏈夌殑閮藉ぇ
					}
				}
			}
		}
		
		return addedList;
	}
	
	public static int[] insert(int[] input,int index,int value){
		if(index == input.length-1){
			input[input.length-1] = value;
			return input;
		}
		
		for(int i=input.length-1;i>index;i--){
			input[i] = input[i -1];
		}
		input[index] = value;
		return input;
	}
	
	public static String arrayToString(int[] input){
		String res = "[";
		for(int i=0;i<input.length;i++){
			res = res + String.valueOf(input[i])+", ";
		}
		res = res +"]\n";
		return res;
	}
	
	/**
	 * @param input
	 * @return
	 * to String with a fixed length
	 */
	public static String fixToString(String input,int length){
		char[] res = new char[length];
		for(int i=0;i<length;i++){
			if(i < input.length()){
				res[i] = input.charAt(i);
			}else{
				res[i] = ' ';
			}
		}
		return String.valueOf(res);
	}
	
	public static void saveDB(int[][] soundDB,Context context){
		JSONArray db;
		String data="";
		try {
			db = new JSONArray(soundDB);
			data = db.toString(4); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File mDBFile = getStorageDir(context,"/db","soundDB.json");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(mDBFile);
			outputStream.write(data.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static int[][] loadDB(Context context){
		File soundDB = getStorageDir(context,"/db","soundDB.json");
		BufferedReader reader;
		String mLine;
		String soundJsonString = "";
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(soundDB)));
			mLine = reader.readLine();
			while (mLine != null) {
				soundJsonString += mLine;
				mLine = reader.readLine();
			}
			reader.close();
		}catch (Exception e){
			e.printStackTrace();
			return new int[88][];
		}
		JSONArray soundJson = new JSONArray(soundJsonString);
		int[][] res = new int[soundJson.length()][];
		for(int i=0;i<soundJson.length();i++){
			res[i] = new int[soundJson.getJSONArray(i).length()];
			for(int j=0;j<soundJson.getJSONArray(i).length();j++){
				res[i][j] = soundJson.getJSONArray(i).getInt(j);
			}
		}
		return res;
	}
	
	public static File getStorageDir(Context context, String dirname, String filename) {
	    // Get the directory for the app's private pictures directory. 
		String root = Environment.getExternalStorageDirectory().getPath();
		File myDir = new File(root+Statics.BASE_DIR+dirname);
		myDir.mkdirs();
	    File file = new File(myDir,filename);
	    Log.d("Randoms",file.getAbsolutePath());
	    return file;
	}
	
	private static JSONObject[] mMusicList; // add music file cache
	public static JSONObject[] getMusicSheets(Context context){
		if(mMusicList != null)return mMusicList;
		String root = Environment.getExternalStorageDirectory().getPath();
		File myDir = new File(root+Statics.BASE_DIR+"/music");
		myDir.mkdirs();
		File[] musicFiles = myDir.listFiles();
		JSONObject[] musicSheetList = new JSONObject[musicFiles.length];
		for(int i=0;i<musicFiles.length;i++){
			musicSheetList[i] = new JSONObject(readFile(musicFiles[i]));
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
