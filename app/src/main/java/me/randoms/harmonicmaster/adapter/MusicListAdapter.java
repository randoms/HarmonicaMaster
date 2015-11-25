package me.randoms.harmonicmaster.adapter;

import me.randoms.harmonicmaster.PlayActivity;
import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.json.JSONObject;

import org.joda.time.LocalDateTime;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter 
	implements OnItemClickListener {
	
	private JSONObject[] musicList;
	private Context context;
	
	public MusicListAdapter(Context mContext,JSONObject[] mMusicList){
		musicList = mMusicList;
		context = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musicList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return musicList[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View mView;
		if(convertView != null){
			mView = convertView;
		}else{
			mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sound_list, parent, false);
		}
		TextView title = (TextView)mView.findViewById(R.id.title);
		TextView author = (TextView)mView.findViewById(R.id.author);
		TextView createTime = (TextView)mView.findViewById(R.id.createTime);
		TextView length = (TextView)mView.findViewById(R.id.length);
		title.setText(musicList[position].getString("name"));
		author.setText(musicList[position].getString("author"));
		LocalDateTime date = new LocalDateTime(musicList[position].getLong("time"));
		String dateString = String.valueOf(date.toString("YY-MM-dd HH:MM:SS"));
		
		createTime.setText(dateString);
		long musicLength = musicList[position].getLong("length");
		String musicLengthStr = String.valueOf(musicLength/1000/60)+"m "+String.valueOf(musicLength/1000%60) + "s";
		length.setText(musicLengthStr);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent mIntent = new Intent(parent.getContext(),PlayActivity.class);
		mIntent.putExtra("uuid", musicList[position].getString("id"));
		context.startActivity(mIntent);
	}
	
	
}
