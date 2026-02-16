package dining.solution1;

/**
 *
 * Solution to Dining Philosophers problem
 * Atomicity: 
 * 
 * https://start-concurrent.github.io/chunked/chap13.html#_solution_dining_philosophers
 * 
 */
 
public class FixedPhilosopher1 extends Thread {

    public static final int SEATS = 5;
    private static boolean[] chopsticks = new boolean[SEATS];
    private int seat;

    public FixedPhilosopher1(int seat) {
        this.seat = seat;
    }

    private void getChopsticks( ) throws InterruptedException {
        int location1 = seat;
        int location2 = (seat + 1) % SEATS;
        synchronized(chopsticks) {	  	
            while(chopsticks[location1] || chopsticks[location2]) { 
                try {
                    chopsticks.wait();	
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            chopsticks[location1] = true;            
            chopsticks[location2] = true;
        }
        System.out.println("Philosopher " + seat + " picked up chopsticks " +
			location1 + " and " + location2 + ".");
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
                getChopsticks();
                eat();
                returnChopsticks();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        FixedPhilosopher1[] philosophers = new FixedPhilosopher1[SEATS];
        for (int i = 0; i < SEATS; i++) {
            philosophers[i] = new FixedPhilosopher1(i);
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