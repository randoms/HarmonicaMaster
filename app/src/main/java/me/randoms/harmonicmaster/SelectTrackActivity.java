package me.randoms.harmonicmaster;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import me.randoms.harmonicmaster.adapter.TrackAdapter;
import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.Track;
import me.randoms.harmonicmaster.utils.Midi;
import me.randoms.harmonicmaster.utils.Statics;

public class SelectTrackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_track);

        ListView selectTrackList = (ListView) findViewById(R.id.selectTrackList);
        String filename = getIntent().getStringExtra("midiName");
        String root = Environment.getExternalStorageDirectory().getPath();
        Music midiData =  Midi.midiToJson(new File(root+ Statics.BASE_DIR+"/midi/" + filename));
        ArrayList<Track> trackList = new ArrayList<>();
        if(midiData != null)
            trackList = midiData.getTracks();
        TrackAdapter trackAdapter = new TrackAdapter(this, trackList);
        selectTrackList.setAdapter(trackAdapter);
        selectTrackList.setOnItemClickListener(trackAdapter);
    }
}
