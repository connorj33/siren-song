package sirensong.com.sirensong;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    Queue<Long> times = new LinkedList<Long>() {
    };
    Queue<Long> durations;



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
