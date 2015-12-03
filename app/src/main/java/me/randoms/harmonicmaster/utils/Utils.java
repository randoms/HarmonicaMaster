package me.randoms.harmonicmaster.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONException;
import me.randoms.harmonicmaster.json.JSONObject;
import me.randoms.harmonicmaster.models.Tracks;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;


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

        // load midi files
        File midiDir = new File(root+Statics.BASE_DIR+"/midi");
        midiDir.mkdirs();
        File[] midiFiles = midiDir.listFiles();
        if(musicFiles.length + midiFiles.length == 0)
            return new JSONObject[]{};
        JSONObject[] musicSheetList = new JSONObject[musicFiles.length + midiFiles.length];


        for(int i=0;i<musicFiles.length;i++){
            musicSheetList[i] = new JSONObject(readFile(musicFiles[i]));
        }

        for(int i=0;i<midiFiles.length;i++){
            musicSheetList[i + musicFiles.length] = midiToJson(midiFiles[i]);
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

	public static JSONObject midiToJson(File file){
		MidiFile midiFile = null;
		try {
			midiFile = new MidiFile(file);
		}catch (IOException e){
			e.printStackTrace();
			return null;
		}
		JSONObject musicData = new JSONObject();
		musicData.put("author", "MidiFile");
		musicData.put("time", file.lastModified());
		musicData.put("name", file.getName());
		musicData.put("id", UUID.randomUUID().toString());
		MidiTrack track0 = midiFile.getTracks().get(0);
		float bpm = 120;
        Iterator<MidiEvent> it0 = track0.getEvents().iterator();
        while (it0.hasNext()){
            MidiEvent event = it0.next();
            if(event instanceof Tempo){
                Tempo tempo = (Tempo)event;
                bpm = tempo.getBpm();
            }
        }
        float tickTime = 60*1000/(midiFile.getResolution() * bpm);
		long length = (long)(midiFile.getLengthInTicks() * tickTime);
		musicData.put("length", length);
		JSONArray soundList = new JSONArray();
        musicData.put("sounds", soundList);
        JSONArray trackList = new JSONArray();
        // add track info
        ArrayList<MidiTrack> tracks = midiFile.getTracks();
        int trackCount = 0;
        for(MidiTrack mTrack:tracks){
            int programNum = 0;
            int soundNum = 0;
            Iterator<MidiEvent> lt = mTrack.getEvents().iterator();
            while (lt.hasNext()){
                MidiEvent event = lt.next();
                if(event instanceof ProgramChange){
                    programNum = ((ProgramChange) event).getProgramNumber() + 1;
                }
                if(event instanceof NoteOn){
                    soundNum ++;
                }
            }
            trackList.put(new Tracks("音轨" + trackCount, soundNum, programNum));
            trackCount ++;
        }
        musicData.put("tracks", trackList);
		return musicData;
	}

    public static JSONObject getMidiTracks(File file, int trackNum){
        MidiFile midiFile = null;
        try {
            midiFile = new MidiFile(file);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        JSONObject musicData = new JSONObject();
        musicData.put("author", "MidiFile");
        musicData.put("time", file.lastModified());
        musicData.put("name", file.getName());
        musicData.put("id", UUID.randomUUID().toString());
        MidiTrack track0 = midiFile.getTracks().get(0);
        float bpm = 120;
        Iterator<MidiEvent> it0 = track0.getEvents().iterator();
        while (it0.hasNext()){
            MidiEvent event = it0.next();
            if(event instanceof Tempo){
                Tempo tempo = (Tempo)event;
                bpm = tempo.getBpm();
            }
        }
        float tickTime = 60*1000/(midiFile.getResolution() * bpm);
        long length = (long)(midiFile.getLengthInTicks() * tickTime);

        musicData.put("length", length);

        JSONArray soundList = new JSONArray();
        MidiTrack track1 = midiFile.getTracks().get(trackNum);
        Iterator<MidiEvent> it1 = track1.getEvents().iterator();
        // get average tone
        int totalSound = 0;
        int totalTone = 0;
        while (it1.hasNext()){
            MidiEvent event = it1.next();
            if(event instanceof NoteOn){
                NoteOn noteOn = (NoteOn)event;
                totalSound ++;
                totalTone += noteOn.getNoteValue();
            }
        }
        int distanceFormCenter = 0;
        int distanceToAdd = 0;
        if(totalSound != 0){
            distanceFormCenter = totalTone / totalSound;
            distanceToAdd = -(int)(Math.floor((distanceFormCenter - 72.0)/12))*12; //转调, 72是中心C
        }
        it1 = track1.getEvents().iterator();
        boolean isLastStillOn = false;
        while (it1.hasNext()){
            //TODO 这里需要修改
            MidiEvent event = it1.next();
            if(event instanceof NoteOff || event instanceof NoteOn){

                if(isLastStillOn){
                    JSONObject lastNote = (JSONObject) soundList.get(soundList.length() - 1);
                    lastNote.put("end", (long)(event.getTick() * tickTime));
                }

                if(event instanceof NoteOn && ((NoteOn)event).getVelocity() != 0){
                    NoteOn noteOn = (NoteOn)event;
                    JSONObject soundData = new JSONObject();
                    soundData.put("name", noteOn.getNoteValue() - 47 + distanceToAdd);
                    long start = (long)(noteOn.getTick() * tickTime);
                    soundData.put("start", start);
                    soundList.put(soundData);
                    isLastStillOn = true;
                }else {
                    isLastStillOn = false;
                }

                /*NoteOff noteOff = (NoteOff)event;
                JSONObject soundData = new JSONObject();
                soundData.put("name", noteOff.getNoteValue() - 47 + distanceToAdd);
                Log.d("name", (noteOff.getNoteValue() - 47 + distanceToAdd) + "");
                long start = (long)(noteOff.getTick() * tickTime);
                long soundLength = (long)(noteOff.getDelta() * tickTime);
                Log.d("length", soundLength + "");
                soundData.put("start", start);
                soundData.put("end", start + soundLength);
                soundList.put(soundData);*/
            }
        }
        musicData.put("sounds", soundList);
        return musicData;
    }
}
