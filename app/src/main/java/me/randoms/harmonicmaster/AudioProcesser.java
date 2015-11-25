package me.randoms.harmonicmaster;

import me.randoms.harmonicmaster.utils.Fft;
import me.randoms.harmonicmaster.utils.Utils;
import android.util.Log;


// use static method to increase efficiency
public final class AudioProcesser {
	static int a =0;
	static short[] soundData;
	static double[] fftdata; // input data and real part res
	static double[] FFTImage; // image part fft res
	static double[] mFFTBytes; // final fft res
	static int bigSoundCount = 0;
	static int mSpectrumNum = 512;
	static Fft mFFT = new Fft(1024);
	static String res = "";
	static int[][] soundDb = new int[88][6]; // max 88 sound, the same as piano key numbers
	static int[] lastTopSix;
	static boolean blowFlag = false;
	
	
	// static variables
	static int staticCount = 0;
	static int staticId = -1;
	static short[] staticRecord = new short[mSpectrumNum]; // Frequency and count
	
	// recognize variables
	static int currentSoundId = -1; // -1 means need to recognize again
	static boolean startRecognize = false; // only start recognize when this is true
	
	//process lock
	static boolean processFlag = false;
	
	public static boolean getBlowFlag(){
		return blowFlag;
	}
	
	public static double[] getFFT(){
		return mFFTBytes;
	}
	
	public static short[] getSoundData(){
		return soundData;
	}
	
	public static String getRes(){
		return res;
	}
	
	public static void beginRecognize(){
		startRecognize = true;
	}
	
	public static void stopRecognize(){
		startRecognize = false;
	}
	
	public static void setInsertSound(int soundId){
		staticId = soundId;
	}
	
	public static boolean isStatic(){
		if(staticId != -1){
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void process(short[] fft){
		processFlag = true;
		res = "";
		soundData = fft;
		// byte to double
		double[] fftdata = new double[fft.length];
		mFFTBytes = new double[fft.length];
		FFTImage = new double[fft.length];
		bigSoundCount = 0;
		for(int i = 0;i<fft.length;i++){
			if(fft[i]>1024 || fft[i] <-1024){
				bigSoundCount ++;
			}
			fftdata[i] = (double)fft[i];
			FFTImage[i] = 0;
		}
		mFFT.fft(fftdata, FFTImage);
		for(int i=0;i<mSpectrumNum;i++){
			mFFTBytes[i] = Math.hypot(fftdata[i],FFTImage[i]);
		}

		
		// find the top 6 frequency
		int[] topSix = Utils.findPeaks(mFFTBytes);
		for(int i=0;i<topSix.length;i++){
			res = res + "freq:"+Utils.fixToString(String.valueOf(topSix[i]), 6);//+"value:"+String.valueOf(fft[topSix[i]])+ " ";
		}
		
		// check if there is a blow
		// if more than 3 freq are the same
		int equalCount = 0;
		if(lastTopSix == null){
			lastTopSix = topSix;
		}else{
			for(int i=0;i<6;i++){
				for(int j=0; j<6;j++){
					if(lastTopSix[i] == topSix[j]){
						equalCount ++;
					}
				}
			}
		}
		
		//the first time equal
		if(equalCount>=3 && bigSoundCount >100){
			blowFlag = true;
		}else if(blowFlag){
			blowFlag = false;
			currentSoundId = -1;
		}
		
		if(blowFlag){
			res = res + Utils.fixToString("blow", 6);
		}else{
			res = res + Utils.fixToString("no", 6);
		}
		
		// update lastSix
		lastTopSix = topSix;
		
		
		// fake data
		/*for(int i=0;i<6;i++){
			lastTopSix[i] = i*2+10+staticId; 
		}
		
		blowFlag = true;*/
		
		// static task begin
		if(staticId != -1 && blowFlag){
			insertNewSound();
		}
		
		// recognize work start
		if(startRecognize && currentSoundId == -1 && blowFlag == true){
			recognizeWork();
		}
		processFlag = false;
	}
	
	/**
	 * @param id
	 * analysis the sound and record it's top six frequency to database
	 */
	public static void insertNewSound(){
		if(staticId == -1)return;
		if(staticCount<1000){
			for(int i=0;i<6;i++){
				staticRecord[lastTopSix[i]] += 1;
				Log.d("AudioProcesser",String.valueOf(staticCount));
				staticCount ++;
			}
		}else{
			// static over
			// get top six record
			staticCount =0;
			soundDb[staticId] = Utils.getTopSix(staticRecord);
			staticRecord = new short[mSpectrumNum]; // clear record
			staticId = -1;
		}
	}
	
	private static void recognizeWork(){
		for(int i=5;i>=0;i--){
			for(int j=0;j<88;j++){
				int equalCount = 0;
				for(int k=0;k<6;k++){
					for(int l=0;l<6;l++){
						if(soundDb[j][k] -1 <=lastTopSix[l] && soundDb[j][k] +1 >=lastTopSix[l]){
							equalCount ++;
						}
					}
				}
				if(equalCount > i){
					currentSoundId = j;
					Log.d("RandomsEqual",String.valueOf(equalCount));
					return;
				}
			}
		}
	}
	
	public static int getSoundName(){
		return currentSoundId;
	}
	
	public static boolean isProcessing(){
		return processFlag;
	}
	
}
