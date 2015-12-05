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
public class TrackAdapter extends BaseAdapter {

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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
        instrument.setText(Statics.getProgramName(mTracks.get(position).instrumentId) + mTracks.get(position).instrumentId);
        soundNum.setText(mTracks.get(position).soundNum + "个音符");
        View lisetenGame = mView.findViewById(R.id.listen);
        View playGame = mView.findViewById(R.id.play);
        if(mTracks.get(position).soundNum == 0){
            lisetenGame.setVisibility(View.INVISIBLE);
            playGame.setVisibility(View.INVISIBLE);
        }else{
            lisetenGame.setVisibility(View.VISIBLE);
            playGame.setVisibility(View.VISIBLE);
        }

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(parent.getContext(),GameActivity.class);
                playGame(mIntent, position);
            }
        });

        lisetenGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(parent.getContext(),GameActivity.class);
                viewGame(mIntent, position);
            }
        });
        return mView;
    }

    private void playGame(Intent mIntent, int position){
        String uuid = ((SelectTrackActivity)context).getIntent().getStringExtra("uuid");
        ArrayList<Music> musicList = Utils.getMusicSheets();
        for(Music music : musicList){
            if(music.getId().equals(uuid)){
                String filename = ((SelectTrackActivity)context).getIntent().getStringExtra("midiName");
                String root = Environment.getExternalStorageDirectory().getPath();
                Music midiData =  Midi.getMidiTracks(new File(root+ Statics.BASE_DIR+"/midi/" + filename), position, true);
                mIntent.putExtra("midFilePath", root+ Statics.BASE_DIR+"/midi/" + filename);
                mIntent.putExtra("trackIndex", position);
                if(midiData != null)
                    music.setSounds(midiData.getSounds());
                break;
            }
        }
        mIntent.putExtra("uuid", uuid);
        context.startActivity(mIntent);
    }

    private void viewGame(Intent mIntent, int position){
        mIntent.putExtra("playMusic", true);
        playGame(mIntent, position);
    }


}
