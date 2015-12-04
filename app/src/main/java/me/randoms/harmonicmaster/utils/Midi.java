package me.randoms.harmonicmaster.utils;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import me.randoms.harmonicmaster.models.Music;
import me.randoms.harmonicmaster.models.Sound;
import me.randoms.harmonicmaster.models.Track;

/**
 * Created by randoms on 15-12-3.
 * In package me.randoms.harmonicmaster.utils
 */
public class Midi {
    public static Music midiToJson(File file){
        MidiFile midiFile;
        try {
            midiFile = new MidiFile(file);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        Music musicData = new Music();
        musicData.setAuthor("MidiFile");
        musicData.setTime(file.lastModified());
        musicData.setName(file.getName());
        musicData.setId(UUID.randomUUID().toString());
        MidiTrack track0 = midiFile.getTracks().get(0);
        float bpm = 120;
        for (MidiEvent event : track0.getEvents()) {
            if (event instanceof Tempo) {
                Tempo tempo = (Tempo) event;
                bpm = tempo.getBpm();
            }
        }
        float tickTime = 60*1000/(midiFile.getResolution() * bpm);
        long length = (long)(midiFile.getLengthInTicks() * tickTime);
        musicData.setLength(length);
        ArrayList<Sound> soundList = new ArrayList<>();
        musicData.setSounds(soundList);
        ArrayList<Track> trackList = new ArrayList<>();
        // add track info
        ArrayList<MidiTrack> tracks = midiFile.getTracks();
        int trackCount = 0;
        for(MidiTrack mTrack:tracks){
            int programNum = 0;
            int soundNum = 0;
            for (MidiEvent event : mTrack.getEvents()){
                if(event instanceof ProgramChange){
                    programNum = ((ProgramChange) event).getProgramNumber() + 1;
                }
                if(event instanceof NoteOn){
                    soundNum ++;
                }
            }
            trackList.add(new Track("音轨" + trackCount, soundNum, programNum));
            trackCount ++;
        }
        musicData.setTracks(trackList);
        return musicData;
    }

    public static Music getMidiTracks(File file, int trackNum){
        MidiFile midiFile;
        try {
            midiFile = new MidiFile(file);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        Music musicData = new Music();
        musicData.setAuthor("MidiFile");
        musicData.setTime(file.lastModified());
        musicData.setName(file.getName());
        musicData.setId(UUID.randomUUID().toString());
        MidiTrack track0 = midiFile.getTracks().get(0);
        float bpm = 120;
        for (MidiEvent event : track0.getEvents()){
            if(event instanceof Tempo){
                Tempo tempo = (Tempo)event;
                bpm = tempo.getBpm();
            }
        }
        float tickTime = 60*1000/(midiFile.getResolution() * bpm);
        long length = (long)(midiFile.getLengthInTicks() * tickTime);

        musicData.setLength(length);

        ArrayList<Sound> soundList = new ArrayList<>();
        MidiTrack track1 = midiFile.getTracks().get(trackNum);
        // get average tone
        int totalSound = 0;
        int totalTone = 0;
        for (MidiEvent event : track1.getEvents()){
            if(event instanceof NoteOn){
                NoteOn noteOn = (NoteOn)event;
                totalSound ++;
                totalTone += noteOn.getNoteValue();
            }
        }
        int distanceFormCenter;
        int distanceToAdd = 0;
        if(totalSound != 0){
            distanceFormCenter = totalTone / totalSound;
            distanceToAdd = -(int)(Math.floor((distanceFormCenter - 72.0)/12))*12; //转调, 72是中心C
        }
        boolean isLastStillOn = false;
        for (MidiEvent event : track1.getEvents()){
            //TODO 这里需要测试
            if(event instanceof NoteOff || event instanceof NoteOn){

                if(isLastStillOn){
                    Sound lastNote = soundList.get(soundList.size() - 1);
                    lastNote.setEnd((int)(event.getTick() * tickTime));
                }

                if(event instanceof NoteOn && ((NoteOn)event).getVelocity() != 0){
                    NoteOn noteOn = (NoteOn)event;
                    Sound soundData = new Sound();
                    soundData.setName(noteOn.getNoteValue() - 47 + distanceToAdd);
                    int start = (int)(noteOn.getTick() * tickTime);
                    soundData.setStart(start);
                    soundList.add(soundData);
                    isLastStillOn = true;
                }else {
                    isLastStillOn = false;
                }
            }
        }
        musicData.setSounds(soundList);
        return musicData;
    }
}
