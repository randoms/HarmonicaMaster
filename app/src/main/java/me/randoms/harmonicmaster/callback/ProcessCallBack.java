package me.randoms.harmonicmaster.callback;

abstract public class ProcessCallBack {
	abstract public void onStart();
	abstract public void onProcess(Class<?> obj);
	abstract public void onStop();
	abstract public void onError(Throwable e);
}
