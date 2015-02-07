package sirensong.com.sirensong;

import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    private Queue<Long> times;
    private Queue<Long> durations;
    private long temp;


    Note(int pitch){
        this.pitch = pitch;
    }

    public int getPitch(){
        return this.pitch;
    }

    public void setStartTime(long startTime){
        if(times.isEmpty()){
            times.add(startTime);
            temp = startTime;
        }
        else if(times.size() % 2 == 0) {
            times.add(startTime);
            temp = startTime;
        }
    }

    public void setEndTime(long endTime) {
        if (times.size() % 2 == 1) {
            times.add(endTime);
            durations.add(endTime-temp);
        }
    }




    //create time becomes start time if there isn't another incomplete note of the same pitch, and becomes end time if there is.
    //if a third note of the same pitch arrives before the timeout, the second one gets dropped.  after the timeout, the first and last are combined

}
