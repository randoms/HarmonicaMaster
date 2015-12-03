package me.randoms.harmonicmaster;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;

import me.randoms.harmonicmaster.adapter.TrackAdapter;
import me.randoms.harmonicmaster.json.JSONArray;
import me.randoms.harmonicmaster.json.JSONObject;
import me.randoms.harmonicmaster.models.Tracks;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;

public class SelectTrackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_track);

        ListView selectTrackList = (ListView) findViewById(R.id.selectTrackList);
        String filename = getIntent().getStringExtra("midiName");
        String root = Environment.getExternalStorageDirectory().getPath();
        JSONObject midiData =  Utils.midiToJson(new File(root+ Statics.BASE_DIR+"/midi/" + filename));
        JSONArray trackList = midiData.getJSONArray("tracks");
        Tracks[] mTracks = new Tracks[trackList.length()];
        for(int i = 0;i < trackList.length(); i++){
            mTracks[i] = (Tracks)trackList.get(i);
        }
        TrackAdapter trackAdapter = new TrackAdapter(this, mTracks);
        selectTrackList.setAdapter(trackAdapter);
        selectTrackList.setOnItemClickListener(trackAdapter);
    }
}
