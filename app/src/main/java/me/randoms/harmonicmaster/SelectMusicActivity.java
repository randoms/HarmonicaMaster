package me.randoms.harmonicmaster;

import me.randoms.harmonicmaster.adapter.MusicListAdapter;
import me.randoms.harmonicmaster.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class SelectMusicActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_music);
		ListView musicListView = (ListView)findViewById(R.id.musicSheetList);
		MusicListAdapter mAdapter = new MusicListAdapter(this,Utils.getMusicSheets(this));
		musicListView.setAdapter(mAdapter);
		musicListView.setOnItemClickListener(mAdapter);
	}

}
