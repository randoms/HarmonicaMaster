package me.randoms.harmonicmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.SelectTrackActivity;
import me.randoms.harmonicmaster.models.Music;

public class MusicListAdapter extends BaseAdapter 
	implements OnItemClickListener {
	
	private ArrayList<Music> musicList;
	private Context context;
	
	public MusicListAdapter(Context mContext,ArrayList<Music> mMusicList){
		musicList = mMusicList;
		if(mMusicList == null){
			musicList = new ArrayList<>();
		}
		context = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musicList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return musicList.get(position);
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
		title.setText(musicList.get(position).getName());
		author.setText(musicList.get(position).getAuthor());
		LocalDateTime date = new LocalDateTime(musicList.get(position).getTime());
		String dateString = String.valueOf(date.toString("YY-MM-dd HH:MM:SS"));
		
		createTime.setText(dateString);
		long musicLength = musicList.get(position).getLength();
		String musicLengthStr = String.valueOf(musicLength/1000/60)+"m "+String.valueOf(musicLength/1000%60) + "s";
		length.setText(musicLengthStr);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
        Intent mIntent;
        if(musicList.get(position).getTracks() != null){
            // midi file
            mIntent = new Intent(parent.getContext(), SelectTrackActivity.class);
            mIntent.putExtra("midiName", musicList.get(position).getName());
        }else{
            mIntent = new Intent(parent.getContext(),GameActivity.class);
        }

		mIntent.putExtra("uuid", musicList.get(position).getId());
		context.startActivity(mIntent);
	}
	
	
}
