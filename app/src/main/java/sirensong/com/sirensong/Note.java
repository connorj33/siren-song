package sirensong.com.sirensong;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    private long createTime;
    private long startTime;
    private long endTime;
    private long duration;
    private boolean complete = false;
    //quarter, whole, half, eight note?


    Note(int pitch){
        this.pitch = pitch;
    }

    Note(int pitch, long startTime, long endTime){
        this.pitch = pitch;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime - startTime;
    }




    //create time becomes start time if there isn't another incomplete note of the same pitch, and becomes end time if there is.
    //if a third note of the same pitch arrives before the timeout, the second one gets dropped.  after the timeout, the first and last are combined

}
