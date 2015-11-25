package me.randoms.harmonicmaster.adapter;


import me.randoms.harmonicmaster.MainActivity;
import me.randoms.harmonicmaster.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SoundBtnAdapter extends BaseAdapter implements OnItemClickListener{
	private int[] keys;
	
	private Context mContext;
	
	public SoundBtnAdapter(Context context){
		mContext = context;
		keys = new int[88];
		for(int i=0;i<88;i++){
			keys[i] = i;
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return keys.length;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Log.d("Randoms","Clicked");
		Intent mIntent = new Intent(mContext,MainActivity.class);
		mIntent.putExtra("doRecoginze", false);
		mIntent.putExtra("insertSound", position);
		mContext.startActivity(mIntent);
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return keys[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return keys[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		// TODO Auto-generated method stub
		TextView mButton;
		View mView;
		if(convertView != null){
			mView = convertView;
		}else{
			mView = LayoutInflater.from(container.getContext()).inflate(
					R.layout.adapter_sound_btn, container, false);
		}
		mButton = (TextView)mView.findViewById(R.id.sound_btn);
		mButton.setText(String.valueOf(keys[position]+1));
		return mView;
	}
}
