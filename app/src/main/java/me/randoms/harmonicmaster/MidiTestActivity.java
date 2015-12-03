package me.randoms.harmonicmaster;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.InstrumentName;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TextualMetaEvent;
import com.leff.midi.event.meta.TrackName;
import com.leff.midi.examples.EventPrinter;
import com.leff.midi.util.MidiProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.randoms.harmonicmaster.utils.Statics;

public class MidiTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midi_test);
        // Create a new MidiProcessor:
        MidiFile midi = null;
        try {
            midi = new MidiFile(new File(Environment.getExternalStorageDirectory().getPath()
             + Statics.BASE_DIR + "/midi/深海少女.mid"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MidiTrack track = midi.getTracks().get(0);
        Log.d("midi length", midi.getLengthInTicks() +"");
        Log.d("midi tracks", midi.getTrackCount() + "");
        Log.d("midi resolution", midi.getResolution() + "");
        ArrayList<MidiTrack> tracks = midi.getTracks();
        for(MidiTrack mTrack: tracks){
            Iterator<MidiEvent> it = mTrack.getEvents().iterator();
            Log.d("mTrack:", mTrack.toString());
            while (it.hasNext()){
                MidiEvent event = it.next();
                if(event instanceof ProgramChange){
                    ProgramChange programChange = (ProgramChange)event;
                    Log.d("HERE", "That's it" +programChange.getProgramNumber());
                    if(programChange.getProgramNumber() == 23){

                    }
                }
            }
        }
    }
}
