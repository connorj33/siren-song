package sirensong.com.sirensong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

/**
 * Created by Connor on 2/8/15.
 */
public class MidiMaker implements Runnable{
    Sheep bessie;
    public MidiMaker(long startTime, GatherNotes gather) throws IOException {

        bessie = new Sheep(startTime, gather);
        new Thread(bessie).start();
    }
    MidiTrack tempoTrack = new MidiTrack();
    MidiTrack noteTrack = new MidiTrack();
    TimeSignature signature = new TimeSignature(0 ,0 , 4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);


    public void run() {
        //start bessie

        LinkedList<Long> durations = extractAndMidifyNotes();
        Tempo tempo =new Tempo();
        float rate = determineTempo(durations);
        tempo.setBpm(rate);
        tempoTrack.insertEvent(tempo);
        tempoTrack.insertEvent(signature);
        midisheep("save");
    }

    public LinkedList<Long> extractAndMidifyNotes() {
        int channel = 0;
        int pitch;
        LinkedList<Long> durations = new LinkedList<>();
        int velocity = 100;


        for (int i = 0; i < bessie.noteList.length; i++) {
            for (int j = 0; j < bessie.noteList[i].times.size(); j++) {
                pitch = bessie.noteList[i].getPitch();
                noteTrack.insertNote(channel, pitch, velocity, 0, bessie.noteList[i].durations.element());
                durations.add(bessie.noteList[i].durations.element());

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

    public void midisheep(String filename) {
        ArrayList<MidiTrack> tracks = new ArrayList<>();

          tracks.add(tempoTrack);
          tracks.add(noteTrack);



        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // 4. Write the MIDI data to a file
        File output = new File(filename);
        try
        {
            midi.writeToFile(output);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}

