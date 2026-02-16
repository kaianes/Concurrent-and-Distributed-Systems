package deadlocks;

/**
 * adapted from Brian Goetz et al. (2006). Java Concurrency in Practice
 *
 */
public class LeftRightDeadlock {

    private static final Object left = new Object();
    private static final Object right = new Object();

    public static void leftRight() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " leftRight");
        synchronized (left) {
            System.out.println(Thread.currentThread().getName() + " get left lock");
            Thread.sleep(50);
            synchronized (right) {
                doSomething();
            }
        }
    }

    public static void rightLeft() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " rightLeft");
        synchronized (right) {
            System.out.println(Thread.currentThread().getName() + " get right lock");
            Thread.sleep(50);
            synchronized (left) {
                doSomething();
            }
        }
    }

    private static void doSomething() {
        try {
            System.out.println("doSomething()");
            Thread.sleep((long) (50 + Math.random() * 10));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        Thread t1 = new Thread() {
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " started");
                    LeftRightDeadlock.leftRight();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread t2 = new Thread() {
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " started");
                    LeftRightDeadlock.rightLeft();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
