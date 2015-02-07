package sirensong.com.sirensong;

import org.apache.commons.math3.complex.Complex;

/**
 * A class for finding peaks among an array of values
 */
public class PeakFinder {

    int scanRange = 1000;
    double runningTotal;
    double rollingAverage;

    public int[] findPeaks(Complex[] input) {
        int left = 0;
        int right = 1;
        runningTotal = input[0].getReal();
        rollingAverage = runningTotal;

        //expanding the range it is scanning
        while (right < scanRange) {
            runningTotal += input[right].getReal();
            rollingAverage = runningTotal / right;
            right++;
        }
        while (right < input.length) {
            runningTotal += input[right].getReal();
            runningTotal -= input[left].getReal();
            right++;
            left++;
            rollingAverage = runningTotal;
        }



        return null;
    }

}
