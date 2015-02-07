package sirensong.com.sirensong;

import org.apache.commons.math3.complex.Complex;

import java.util.LinkedList;

/**
 * A class for finding peaks among an array of values
 */
public class PeakFinder {

//    int scanRange = 1024;
//    double runningTotal;
//    double rollingAverage;
//    double[] trimmed;
//    double peakRatioThreshhold = 20;
//    int peakLeft;
//    int peakRight;
//    LinkedList<Integer> peaks;
//
//
//    public Integer[] shear(Complex[] input) {
//        trimmed = new double[input.length];
//        int left = 0;
//        int right = 1;
//        runningTotal = input[0].getReal();
//        rollingAverage = runningTotal;
//
//        //expanding the range it is scanning
//        while (right < scanRange) {
//            runningTotal += input[right].getReal();
//            rollingAverage = runningTotal / right;
//            right++;
//            trimBaseline(input, left, right, trimmed);
//        }
//        //move the range along
//        while (right < input.length) {
//            runningTotal += input[right].getReal();
//            runningTotal -= input[left].getReal();
//            right++;
//            left++;
//            rollingAverage = runningTotal / scanRange;
//            trimBaseline(input, left, right, trimmed);
//        }
//        //shrink it as you get to the end
//        while (left < input.length) {
//            runningTotal -= input[left].getReal();
//            rollingAverage = runningTotal / input.length - left;
//            trimBaseline(input, left, right, trimmed);
//        }
//
//        //at this point, you have the baseline trimmed out, you can do another pass and find ratios.
//        //peaks should be much more obvious.
//        left = 0;
//        right = 1;
//        peakLeft = -1;
//        peakRight = -1;
//        runningTotal = trimmed[0];
//        rollingAverage = runningTotal;
//        peaks = new LinkedList<>();
//
//        while (right < scanRange) {
//            runningTotal += trimmed[right];
//            rollingAverage = runningTotal / right;
//            right++;
//            findPeak(left, right, trimmed);
//        }
//        //move the range along
//        while (right < input.length) {
//            runningTotal += trimmed[right];
//            runningTotal -= trimmed[left];
//            right++;
//            left++;
//            rollingAverage = runningTotal / scanRange;
//            findPeak(left, right, trimmed);
//        }
//        //shrink it as you get to the end
//        while (left < input.length) {
//            runningTotal -= trimmed[left];
//            rollingAverage = runningTotal / input.length - left;
//            findPeak(left, right, trimmed);
//        }
//
//
//
//        return peaks.toArray(new Integer[0]);
//    }
//
//    void trimBaseline(Complex[] input, int left, int right, double[] trimmed) {
//        int position = (right+left)/2;
//        double current = input[position].getReal();
////        double ratio = current/rollingAverage;
////        if (ratio > 2)
//        if (current < rollingAverage) {
//            trimmed[position] = 0;
//        }
//        else {
//            trimmed[position] = input[position].getReal() - rollingAverage;
//        }
//    }
//
//    void findPeak(int left, int right, double[] trimmed) {
//        int position = (right+left)/2;
//        double current = trimmed[position];
//        double ratio = current/rollingAverage;
//        if (ratio > peakRatioThreshhold) {
//            if (peakLeft == -1) { //not already detecting a peak, start detecting one
//                //peaking = true;
//                peakLeft = position;
//                peakRight = position;
//            }
//            else { //widen the detected peak
//                peakRight = position;
//            }
//        }
//        else {
//            //check if we have just left a peak
//            if (peakRight != 0) {
//                peaks.add((peakRight + peakLeft) / 2);
//                peakLeft = -1;
//                peakRight = -1;
//            }
//        }
//    }

    double posThresh = 20000000;
    double negThresh = -20000000;

    int peakLeft = -1;
    int peakRight = -1;

    int fuzzer = 75;

    public Integer[] findPeaks(Complex[] fft) {
        LinkedList<Integer> peaks = new LinkedList<>();
        for (int i = 0; i < fft.length /2; i++) {
            if (fft[i].getReal() > posThresh || fft[i].getReal() < negThresh) {
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
