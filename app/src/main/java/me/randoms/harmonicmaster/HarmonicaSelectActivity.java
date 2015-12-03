package me.randoms.harmonicmaster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.randoms.harmonicmaster.utils.Statics;

public class HarmonicaSelectActivity extends Activity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmonica_select);
        prefs = PreferenceManager.getDefaultSharedPreferences(HarmonicaSelectActivity.this);
        findViewById(R.id.ten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Statics.HARMONIC_TYPE , Statics.TEN);
                editor.commit();
                finish();
            }
        });

        findViewById(R.id.twelve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Statics.HARMONIC_TYPE , Statics.TWELVE);
                editor.commit();
                finish();
            }
        });

        findViewById(R.id.twentyFour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(Statics.HARMONIC_TYPE , Statics.TWENTYFOUR);
                editor.commit();
                finish();
            }
        });
    }
}
