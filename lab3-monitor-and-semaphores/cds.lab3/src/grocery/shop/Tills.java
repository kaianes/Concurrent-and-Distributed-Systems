package grocery.shop;

import java.util.concurrent.Semaphore;
 
public class Tills {

    private final Semaphore semaphore;

    public Tills(int size, boolean fairness) {
        semaphore = new Semaphore(size, fairness);
    }

    public void goToTill() {
        try {
            System.out.println(Thread.currentThread().getName() 
                    + " arrives at tills, available tills " + semaphore.availablePermits()
                    + " clients in the queue " + semaphore.getQueueLength());
            semaphore.acquire();
            Long products = (long) (1 + Math.random() * 10);
            System.out.println(Thread.currentThread().getName()
                    + " using a till for  " + products
                    + " products, for time " + products * 100 
                    + " ms, available tills now "
                    + semaphore.availablePermits());
            Thread.sleep(products * 100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() 
                    + " packs groceries, there are available tills " + semaphore.availablePermits()
                    + " clients in the queue " + semaphore.getQueueLength());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " will exit the shop, there are available tills " 
                    + semaphore.availablePermits()
                    + " clients in the queue " + semaphore.getQueueLength());
        }
    }
}
