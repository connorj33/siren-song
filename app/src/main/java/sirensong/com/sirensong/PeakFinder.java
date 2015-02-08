package sirensong.com.sirensong;

import org.apache.commons.math3.complex.Complex;

import java.util.LinkedList;

/**
 * A class for finding peaks among an array of values
 */
public class PeakFinder {

    double posThresh = 20000000;
    double negThresh = -20000000;

    int peakLeft = -1;
    int peakRight = -1;

    int fuzzer = 75;

    int HighPass = 170; //no peaks below this allowed. There is no reason to need them.

    public Integer[] findPeaks(Complex[] fft) {
        LinkedList<Integer> peaks = new LinkedList<>();
        for (int i = HighPass; i < fft.length /2; i++) {
            if (fft[i].abs() > posThresh ) {
                if (peakLeft == -1) { //not already detecting a peak, start detecting one
                    peakLeft = i;
                    peakRight = i;
                    fuzzer = 75;
                }
                else { //widen the detected peak
                    peakRight = i;
                }
            }
            else {
                //check if we have just left a peak
                if (peakRight != -1) {
                    if (fuzzer > 0) {
                        fuzzer--;
                    }
                    else {
                        peaks.add((peakRight + peakLeft) / 2);
                        peakLeft = -1;
                        peakRight = -1;
                    }
                }
            }
        }
        return peaks.toArray(new Integer[0]);
    }

}
