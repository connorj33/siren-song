package sirensong.com.sirensong;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by Connor on 2/7/15.
 */
public class Note {
    private int pitch;
    public Queue<Long> times = new Queue<Long>() {
        @Override
        public boolean add(Long aLong) {
            return false;
        }

        @Override
        public boolean offer(Long aLong) {
            return false;
        }

        @Override
        public Long remove() {
            return null;
        }

        @Override
        public Long poll() {
            return null;
        }

        @Override
        public Long element() {
            return null;
        }

        @Override
        public Long peek() {
            return null;
        }

        @Override
        public boolean addAll(Collection<? extends Long> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<Long> iterator() {
            return null;
        }

        @Override
        public boolean remove(Object object) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return null;
        }
    };

    public  Queue<Long> durations;



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
