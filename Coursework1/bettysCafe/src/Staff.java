import java.util.function.Consumer;

public class Staff implements Runnable {
    private final String name;
    private final ItemType role;
    private final CafeSession session;
    private final Buffet buffet;
    private final Consumer<String> logger;

    public Staff(String name, ItemType role, CafeSession session, Buffet buffet, Consumer<String> logger) {
        this.name = name;
        this.role = role;
        this.session = session;
        this.buffet = buffet;
        this.logger = logger;
    }

    @Override
    // Implement Runnable - each staff is a separate thread
    public void run() {
        try {
            while (session.isOpen()) {
                Thread.sleep(session.randomMs(700, 1800));
                if (!session.isOpen()) {
                    break;
                }

                int qty = session.randomInt(1, 3);
                buffet.add(role, qty, name, logger);
                Thread.sleep(session.randomMs(300, 900));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            logger.accept(name + " ended shift.");
        }
    }
}
