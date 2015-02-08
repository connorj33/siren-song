package sirensong.com.sirensong;

/**
 * Created by test on 2/7/2015.
 */
public class Sheep implements Runnable {
    long startTime;
    GatherNotes gather;
    Note[] noteList = new Note[88];


    Sheep(long startTime, GatherNotes gather) {
        this.startTime = startTime;
        this.gather = gather;
    }

    public Note[] getAllNotes(){

    }

    public int getNotePitch(Note a){
        return a.getPitch();
    }

    public long[] getStartingTimes(Note a){
        long startTimes[] = new long[a.times.size()/2];
        for (int i = 0; i < a.times.size()/2; i++) {
            startTimes[i] = a.times[2*i];
        }
    }

    public long[] get_durations(Note a){
        return a.durations[];
    }


    @Override
    public void run() {
        for(int i = 0; i < noteList.length; i++){
            noteList[i] = new Note((int)27.5 *((2^(1/12))^i));
        }
        Freq currentFreq;
        while (true) {
            currentFreq = gather.deQueueFreq();
            if (currentFreq == null) { //queue is empty
                if (gather.isDone) {
                    break; //there is no more to consume and this thread can die in peace.
                }
                // it is empty, but more is coming. wait, loop again.
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                FrequencyToNotes ftn = new FrequencyToNotes();
                int[] newArray = ftn.hertzify(currentFreq.peaks);
                for(int i = 0; i < newArray.length; i++){
                    ftn.getNote(newArray[i],currentFreq.timeStamp,noteList, ftn.oldFrequencies, 4);
                }
                ftn.holdover(ftn.oldFrequencies);
            }
        }
    }
}
