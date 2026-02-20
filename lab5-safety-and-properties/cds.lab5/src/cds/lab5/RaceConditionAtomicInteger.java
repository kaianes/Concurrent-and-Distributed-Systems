package cds.lab5;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author raluc
 */
public class RaceConditionAtomicInteger extends Thread {
  
    private static AtomicInteger total = new AtomicInteger(0);
    public static final int THREADS = 1000;
    public static final int COUNT = 10000;

    public void run() {
        for(int i = 0; i < COUNT; i++)
            total.incrementAndGet();
    }    
    
    public static void main(String[] args) {
        RaceConditionAtomicInteger[] threads = new RaceConditionAtomicInteger[THREADS];
        for(int i = 0; i < THREADS; i++) {
            threads[i] = new RaceConditionAtomicInteger();
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