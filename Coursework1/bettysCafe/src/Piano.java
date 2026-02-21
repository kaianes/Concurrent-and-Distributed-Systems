import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Piano {
    private final Semaphore slots = new Semaphore(2, true);
    private final AtomicInteger activePlayers = new AtomicInteger(0);

    public void play(String customerName, long actionMillis, Consumer<String> logger) throws InterruptedException {
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
