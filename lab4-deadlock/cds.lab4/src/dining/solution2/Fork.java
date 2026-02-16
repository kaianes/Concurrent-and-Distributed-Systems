package dining.solution2;

public class Fork {

    private boolean taken = false;
    private int identity;

    public Fork(int id){
        identity = id;
    }
    
    synchronized void put() {
        taken = false;
        notify();
    }

    synchronized void get()
            throws java.lang.InterruptedException {
        while (taken) {
            wait();
        }
        taken = true;
    }
}
