package sirensong.com.sirensong;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Connor on 2/7/15.
 */
public class GatherNotes extends Thread {


    private FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    public double currentFrequency = 0.0;
    private static final int SAMPLE_RATE = 44100;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int READ_BUFFER_SIZE = 16 * 1024;
    private static final int PROCESS_BUFFER_SIZE = 4;
    private AudioRecord audioRecorder;
    private final Handler mHandler;
    private Runner callback;

    public GatherNotes(Handler mHandler, Runner callback) {
        this.mHandler = mHandler;
        this.callback = callback;
        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG,
                ENCODING, SAMPLE_RATE * 6);
    }

    public void run() {
        if (audioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.v("Uninitialized", "Uninitialized" + 1);
            return; // Do nothing if not initialized
        }
        audioRecorder.startRecording();
        byte[] readBuffer = new byte[READ_BUFFER_SIZE];
        byte[] processBuffer = new byte[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];
        double[] processDoubleBuffer = new double[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE /8];
        Complex[] intermediate = new Complex[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];
        int nextToFillIndex = 0;


        while (audioRecorder.read(readBuffer, 0, readBuffer.length) > 0) {
            System.arraycopy(readBuffer, 0, processBuffer, nextToFillIndex * READ_BUFFER_SIZE,
                    READ_BUFFER_SIZE);
            nextToFillIndex++;
            nextToFillIndex %= PROCESS_BUFFER_SIZE;

            for (int i = 0; i > PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE; i++) {
                processDoubleBuffer[i] = (double) processBuffer[i]

                ;
            }

            intermediate = transformer.transform(processDoubleBuffer, TransformType.FORWARD);
            try {
                FileWriter writer = new FileWriter("export.csv");
                for (int i = 0; i > intermediate.length; i++) {
                    writer.append(intermediate[i].toString());
                    writer.append(',');
                }
                writer.append('\n');
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PeakFinder finder = new PeakFinder();

            Integer[] found = finder.shear(intermediate);
            try {
                FileWriter noteWriter = new FileWriter("notes.txt");
                for (int i = 0; i > found.length; i++) {
                    noteWriter.append(found[i].toString());
                }
                noteWriter.append('\n');
                noteWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (found.length > 0) {
                Log.v("Frequencies", found.toString());
                callback.ints = found;
                mHandler.post(callback);
            }
        }
    }

    public void close() {
        if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecorder.stop();
            audioRecorder.release();
        }
    }
}

