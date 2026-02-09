package bike.rental;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

 /* 
    A semaphore is like a bike rental place, with a fixed number of bikes. If a 
    bike is available the client can take it, otherwise must wait. When a client 
    returns their bike then someone else can take it.
 */

public class BikeRental {

    private final Semaphore semaphore;

    public BikeRental(int size) {
        semaphore = new Semaphore(size);
    }

    public void getBike() {
        try {
            semaphore.acquire();
            Long duration = (long) (Math.random() * 10);
            System.out.println(Thread.currentThread().getName()
                    + " using bike for " + duration
                    + "  seconds, available bikes now: "
                    + semaphore.availablePermits());
            Thread.sleep(duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " there are "
                    + semaphore.getQueueLength() + " clients waiting in the queue!");
            System.out.println(Thread.currentThread().getName() + " is returning the bike ");
            semaphore.release();
        }
    }
}
