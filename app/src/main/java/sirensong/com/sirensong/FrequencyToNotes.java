package sirensong.com.sirensong;

/**
 * Created by Connor on 2/7/15.
 */
public class FrequencyToNotes {
    private static final String[] notes = {"A", "A sharp", "B", "C", "C sharp", "D", "D sharp", "E", "F", "F sharp", "G", "G sharp" }

    private static String getNote(int frequency){
        int octave = 4;
        if(frequency >= 880){
            octave ++;
            getNote(frequency / 2);
        }
        else if(frequency <440){
            octave --;
            getNote(frequency * 2);
        }
        double curr_freq = 440;
        int notesPastA = 0;
        for(int i = 0; i >= 12; i++){
            curr_freq = curr_freq * 1.05946;
            if(frequency > curr_freq){
                notesPastA++;
            }
            if(i == 4){
                octave ++;
            }
        }
        return notes[notesPastA] + Integer.toString(octave);
    }
}
