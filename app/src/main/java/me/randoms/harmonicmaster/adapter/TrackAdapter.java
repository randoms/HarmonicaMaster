package me.randoms.harmonicmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leff.midi.event.meta.Text;

import org.joda.time.LocalDateTime;

import java.io.File;

import me.randoms.harmonicmaster.GameActivity;
import me.randoms.harmonicmaster.R;
import me.randoms.harmonicmaster.SelectTrackActivity;
import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONObject;
import me.randoms.harmonicmaster.models.Tracks;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;

/**
 * Created by randoms on 15-12-2.
 * In package me.randoms.harmonicmaster.adapter
 */
public class TrackAdapter extends BaseAdapter
        implements AdapterView.OnItemClickListener {

    private Tracks[] mTracks;
    private Context context;

    public TrackAdapter(Context mContext,Tracks[] trackses){
        mTracks = trackses;
        context = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTracks.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mTracks[position];
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
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tracks, parent, false);
        }
        TextView trackName = (TextView) mView.findViewById(R.id.trackname);
        TextView instrument = (TextView) mView.findViewById(R.id.instrument);
        TextView soundNum = (TextView) mView.findViewById(R.id.soundNum);

        trackName.setText(mTracks[position].trackName);
        instrument.setText(Statics.getProgramName(mTracks[position].instrumentId));
        soundNum.setText(mTracks[position].soundNum + "个音符");
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent mIntent = new Intent(parent.getContext(),GameActivity.class);
        String uuid = ((SelectTrackActivity)context).getIntent().getStringExtra("uuid");
        JSONObject[] musicList = Utils.getMusicSheets(context);
        for(int i=0;i< musicList.length;i++){
            if(musicList[i].getString("id").equals(uuid)){
                String filename = ((SelectTrackActivity)context).getIntent().getStringExtra("midiName");
                String root = Environment.getExternalStorageDirectory().getPath();
                JSONObject midiData =  Utils.getMidiTracks(new File(root+ Statics.BASE_DIR+"/midi/" + filename), position);
                musicList[i].put("sounds", midiData.getJSONArray("sounds"));
            }
        }
        mIntent.putExtra("uuid", uuid);
        context.startActivity(mIntent);
    }


}
