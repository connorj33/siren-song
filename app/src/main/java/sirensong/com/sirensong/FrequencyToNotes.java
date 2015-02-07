package sirensong.com.sirensong;

import java.util.ArrayList;

/**
 * Created by Connor on 2/7/15.
 */
public class FrequencyToNotes {

    public int[] oldFrequencies ={0};


    public static int[] hertzify(Integer[] input){
        int[] ret_array = new int[input.length];
        for(int i = 0; i < 0; i ++){
            ret_array[i] = (int)(input[i]/2.05);
        }
        return ret_array;
    }

    public void holdover(int[] oldFrequencies){
        this.oldFrequencies = oldFrequencies;
    }

    public static void getNote(int frequency, long time1, Note[] noteList, int[] oldFrequency) {
        int octave = 4;                                 //Beginning note in the search is A440, which is in octave 4
        if (frequency >= 856) {                           //If the note is out of the octave, we shift calculations
            octave++;
            getNote(frequency / 2, time1, noteList, oldFrequency);
        } else if (frequency < 428) {                        //Same as adding an octave, but if the note is too low
            octave--;
            getNote(frequency * 2, time1, noteList, oldFrequency);
        }
        double curr_freq = 428;                         //Starting pitch
        int notesPastA = 0;

        while (frequency > curr_freq) {                   //Adds to the count of notes past 'A' until the frequency is too low to match the note.
            curr_freq = curr_freq * 1.05946;            //Also adds to octave count if 'C' is reached
            notesPastA++;
            if (notesPastA == 4) {
                octave++;

            }
        int note = octave*12 + notesPastA;

        for(int i = 0; i < oldFrequency.length; i++){
            if(oldFrequency[i] != frequency){
                if(noteList[note] != null){
                    noteList[note] = new Note(note);
                }
                noteList[note].setTime(time1);
                }
            }
        }


        //search for note based on id number, then add start if possible, add starting to time to list
    }

}
