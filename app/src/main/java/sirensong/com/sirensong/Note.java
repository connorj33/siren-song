package sirensong.com.sirensong;

import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    private Queue<Long> startTime;
    private Queue<Long> endTime;
    private long duration;
    private boolean complete = false;
    //quarter, whole, half, eight note?


     private void setPitch(int pitch){
        this.pitch = pitch;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
        this.duration = this.endTime - this.startTime;
        this.complete = true;
    }

    public long getStartTime(){
        return this.startTime;
    }

    public long getDuration(){
        return this.duration;
    }

    public int getPitch(){
        return this.pitch;
    }


    //create time becomes start time if there isn't another incomplete note of the same pitch, and becomes end time if there is.
    //if a third note of the same pitch arrives before the timeout, the second one gets dropped.  after the timeout, the first and last are combined

}
