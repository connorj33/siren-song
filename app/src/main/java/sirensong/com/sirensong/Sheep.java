package sirensong.com.sirensong;

import java.util.Iterator;

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
        //following the loop, sheep has one last task: getting rid of transient notes and gaps in noteList.
        clean(noteList);
    }

    private void clean(Note[] noteList) {
        int noteMinLength = 50;
        int gapMinLength = 50;

        //get rid of gaps first:
        for (Note n: noteList) {
            Iterator<Long> iter1 = n.times.iterator();
            Iterator<Long> iter2 = n.durations.iterator();


            Long currentBegin;
            Long currentEnd;
            Long nextBegin;

            if (iter1.hasNext()) {
                currentBegin = iter1.next();
                currentEnd = iter1.next();
                if (iter1.hasNext()) {
                    nextBegin = iter1.next();
                }
                else {
                    continue;
                }
                if (nextBegin - currentEnd < gapMinLength) {
                    
                }
            }



        }

        //next, a pass to get rid of transient notes:
        for (Note n: noteList) {
            Iterator<Long> iter1 = n.times.iterator();
            Iterator<Long> iter2 = n.durations.iterator();

            Long currentDur;
            while (iter2.hasNext()) {
                currentDur = iter2.next();
               iter1.next(); //unless invalid data structure, there will be things here.
                if (currentDur < noteMinLength) {
                    iter2.remove();
                    iter1.remove();
                    iter1.next();
                    iter1.remove();
                }
                else {
                    iter1.next(); //it is long enough to be allowed, just loop again.
                }
            }
        }
    }
}
