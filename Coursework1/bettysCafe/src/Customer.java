import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class Customer implements Runnable {
    private final String name;
    private final CafeSession session;
    private final Buffet buffet;
    private final Piano piano;
    private final Consumer<String> logger;

    public Customer(String name, CafeSession session, Buffet buffet, Piano piano, Consumer<String> logger) {
        this.name = name;
        this.session = session;
        this.buffet = buffet;
        this.piano = piano;
        this.logger = logger;
    }

    @Override
    // The main loop of the customer thread, where it randomly decides to order items, listen to music, or play the piano while the session is open.
    public void run() {
        try {
            while (session.isOpen()) {
                // Randomly select an action for the customer to perform. (0 to 6)
                int action = ThreadLocalRandom.current().nextInt(7);
                switch (action) {
                    // In acordance with the coursework actions specification
                    case 0 -> {
                        Order order = Order.coffeeOnly();
                        logger.accept(name + " wants " + order.describe() + ".");
                        // 
                        if (buffet.takeWhenAvailable(order, name, session, logger)) {
                            long ms = session.randomMs(900, 3000);
                            logger.accept(name + " drinks for " + ms + "ms.");
                            Thread.sleep(ms);
                            logger.accept(name + " finished drinking.");
                        }
                    }
                    case 1 -> {
                        Order order = Order.teaOnly();
                        logger.accept(name + " wants " + order.describe() + ".");
                        if (buffet.takeWhenAvailable(order, name, session, logger)) {
                            long ms = session.randomMs(900, 3000);
                            logger.accept(name + " drinks for " + ms + "ms.");
                            Thread.sleep(ms);
                            logger.accept(name + " finished drinking.");
                        }
                    }
                    case 2 -> {
                        Order order = Order.teaAndCake();
                        logger.accept(name + " wants " + order.describe() + ".");
                        if (buffet.takeWhenAvailable(order, name, session, logger)) {
                            long ms = session.randomMs(900, 3000);
                            logger.accept(name + " eats for " + ms + "ms.");
                            Thread.sleep(ms);
                            logger.accept(name + " finished eating.");
                        }
                    }
                    case 3 -> {
                        Order order = Order.coffeeAndCake();
                        logger.accept(name + " wants " + order.describe() + ".");
                        if (buffet.takeWhenAvailable(order, name, session, logger)) {
                            long ms = session.randomMs(900, 3000);
                            logger.accept(name + " eats for " + ms + "ms.");
                            Thread.sleep(ms);
                            logger.accept(name + " finished eating.");
                        }
                    }
                    case 4 -> {
                        Order order = Order.cakeOnly();
                        logger.accept(name + " wants " + order.describe() + ".");
                        if (buffet.takeWhenAvailable(order, name, session, logger)) {
                            long ms = session.randomMs(900, 3000);
                            logger.accept(name + " eats for " + ms + "ms.");
                            Thread.sleep(ms);
                            logger.accept(name + " finished eating.");
                        }
                    }
                    case 5 -> {
                        long ms = session.randomMs(700, 2500);
                        logger.accept(name + " listens to music for " + ms + "ms.");
                        Thread.sleep(ms);
                        logger.accept(name + " finished listening music.");
                    }
                    case 6 -> {
                        logger.accept(name + " wants to play the piano.");
                        long ms = session.randomMs(800, 2200);
                        piano.play(name, ms, logger);
                    }
                    default -> {
                    }
                }
                if (session.isOpen()) {
                    Thread.sleep(session.randomMs(150, 500));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            logger.accept(name + " leaves the cafe.");
        }
    }
}
