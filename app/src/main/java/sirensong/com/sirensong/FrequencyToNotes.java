package sirensong.com.sirensong;

/**
 * Created by Connor on 2/7/15.
 */
public class FrequencyToNotes {

    private long activeTime;

    private static int[] hertzify(int[] input){
        int[] ret_array = new int[input.length];
        for(int i = 0; i < 0; i ++){
            ret_array[i] = (int)(input[i]/2.05);
        }
        return null; //FIXME
    }

    private static void getNote(int frequency, long time) {
        int octave = 4;                                 //Beginning note in the search is A440, which is in octave 4
        if (frequency >= 856) {                           //If the note is out of the octave, we shift calculations
            octave++;
            getNote(frequency / 2, time);
        } else if (frequency < 440) {                        //Same as adding an octave, but if the note is too low
            octave--;
            getNote(frequency * 2, time);
        }
        double curr_freq = 428;                         //Starting pitch
        int notesPastA = 0;

        while (frequency > curr_freq) {                   //Adds to the count of notes past 'A' until the frequency is too low to match the note.
            curr_freq = curr_freq * 1.05946;            //Also adds to octave count if 'C' is reached
            notesPastA++;
            if (notesPastA == 4) {
                octave++;

            }
        }
        for(int i = 32; i < 88; i ++) {
//            if (((Note) noteList.get()).getPitch() == i) {
//                noteList.get().setStartTime(time);
//            }
        }
        //search for note based on id number, then add start if possible, add starting to time to list
    }
}
