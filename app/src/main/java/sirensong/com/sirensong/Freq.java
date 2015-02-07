package sirensong.com.sirensong;

import org.apache.commons.math3.complex.Complex;

/**
 * A tiny data structure containing the analyzed data from each buffer chunk.
 */
public class Freq {
    public Integer[] peaks;
    public Complex[] fftResults;
    public long timeStamp;
}
