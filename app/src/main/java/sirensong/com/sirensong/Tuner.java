package sirensong.com.sirensong;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import org.apache.commons.math3.transform.*;

/**
 * Created by Connor on 2/7/15.
 */
public class Tuner extends Thread {


    private FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    public double currentFrequency = 0.0;
    private static final int SAMPLE_RATE = 44100;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int READ_BUFFER_SIZE = 16 * 1024;
    private static final int PROCESS_BUFFER_SIZE = 4;
    private AudioRecord audioRecorder;
    private final Handler mHandler;
    private Runnable callback;

    public Tuner(Handler mHandler, Runnable callback) {
        this.mHandler = mHandler;
        this.callback = callback;
        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG,
                ENCODING, SAMPLE_RATE * 6);
    }

    public void run() {
        if (audioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            return; // Do nothing if not initialized
        }
        audioRecorder.startRecording();
        byte[] readBuffer = new byte[READ_BUFFER_SIZE];
        byte[] processBuffer = new byte[PROCESS_BUFFER_SIZE * READ_BUFFER_SIZE];
        int nextToFillIndex = 0;

        while (audioRecorder.read(readBuffer, 0, readBuffer.length) > 0) {
            System.arraycopy(readBuffer, 0, processBuffer, nextToFillIndex * READ_BUFFER_SIZE,
                    READ_BUFFER_SIZE);
            nextToFillIndex++;
            nextToFillIndex %= PROCESS_BUFFER_SIZE;

            currentFrequency = 0;//DOSOMETHINGHEREKINDALIKETHEFFTTHATSTEPHENISDOING

            if (currentFrequency > 0) {
                mHandler.post(callback);
            }
        }
    }

    public void stopRunning() {
        super.stop();

    }

    public void close() {
        if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecorder.stop();
            audioRecorder.release();
        }
    }
}

