package sirensong.com.sirensong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

/**
 * Created by Connor on 2/8/15.
 */
public class MidiMaker{
    Sheep bessie;
    public MidiMaker(Sheep bessie) throws IOException {
        this.bessie = bessie;
        durations = extractAndMidifyNotes();
    }

    LinkedList<Long> durations;
    MidiTrack tempoTrack = new MidiTrack();
    MidiTrack noteTrack = new MidiTrack();
    TimeSignature signature = new TimeSignature(0 ,0 , 4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
    Tempo tempo =new Tempo();

    public void makeMidi() throws IOException {

        tempoTrack.insertEvent(signature);
        float rate = determineTempo(durations);
        tempo.setBpm(rate);
        tempoTrack.insertEvent(tempo);
        midisheep();
    }

    public LinkedList<Long> extractAndMidifyNotes() {
        int channel = 0;
        int pitch;
        LinkedList<Long> durations = new LinkedList<>();
        int velocity = 100;


        for (int i = 0; i < bessie.noteList.length; i++) {
            try {
                for (int j = 0; j < bessie.noteList[i].times.size(); j++) {
                    pitch = bessie.noteList[i].getPitch();
                    if(!(bessie.noteList[i].durations.isEmpty())) {
                        noteTrack.insertNote(channel, pitch, velocity, 100, bessie.noteList[i].durations.element());
                        durations.add(bessie.noteList[i].durations.element());
                    }
                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        return durations;
    }

    public long determineTempo(LinkedList<Long> durations) {
        long[] durationArray = new long[durations.size()];
        for (int i = 0; i < durations.size(); i++) {
            durationArray[i] = durations.poll();
        }
        double averageBeatLength = arrayAvg(durationArray);
        return nsToPerMin(averageBeatLength);
    }

    public long nsToPerMin(double ns) {
        return (long) (ns / 60000000000l);
    }

    public double arrayAvg(long[] data) {
        long average = 0;
        for (long aData : data) {
            average += aData;
        }
        return (double) (average / data.length);
    }

    public void midisheep() throws IOException {
        ArrayList<MidiTrack> tracks = new ArrayList<>();

          tracks.add(tempoTrack);
          tracks.add(noteTrack);



        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
        File midisave = new File(MainActivity.context.getFilesDir(), "save.mid");
        midi.writeToFile(midisave);

    }
}

