package deadlocks;
/**
 * Example of deadlock, from:
 * https://start-concurrent.github.io/chunked/chap13.html#_pitfalls_synchronization_challenges
 */


public class DeadlockPhilosopher extends Thread {

    public static final int SEATS = 5;
    private static boolean[] chopsticks = new boolean[SEATS];
    private int seat;

    public DeadlockPhilosopher(int seat) {
        this.seat = seat;
    }
    
    private void getChopstick(int location) throws InterruptedException {
        synchronized (chopsticks) {
            while (chopsticks[location]) {
                chopsticks.wait();
            }
            chopsticks[location] = true;
        }
        System.out.println("Philosopher " + seat + " picked up chopstick " + location + ".");
    }

    private void eat() throws InterruptedException {
        Long duration = (long) (Math.random() * 10);
        System.out.println("Philosopher " + seat + " eats for  " + duration + " ms.");
        Thread.sleep(duration);
    }

    private void returnChopsticks() {

        synchronized (chopsticks) {
            // Done eating, put back chopsticks
            chopsticks[seat] = false;
            chopsticks[(seat+1)%SEATS] = false;    
            chopsticks.notifyAll();
        }
        System.out.println("Philosopher " + seat + " returned chopsticks.");
    }

    private void think() throws InterruptedException {
        Long duration = (long) (Math.random()* 1000);
        System.out.println("Philosopher " + seat + " thinks for  " + duration + " ms.");
        Thread.sleep(duration);
    }

    public void run() {
        try {
            while (true) {
                think();
                getChopstick(seat);
                // Thread.sleep(50);
                getChopstick((seat + 1) % SEATS);
                eat();
                returnChopsticks();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        DeadlockPhilosopher[] philosophers = new DeadlockPhilosopher[SEATS];
        for (int i = 0; i < SEATS; i++) {
            philosophers[i] = new DeadlockPhilosopher(i);
            philosophers[i].start();
        }
        try {
            for (int i = 0; i < SEATS; i++) {
                philosophers[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
    }
}
