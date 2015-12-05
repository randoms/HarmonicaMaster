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

import me.randoms.harmonicmaster.models.Harmonic10;
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
        return getMidiTracks(file, trackNum, false);
    }

    public static Music getMidiTracks(File file, int trackNum, boolean changeTone){
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
        MidiTrack track1;
        if(changeTone)
            track1 = ChangeTone(midiFile.getTracks().get(trackNum), 10);
        else
            track1 = midiFile.getTracks().get(trackNum);
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

    public static MidiTrack ChangeTone(MidiTrack origin, int type){
        // get current Harmonica type
        int[] sweetArea = Harmonic10.sweetAreas;
        if(type == Statics.TEN){
            sweetArea = Harmonic10.sweetAreas;
        }

        // get average tone
        int totalSound = 0;
        int totalTone = 0;
        for (MidiEvent event : origin.getEvents()){
            if(event instanceof NoteOn){
                NoteOn noteOn = (NoteOn)event;
                totalSound ++;
                totalTone += noteOn.getNoteValue();
            }
        }
        int distanceFormCenter = 0;
        int distanceToAdd = 0;
        if(totalSound != 0){
            distanceFormCenter = totalTone / totalSound;
            distanceToAdd = -(int)(Math.floor((distanceFormCenter - 72.0)/12))*12; //转调, 72是中心C 整12度升降
        }

        int[] soundList = new int[totalSound];
        int soundIndex = 0;
        for(MidiEvent event : origin.getEvents()){
            if(event instanceof NoteOn && ((NoteOn)event).getVelocity() != 0){
                soundList[soundIndex] = ((NoteOn) event).getNoteValue() - 48 + distanceToAdd;
                soundIndex ++;
            }
        }

        int originTonilty = distanceFormCenter%12;
        int[] badTonesCount = new int[12];

        for(int toneIndex = 0; toneIndex < 12; toneIndex ++){
            for(int tonename:soundList){
                int newTonename = tonename + toneIndex - originTonilty;
                boolean foundFlag = false;
                for(int sweetTone:sweetArea){
                    if(sweetTone == newTonename)
                        foundFlag = true;
                }
                // bad tone found
                if(!foundFlag)
                    badTonesCount[toneIndex] ++;
            }
        }

        // find min bad tone count
        int minBadCountIndex = 0;
        for(int badCountIndex = 0; badCountIndex < 12; badCountIndex ++){
            if(badTonesCount[badCountIndex] < badTonesCount[minBadCountIndex])
                minBadCountIndex = badCountIndex;
        }

        // 开始转调
        MidiTrack newTrack = new MidiTrack();
        for(MidiEvent event: origin.getEvents()){
            if(event instanceof NoteOn){
                int noteValue = ((NoteOn) event).getNoteValue();
                ((NoteOn) event).setNoteValue(noteValue + minBadCountIndex - originTonilty);
            }
            if(event instanceof NoteOff){
                int noteValue = ((NoteOff) event).getNoteValue();
                ((NoteOff) event).setNoteValue(noteValue + minBadCountIndex - originTonilty);
            }
            newTrack.insertEvent(event);
        }
        return newTrack;
    }
}
