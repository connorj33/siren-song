package sirensong.com.sirensong;

import org.apache.commons.math3.complex.Complex;

import java.util.LinkedList;

/**
 * A class for finding peaks among an array of values
 */
public class PeakFinder {

    double posThresh =  25000000;

    int peakLeft = -1;
    int peakRight = -1;

    int fuzzer = 75;

    int HighPass = 170; //no peaks below this allowed. There is no reason to need them.
    int LowPass = 8589;

    public Integer[] findPeaks(Complex[] fft) {
        LinkedList<Integer> peaks = new LinkedList<>();
        for (int i = HighPass; i < LowPass; i++) {
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

//        try {
//            File notes = new File(MainActivity.context.getFilesDir(), "export.csv");
//            FileWriter noteWriter = new FileWriter(notes);
//            for (int i = 0; i < fft.length; i++) {
//                noteWriter.append(i + ",");
//                noteWriter.append(Double.toString(fft[i].abs()));
//                noteWriter.append("\n");
//            }
//            noteWriter.append('\n');
//            noteWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return peaks.toArray(new Integer[0]);
    }

}
