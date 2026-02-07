package photo.booth;

import java.util.concurrent.Semaphore;

public class PhotoBooth {

    private final Semaphore semaphore;

    public PhotoBooth(int size, boolean fairness) {
        semaphore = new Semaphore(size, fairness);
    }

    private void takePictures() {
        System.out.println(Thread.currentThread().getName() + " is in the booth, will take pictures now  ");
        int firstOperation = (int) (1 + (Math.random() * 10000) % 4);
        switch (firstOperation) {
            case 1:
                System.out.println(Thread.currentThread().getName() + " chooses 1 - passport – digital photos");
                break;
            case 2:
                System.out.println(Thread.currentThread().getName() + " chooses 2 - passport – paper photos");
                break;
            case 3:
                System.out.println(Thread.currentThread().getName() + " chooses 3 - funny – digital photos ");
                break;
            case 4:
                System.out.println(Thread.currentThread().getName() + " chooses 4 - funny – paper photos ");
                break;
            default:
                break;
        }
        int secondOperation = (int) ((Math.random() * 10000) % 5);
        switch (secondOperation) {
            case 0:
                System.out.println(Thread.currentThread().getName() + " chooses 0 - exit book");
                break;
            case 1:
                System.out.println(Thread.currentThread().getName() + " chooses 1 - passport – digital photos");
                break;
            case 2:
                System.out.println(Thread.currentThread().getName() + " chooses 2 - passport – paper photos");
                break;
            case 3:
                System.out.println(Thread.currentThread().getName() + " chooses 3 - funny – digital photos ");
                break;
            case 4:
                System.out.println(Thread.currentThread().getName() + " chooses 4 - funny – paper photos ");
                break;
            default:
                System.out.println(Thread.currentThread().getName() + " Now exiting booth");
        }
    }

    public void goToBooth() {
        try {
            System.out.println(Thread.currentThread().getName()
                    + " arrives, photo booths available " + semaphore.availablePermits()
                    + ", clients in the queue " + semaphore.getQueueLength());            
            boolean resultAquired = false;

            while (resultAquired == false) {
                resultAquired = semaphore.tryAcquire();
                System.out.println(Thread.currentThread().getName() + " tries to enter the photo booth, successful = " + resultAquired);
                if (resultAquired) {
                    takePictures();
                } else {
                    Long duration = (long) (Math.random() * 1000);
                    System.out.println(Thread.currentThread().getName() + " will walk around for " + duration + " ms and try again later");
                    Thread.sleep(duration);
                }

            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName()
                    + " almost ready to leave, booth available " + semaphore.availablePermits()
                    + " clients in the queue " + semaphore.getQueueLength());
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " leaves now");
        }
    }
}
