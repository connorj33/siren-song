package sirensong.com.sirensong;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class GatherNotes extends Thread {


    private FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    //public double currentFrequency = 0.0;
    private static final int SAMPLE_RATE = 16000; //44100;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int READ_BUFFER_SIZE = 16 * 1024;
    private static final int PROCESS_BUFFER_SIZE = 4;
    private AudioRecord audioRecorder;

    byte[] processBuffer;
    ArrayList<Note> NoteList;
    private Queue<Freq> freqQueue;

    public volatile boolean isDone = true;

    public GatherNotes() {
        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG,
                ENCODING, SAMPLE_RATE * 6);
    }

    public void run() {
        if (audioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.v("Uninitialized", "Uninitialized" + 1);
            return; // Do nothing if not initialized
        }
        isDone = false;
        audioRecorder.startRecording();
        long startTime = System.nanoTime();

        freqQueue = new LinkedList<>();
        byte[] readBuffer = new byte[READ_BUFFER_SIZE];
        processBuffer = new byte[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];
        int nextToFillIndex = 0;

        //start Connor's consumer thread
        new Thread(new Sheep(startTime, this)).start();

        while (audioRecorder.read(readBuffer, 0, readBuffer.length) > 0) {
            System.arraycopy(readBuffer, 0, processBuffer, nextToFillIndex * READ_BUFFER_SIZE,
                    READ_BUFFER_SIZE);
            long timestamp = System.nanoTime(); //TODO subtract the time since the current contents of the buffer started being buffered.

            nextToFillIndex++;
            nextToFillIndex %= PROCESS_BUFFER_SIZE;

            double[] processDoubleBuffer = new double[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE / 2];
            for (int i = 0, j = 0; i != processDoubleBuffer.length; ++i, j += 2) {
                processDoubleBuffer[i] = (double)( (processBuffer[j  ] & 0xff) |
                        ((processBuffer[j+1] & 0xff) <<  8) );
            }

            new Thread(new Worker(processDoubleBuffer, timestamp)).start();
        }
    }

    public void close() {
        if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecorder.stop();
            audioRecorder.release();
            isDone = true;
            Log.v("Frequencies", "closed the thing");
        }
    }

    public synchronized void queueFreq(Freq freq) {
        freqQueue.add(freq);
    }

    /**
     * will either return an available item to be processed or null
     */
    public synchronized Freq deQueueFreq() {
        return freqQueue.poll();
    }

    /**
     * A class that can handle the fft and subsequent peak identification asynchronously,
     * given  chunk of the audio buffer and a timestamp
     */
    private class Worker implements Runnable {

        double[] dArray;
        long timeStamp;

        public Worker(double[] array, long timeStamp) {
            dArray = array;
            this.timeStamp = timeStamp;
        }

        @Override
        public void run() {
            Complex[] intermediate; // = new Complex[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];

            intermediate = transformer.transform(dArray, TransformType.FORWARD);
            PeakFinder finder = new PeakFinder();
            Integer[] found = finder.findPeaks(intermediate);
            try {
                File notes = new File(MainActivity.context.getFilesDir(), "notes.txt");
                FileWriter noteWriter = new FileWriter(notes);
                for (int i = 0; i < found.length; i++) {
                    noteWriter.append(found[i].toString());
                    noteWriter.append("\n");
                }
                noteWriter.append('\n');
                noteWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Freq temp = new Freq();
            temp.peaks = found;
            temp.fftResults = intermediate;
            temp.timeStamp = timeStamp;
            queueFreq(temp);

//            if (found.length > 0) {
//                Log.v("Frequencies", found.toString());
//
//            }
        }
    }
}

