package me.randoms.harmonicmaster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import me.randoms.harmonicmaster.adapter.MusicListAdapter;
import me.randoms.harmonicmaster.utils.Statics;
import me.randoms.harmonicmaster.utils.Utils;

public class SelectMusicActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_music);
		ListView musicListView = (ListView)findViewById(R.id.musicSheetList);
		MusicListAdapter mAdapter = new MusicListAdapter(this,Utils.getMusicSheets());
        if(Utils.getMusicSheets().size() == 0){
            findViewById(R.id.downloadText).setVisibility(View.VISIBLE);
            musicListView.setVisibility(View.GONE);
        }
		musicListView.setAdapter(mAdapter);
		musicListView.setOnItemClickListener(mAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SelectMusicActivity.this);
        if (prefs.getBoolean(Statics.FIRST_TIME_FLAG, true)) {
            // first time start
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Statics.FIRST_TIME_FLAG, false);
            editor.apply();
            Intent intent = new Intent(this, HarmonicaSelectActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectMusicActivity.this, HarmonicaSelectActivity.class);
                startActivity(intent);
            }
        });
	}



}
