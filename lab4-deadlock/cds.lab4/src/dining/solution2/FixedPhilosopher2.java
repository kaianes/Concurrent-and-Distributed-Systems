package dining.solution2;
/**
 * Asymmetry idea from:
 * Magee & Kramer (2006). Concurrency: State Models and Java Programs  

 */
public class FixedPhilosopher2 extends Thread {

    public static final int SEATS = 5;
    private int seat;
    Fork left;
    Fork right;

    public FixedPhilosopher2(int seat, Fork right, Fork left) {
        this.seat = seat;
        this.right = right;
        this.left = left;        
    }

    private void eat() throws InterruptedException {
        Long duration = (long) (Math.random() * 10);
        System.out.println("Philosopher " + seat + " eats for  " + duration + " ms.");
        Thread.sleep(duration);
    }

    private void returnChopsticks() {
        left.put();
        right.put();
        System.out.println("Philosopher " + seat + " returned chopsticks.");
    }

    private void think() throws InterruptedException {
        Long duration = (long) (Math.random() * 1000);
        System.out.println("Philosopher " + seat + " thinks for  " + duration + " ms.");
        Thread.sleep(duration);
    }

    public void run() {
        try {
            while (true) {
                think();
                if (seat % 2 == 0) {
                    left.get();
                } else {
                    right.get();
                }
                Thread.sleep(50);
                if (seat % 2 == 0) {
                    right.get();
                } else {
                    left.get();
                }
                eat();
                returnChopsticks();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {

        FixedPhilosopher2[] philosophers = new FixedPhilosopher2[SEATS];
        Fork[] forks = new Fork[SEATS];
        for (int i = 0; i < SEATS; i++) {
            forks[i] = new Fork(i);
        }
        for (int i = 0; i < SEATS; i++) {
            philosophers[i] = new FixedPhilosopher2(i, forks[i], forks[(i + 1) % SEATS]);
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
