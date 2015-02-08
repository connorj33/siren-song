package sirensong.com.sirensong;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

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


    @Override
    public void run() {
        for(int i = 0; i < noteList.length; i++){
            noteList[i] = new Note((int)(27.5 *((Math.pow(2,Math.pow((1/12),i))))));
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
                for (int i = 0; i < newArray.length; i ++) { //aNewArray : newArray) {
                    newArray[i] = FrequencyToNotes.getNote(newArray[i], currentFreq.timeStamp, noteList, ftn.oldFrequencies, 4);
                }
                for (int oldFreq: ftn.oldFrequencies) {
                    if (oldFreq != -1) {
                        //this one was not continued, add an end time
                        noteList[oldFreq].setTime(currentFreq.getTimeStamp());
                    }
                }
                ftn.holdover(newArray);
            }
        }
        //following the loop, sheep has one last task: getting rid of transient notes and gaps in noteList.
        //clean(noteList);
        try {
            new MidiMaker(this).makeMidi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clean(Note[] noteList) {
        int noteMinLength = 50;
        int gapMinLength = 50;

        //get rid of gaps first:
        for (Note n: noteList) {
            int counter = 1;

            while (counter != 0) { //do this procedure until there are no more gaps to close.
                counter = 0;
                Iterator<Long> iter1 = n.times.iterator();
                //Iterator<Long> iter2 = n.durations.iterator();


                Long currentBegin;
                Long currentEnd;
                Long nextBegin;
                LinkedList<Long> newTimes = new LinkedList<>();
                LinkedList<Long> newDurations = new LinkedList<>();

                if (iter1.hasNext()) {
                    currentBegin = iter1.next();
                }
                else {
                    break;
                }

                while (iter1.hasNext()) { //get the two currents and the next start time, copy current begin
                    newTimes.add(currentBegin);
                    currentEnd = iter1.next();
                    if (iter1.hasNext()) {
                        nextBegin = iter1.next();
                    } else {
                        continue;
                    }
                    if (nextBegin - currentEnd < gapMinLength) {
                        counter++; //we closed a gap
                        //in this case, do not copy currentEnd or nextBegin.
                        //skip them, copy over the next end time
                        Long nextEnd = iter1.next();
                        newTimes.add(nextEnd);
                        //calculate a new duration, add it to newDurations
                        newDurations.add(nextEnd - currentBegin);
                        if (iter1.hasNext()) { //set up for next iteration or exit if this is the end.
                            currentBegin = iter1.next();
                        } else {
                            break;
                        }
                    } else {
                        //copy it all over, prepare for another iteration
                        newTimes.add(currentEnd);
                        newDurations.add(currentEnd - currentBegin);
                        currentBegin = nextBegin;
                    }
                }
                n.times = newTimes;
                n.durations = newDurations;
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
