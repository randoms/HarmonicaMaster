package me.randoms.harmonicmaster;

import me.randoms.harmonicmaster.callback.ProcessAudioHandler;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

public class AudioStream extends Thread{
	private boolean stopped = false;

    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
	private ProcessAudioHandler mProcessHandler;
    public AudioStream(ProcessAudioHandler handler)
    { 
    	mProcessHandler = handler;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        start();
    }

    @Override
    public void run()
    { 
        Log.i("Audio", "Running Audio Thread");
        AudioRecord recorder = null;
        int N = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        short[]   buffers  = new short[N];

        try
        {
            
            Log.d("RandomsN",String.valueOf(N));
            recorder = new AudioRecord(AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N*2);
            recorder.startRecording();
            /*
             * Loops until something outside of this thread stops it.
             * Reads the data from the recorder and writes it to the audio track for playback.
             */
            while(!stopped)
            {
            	short[] buffer = buffers;
                recorder.read(buffer,0,buffer.length);
                mProcessHandler.onProcess(buffer);
            }
        }
        catch(Throwable x)
        { 
            Log.w("Audio", "Error reading voice audio", x);
            mProcessHandler.onError(x);
        }
        /*
         * Frees the thread's resources after the loop completes so that it can be run again
         */
        finally
        { 
        	if(recorder != null){
        		recorder.stop();
        		recorder.release();
        	}
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void close()
    { 
         stopped = true;
         mProcessHandler.onStop();
    }

}
