package me.randoms.harmonicmaster.callback;

abstract public class ProcessAudioHandler {
	
	abstract public void onProcess(short[] buffer);
	abstract public void onStop();
	abstract public void onError(Throwable e);
}
