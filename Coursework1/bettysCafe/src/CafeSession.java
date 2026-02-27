import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class CafeSession {
    private final AtomicBoolean open = new AtomicBoolean(true);
    private final double speedMultiplier;

    public CafeSession(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier <= 0.0 ? 1.0 : speedMultiplier;
    }

    public boolean isOpen() {
        return open.get();
    }

    public void close() {
        open.set(false);
    }

    public long randomMs(int min, int max) {
        long raw = ThreadLocalRandom.current().nextLong(min, (long) max + 1L);
        return Math.max(1L, Math.round(raw * speedMultiplier));
    }

    public int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
