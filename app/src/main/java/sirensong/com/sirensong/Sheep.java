package sirensong.com.sirensong;

/**
 * Created by test on 2/7/2015.
 */
public class Sheep implements Runnable {
    long startTime;
    GatherNotes gather;

    Sheep(long startTime, GatherNotes gather) {
        this.startTime = startTime;
        this.gather = gather;
    }

    @Override
    public void run() {
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
                //you have a freq to work with, do shit with it.
                //TODO YOUR CODE HERE (possibly in another thread?)



            }
        }
    }
}
