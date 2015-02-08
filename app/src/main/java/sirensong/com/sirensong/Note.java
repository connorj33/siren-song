package sirensong.com.sirensong;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;

    List<Long> times = new LinkedList<Long>() {
    };
    List<Long> durations = new LinkedList<>();

    Note(int pitch){
        this.pitch = pitch;
    }

    public int getPitch(){
        return this.pitch;
    }

    public void setTime(long time){
        times.add(time);
        if(times.size() % 2 == 0) {
            durations.add(time-times.get(times.size() -1));
        }
    }
}
