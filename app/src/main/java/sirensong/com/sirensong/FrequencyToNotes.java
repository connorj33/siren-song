package sirensong.com.sirensong;

/**
 * Created by Connor on 2/7/15.
 */
public class FrequencyToNotes {
    //Sequential listing of notes in the scale
    private static final String[] notes = {"A ", "A sharp ", "B ", "C ", "C sharp ", "D ", "D sharp ", "E ", "F ", "F sharp ", "G ", "G sharp " };

    private static String getNote(int frequency){
        int octave = 4;                                 //Beginning note in the search is A440, which is in octave 4
        if(frequency >= 880){                           //If the note is out of the octave, we shift calculations
            octave ++;
            getNote(frequency / 2);
        }
        else if(frequency <440){                        //Same as adding an octave, but if the note is too low
            octave --;
            getNote(frequency * 2);
        }
        double curr_freq = 440;                         //Starting pitch
        int notesPastA = 0;

        while(frequency > curr_freq){                   //Adds to the count of notes past 'A' until the frequency is too low to match the note.
            curr_freq = curr_freq * 1.05946;            //Also adds to octave count if 'C' is reached
            notesPastA++;
            if(notesPastA == 4){
                octave ++;
            }
        }

        //returns string in the format "C sharp 6"
        return notes[notesPastA] + Integer.toString(octave);
    }
}
