package me.randoms.harmonicmaster.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.SelectTrackActivity;
import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.Track;
import me.randoms.harmonicmaster.utils.Midi;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;

/**
 * Created by randoms on 15-12-2.
 * In package me.randoms.harmonicmaster.adapter
 */
public class TrackAdapter extends BaseAdapter
        implements AdapterView.OnItemClickListener {

    private ArrayList<Track> mTracks;
    private Context context;

    public TrackAdapter(Context mContext,ArrayList<Track> trackses){
        mTracks = trackses;
        context = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTracks.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View mView;
        if(convertView != null){
            mView = convertView;
        }else{
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tracks, parent, false);
        }
        TextView trackName = (TextView) mView.findViewById(R.id.trackname);
        TextView instrument = (TextView) mView.findViewById(R.id.instrument);
        TextView soundNum = (TextView) mView.findViewById(R.id.soundNum);

        trackName.setText(mTracks.get(position).trackName);
        instrument.setText(Statics.getProgramName(mTracks.get(position).instrumentId));
        soundNum.setText(mTracks.get(position).soundNum + "个音符");
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent mIntent = new Intent(parent.getContext(),GameActivity.class);
        String uuid = ((SelectTrackActivity)context).getIntent().getStringExtra("uuid");
        ArrayList<Music> musicList = Utils.getMusicSheets();
        for(Music music : musicList){
            if(music.getId().equals(uuid)){
                String filename = ((SelectTrackActivity)context).getIntent().getStringExtra("midiName");
                String root = Environment.getExternalStorageDirectory().getPath();
                Music midiData =  Midi.getMidiTracks(new File(root+ Statics.BASE_DIR+"/midi/" + filename), position);
                if(midiData != null)
                    music.setSounds(midiData.getSounds());
                break;
            }
        }
        mIntent.putExtra("uuid", uuid);
        context.startActivity(mIntent);
    }


}
