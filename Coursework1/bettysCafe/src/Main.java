import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    private static final int INITIAL_CUSTOMERS = 6;
    private static final int STAFF_COUNT = 3;
    private static final int INITIAL_CAKES = 2;
    private static final int INITIAL_TEAS = 2;
    private static final int INITIAL_COFFEES = 2;
    // Session duration in milliseconds (10 seconds)
    private static final long SESSION_DURATION_MS = 10_000L;

    public static void main(String[] args) {
        Consumer<String> logger = System.out::println;
        CafeSession session = new CafeSession(1.0);
        Buffet buffet = new Buffet(INITIAL_CAKES, INITIAL_TEAS, INITIAL_COFFEES);
        Piano piano = new Piano();
        List<Thread> actors = new ArrayList<>();

        logger.accept("Starting program with " + INITIAL_CUSTOMERS + " clients and " + STAFF_COUNT + " staff");
        logger.accept(buffet.buffetText());

        ItemType[] roles = {ItemType.TEA, ItemType.CAKE, ItemType.COFFEE};
        for (int i = 0; i < STAFF_COUNT; i++) {
            String staffName = "Staff-" + (i + 1);
            ItemType role = roles[i % roles.length];
            Thread staffThread = new Thread(new Staff(staffName, role, session, buffet, logger), staffName);
            actors.add(staffThread);
            staffThread.start();
        }

        for (int i = 0; i < INITIAL_CUSTOMERS; i++) {
            String customerName = "Client-" + (i + 1);
            Thread customerThread = new Thread(new Customer(customerName, session, buffet, piano, logger), customerName);
            actors.add(customerThread);
            customerThread.start();
        }

        try {
            Thread.sleep(SESSION_DURATION_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            shutdown(session, buffet, actors, logger);
        }
    }

    private static void shutdown(CafeSession session, Buffet buffet, List<Thread> actors, Consumer<String> logger) {
        session.close();
        logger.accept("Session is closing.");
        buffet.signalClosed();

        for (Thread actor : actors) {
            actor.interrupt();
        }
        for (Thread actor : actors) {
            try {
                actor.join(1500L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        int active = 0;
        for (Thread actor : actors) {
            if (actor.isAlive()) {
                active++;
            }
        }
        logger.accept("Session ended. Active threads=" + active + ".");
    }
}
