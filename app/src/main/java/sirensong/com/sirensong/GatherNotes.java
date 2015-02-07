package sirensong.com.sirensong;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Connor on 2/7/15.
 */
public class GatherNotes extends Thread {


    private FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    public double currentFrequency = 0.0;
    private static final int SAMPLE_RATE = 16000; //44100;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int READ_BUFFER_SIZE = 16 * 1024;
    private static final int PROCESS_BUFFER_SIZE = 4;
    private AudioRecord audioRecorder;
    private final Handler mHandler;
    private Runner callback;
    private Context context;

    public GatherNotes(Handler mHandler, Runner callback, Context context) {
        this.mHandler = mHandler;
        this.callback = callback;
        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG,
                ENCODING, SAMPLE_RATE * 6);
        this.context = context;
    }

    public void run() {
        if (audioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.v("Uninitialized", "Uninitialized" + 1);
            return; // Do nothing if not initialized
        }
        audioRecorder.startRecording();
        byte[] readBuffer = new byte[READ_BUFFER_SIZE];
        byte[] processBuffer = new byte[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];


        double[] processDoubleBuffer = new double[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE / 2];

        Complex[] intermediate = new Complex[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];
        int nextToFillIndex = 0;


        while (audioRecorder.read(readBuffer, 0, readBuffer.length) > 0) {
            System.arraycopy(readBuffer, 0, processBuffer, nextToFillIndex * READ_BUFFER_SIZE,
                    READ_BUFFER_SIZE);
            nextToFillIndex++;
            nextToFillIndex %= PROCESS_BUFFER_SIZE;


            for (int i = 0, j = 0; i != processDoubleBuffer.length; ++i, j += 2) {
                processDoubleBuffer[i] = (double)( (processBuffer[j  ] & 0xff) |
                        ((processBuffer[j+1] & 0xff) <<  8) );
            }

            intermediate = transformer.transform(processDoubleBuffer, TransformType.FORWARD);
            try {
                File file = new File(context.getFilesDir(), "export.csv");
                FileWriter writer = new FileWriter(file);
                for (int i = 0; i < intermediate.length; i++) {
                    writer.append(i + ",");
                    writer.append(Double.toString(intermediate[i].getReal()));
                    writer.append('\n');
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
            Log.v("Frequencies", "closed the thing");
        }
    }
}

