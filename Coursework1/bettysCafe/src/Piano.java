import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

// Piano class using semaphore to limit 2 costumers per time
public class Piano {
    private final Semaphore slots = new Semaphore(2, true);
    // Thread safe counter with atomic integer
    private final AtomicInteger activePlayers = new AtomicInteger(0);

    public void play(String customerName, long actionMillis, Consumer<String> logger) throws InterruptedException {
        // The acquire() method assures that if a third costumer tries to play while two are already playing, it will block until a slot is available.
        slots.acquire();
        int active = activePlayers.incrementAndGet();
        try {
            logger.accept(customerName + " plays the piano for " + actionMillis + "ms. (active players=" + active + ")");
            Thread.sleep(actionMillis);
            logger.accept(customerName + " finished playing the piano.");
        } finally {
            activePlayers.decrementAndGet();
            slots.release();
        }
    }
}
