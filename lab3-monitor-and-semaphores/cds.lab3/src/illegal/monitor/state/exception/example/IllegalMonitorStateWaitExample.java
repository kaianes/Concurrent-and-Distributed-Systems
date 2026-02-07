 /**
 *
 * https://examples.javacodegeeks.com/java-basics/exceptions/java-lang-illegalmonitorstateexception-how-to-solve-illegalmonitorstateexception/
 */

package illegal.monitor.state.exception.example;

public class IllegalMonitorStateWaitExample {
    public static void main(String[] args) {
        try {
            //Try to wait on the synchronization object, without owning it.
            //The following statement results in an IllegalMonitorStateException.
            HelperClass.obj.wait();
        }
        catch (InterruptedException ex) {
            System.err.println("An InterruptedException was caught: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
