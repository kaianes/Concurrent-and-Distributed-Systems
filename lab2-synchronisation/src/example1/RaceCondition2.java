package example1;

import java.util.concurrent.TimeUnit;

public class RaceCondition2 extends Thread {

    private static int total = 0;
    public static final int THREADS = 4;
    public static final int COUNT = 1000;

    public void run() {
        for (int i = 0; i < COUNT; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            increment();
            // total++;
        }
    }

    public static synchronized void increment() {
        total++;
    }

    public static void main(String[] args) {
        RaceCondition2[] threads = new RaceCondition2[THREADS];
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new RaceCondition2();
            threads[i].start();
        }
        try {
            for (int i = 0; i < THREADS; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Total:\t" + total);
    }

}
