/**
 *
 * https://examples.javacodegeeks.com/java-basics/exceptions/java-lang-illegalmonitorstateexception-how-to-solve-illegalmonitorstateexception/
 */

package illegal.monitor.state.exception.example;

import java.util.concurrent.TimeUnit;
import illegal.monitor.state.exception.example.HelperClass.WaitingThread;

public class IllegalMonitorStateNotifyExample {
    public static void main(String[] args) {
        try {
            Thread waitThread = new WaitingThread();
             
            //Start the execution.
            waitThread.start();
             
            //Sleep for some seconds.
            TimeUnit.SECONDS.sleep(5);
             
            //Try to notify the waiting thread without owning the synchronization object.
            //The following statement results in an IllegalMonitorStateException.
            HelperClass.obj.notify();
             
            //Wait for all threads to terminate.
            waitThread.join();
        }
        catch (InterruptedException ex) {
            System.err.println("An InterruptedException was caught: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}