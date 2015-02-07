package sirensong.com.sirensong;

import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    private Queue<Long> times;
    private Queue<Long> durations;



    Note(int pitch){
        this.pitch = pitch;
    }

    public int getPitch(){
        return this.pitch;
    }

    public void setTime(long time){
        if(times.isEmpty()){
            times.add(time);
        }

        else if(times.size() % 2 == 0) {
            times.add(time);
        }
    }
}
