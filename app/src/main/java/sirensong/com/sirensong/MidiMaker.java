package sirensong.com.sirensong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.math.*;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
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
    //Find out tempo from intensity spikes from original data
    TimeSignature signature = new TimeSignature();
    //ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);


    Tempo t = new Tempo();
//        t.setBpm(228);
//
//        tempoTrack.insertEvent(ts);
//        tempoTrack.insertEvent(t);


    public void run() {
        //start bessie
        //make midi after that
    }

    public void extractAndMidifyNotes() {
        int channel = 0;
        int pitch;
        ArrayList<Long> durations = new ArrayList<Long>();
        int velocity = 100;


        for (int i = 0; i < bessie.noteList.length; i++) {
            for (int j = 0; j < bessie.noteList[i].times.size(); j++) {
                pitch = bessie.noteList[i].getPitch();
                noteTrack.insertNote(channel, pitch, velocity, 0, bessie.noteList[i].durations.element());
                durations.add(bessie.noteList[i].durations.element());

            }
        }
    }

    public long determineTempo(ArrayList<Long> durations) {
        long[] durationArray = new long[durations.size()];
        for (int i = 0; i < durations.size(); i++) {
            durationArray[i] = durations.remove() {
            }
            double averageBeatLength = arrayAvg(durationArray);
            return nsToPerMin(averageBeatLength);
        }
    }

    public long nsToPerMin(double ns) {
        return (long) (ns / 60000000000l);
    }


    public double arrayAvg(long[] data) {
        long average = 0;
        for (int i = 0; i < data.length; i++) {
            average += data[i];
        }
        return (double) (average / data.length);
    }
        // 3. Create a MidiFile with the tracks we created
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // 4. Write the MIDI data to a file
        File output = new File("exampleout.mid");
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
}
