/**
 *
 * https://examples.javacodegeeks.com/java-basics/exceptions/java-lang-illegalmonitorstateexception-how-to-solve-illegalmonitorstateexception/
 */

package illegal.monitor.state.exception.example;

import illegal.monitor.state.exception.example.HelperClass.WaitingThread;
import illegal.monitor.state.exception.example.HelperClass.WakingThread;
 
public class RunningExample {
    public static void main(String[] args) {
        try {
            Thread waitThread = new WaitingThread();
            Thread wakingThread = new WakingThread();
             
            //Start the execution.
            waitThread.start();
            wakingThread.start();
             
            //Wait for all threads to terminate.
            waitThread.join();
            wakingThread.join();
        }
        catch (InterruptedException ex) {
            System.err.println("An InterruptedException was caught: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
