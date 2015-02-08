package sirensong.com.sirensong;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Connor on 2/7/15.
 */
public class FrequencyToNotes {

    public int[] oldFrequencies = {0};
    public ArrayList<Integer> tones;

    public int[] hertzify(Integer[] input) {
//        int tones_count = 0;
//        int[] ret_array = new int[input.length];
//        for (int i = 0; i < input.length; i++) {
//            ret_array[i] = (int) (input[i] / 2.05);
//
//        }
//        tones = new ArrayList<>();
//        for(int i = ret_array.length; i > 0; i --){
//            for(int j = ret_array.length-i; j > i; j --){
//                if(!(ret_array[i] > ret_array[j]*.95 / 3 && ret_array[i] < ret_array[j]* 1.05 / 3)){
//                    tones.set(tones_count, ret_array[i]);
//                    tones_count ++;
//                }
//            }
//        }
//        return tones.toArray(new Integer[0]);


        // new requirements: must filter out overtones, but cannot take away all peaks.
        // Should take away roughly half if it is working well

        //I will do this by starting with the lowest, checking if any are a multiple of 3 from it,
        //and removing them if they are by setting them to 0, and thus skipping over them when I
        // check later. These will need to be removed completely.
        int number = input.length;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == 0) {
                continue;
            }
            for (int j = i + 1; j < input.length; j++) {
                if (aEquals(input[i], input[j], .02)) {
                    input[j] = 0;
                    number--;
                }
            }
        }

        int[] newArray = new int[number];
        int j = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] != 0) {
                newArray[j++] = (int)(input[i] / 2.05);
            }
        }


        return newArray;
    }

    private boolean aEquals(int int1, int int2, double threshold) {
        return (int1 > int2 * (1 - threshold) && int1 < int2 * (1 + threshold));
    }

    public void holdover(int[] oldFrequencies) {
        this.oldFrequencies = oldFrequencies;
    }

    public static void getNote(int frequency, long time, Note[] noteList, int[] oldFrequency) {
        Log.v("getNote", "working on identifying frequency " + frequency);


        int octave = 4;                                 //Beginning note in the search is A440, which is in octave 4
        if (frequency >= 856) {                           //If the note is out of the octave, we shift calculations
            octave++;
            getNote(frequency / 2, time, noteList, oldFrequency);
        } else if (frequency < 428) {                        //Same as adding an octave, but if the note is too low
            octave--;
            getNote(frequency * 2, time, noteList, oldFrequency);
        }
        double curr_freq = 428;                         //Starting pitch
        int notesPastA = 0;

        while (frequency > curr_freq) {                   //Adds to the count of notes past 'A' until the frequency is too low to match the note.
            curr_freq = curr_freq * 1.05946;            //Also adds to octave count if 'C' is reached
            notesPastA++;
            if (notesPastA == 4) {
                octave++;

            }



            //search for note based on id number, then add start if possible, add starting to time to list
        }

        int note = octave * 12 + notesPastA;

        for (int i = 0; i < oldFrequency.length; i++) {
            if (oldFrequency[i] != frequency) {
                if(note < 88) {
                    noteList[note].setTime(time);
                }
            }
        }

        Log.v(note +" ", ", "+ time);

    }
}