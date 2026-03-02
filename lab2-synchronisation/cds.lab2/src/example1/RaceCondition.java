package example1;

public class RaceCondition extends Thread {
    private static int total = 0;
    public static final int THREADS = 4;
    public static final int COUNT = 100;

    public void run() {
        for(int i = 0; i < COUNT; i++)
            total++; 
    }    
    
    public static void main(String[] args) {
        RaceCondition[] threads = new RaceCondition[THREADS];
        for(int i = 0; i < THREADS; i++) {
            threads[i] = new RaceCondition();
            threads[i].start();
        }
        try {
            for(int i = 0; i < THREADS; i++)
                threads[i].join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Total :\t" + total);
    }

}