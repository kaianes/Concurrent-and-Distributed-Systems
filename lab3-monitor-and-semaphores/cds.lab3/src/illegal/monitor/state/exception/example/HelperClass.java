 /**
 *
 * https://examples.javacodegeeks.com/java-basics/exceptions/java-lang-illegalmonitorstateexception-how-to-solve-illegalmonitorstateexception/
 */

package illegal.monitor.state.exception.example;

import java.util.concurrent.TimeUnit;
 
public class HelperClass {
     
    //The Object used for synchronization among threads.
    public final static Object obj = new Object();
     
    public static class WaitingThread extends Thread {
         
        @Override
        public void run() {
            synchronized (obj) {
                try {
                    System.out.println("[WaitingThread]: Waiting for another thread "
                                    + "to notify me...");
                    obj.wait();
                    System.out.println("[WaitingThread]: Successfully notified!");
                }
                catch (InterruptedException ex) {
                    System.err.println("[WaitingThread]: An InterruptedException was caught: "
                                    + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
     
    public static class WakingThread extends Thread {
        @Override
        public void run() {
            synchronized (obj) {
                try {
                    System.out.println("[WakingThread]: Sleeping for some time...");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("[WakingThread]: Woke up!");
                     
                    System.out.println("[WakingThread]: About to notify another thread...");
                    obj.notify();
                    System.out.println("[WakingThread]: Successfully notified some other thread!");
                }
                catch (InterruptedException ex) {
                    System.err.println("[WaitingThread]: An InterruptedException was caught: "
                            + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
}