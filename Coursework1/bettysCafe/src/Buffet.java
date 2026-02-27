import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/* Monitor using ReentrantLock and Condition to manage the buffet stock and synchronization between customers and staff. */

public class Buffet {
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition stockChanged = lock.newCondition();

    private int cakes;
    private int teas;
    private int coffees;

    // Initializes the buffet with the given stock, ensuring no negative values.
    public Buffet(int cakes, int teas, int coffees) {
        this.cakes = Math.max(0, cakes);
        this.teas = Math.max(0, teas);
        this.coffees = Math.max(0, coffees);
    }

    // Returns true if the order was fulfilled, false if the session is closed before it can be fulfilled.
    public boolean takeWhenAvailable(Order order, String customerName, CafeSession session, Consumer<String> logger)
            throws InterruptedException {
        // if a thread is waiting for a for, then either it will get a lock or it will be interrupted by the session closing, in which case it should return false to indicate it was not able to take the order. 
        lock.lockInterruptibly();
        try {
            boolean waitingLogged = false;
            while (session.isOpen() && !canFulfill(order)) {
                if (!waitingLogged) {
                    logger.accept(customerName + " waits.");
                    waitingLogged = true;
                }
                stockChanged.await();
            }
            if (!session.isOpen()) {
                return false;
            }
            cakes -= order.cakes();
            teas -= order.teas();
            coffees -= order.coffees();
            logger.accept(customerName + " takes " + order.describe() + ".");
            logger.accept(buffetTextUnsafe());
            return true;
        } finally {
            lock.unlock();
        }
    }

    // Adds the specified quantity of the given item type to the buffet, ensuring at least one item is added.
    public void add(ItemType type, int qty, String staffName, Consumer<String> logger) {
        int amount = Math.max(1, qty);
        lock.lock();
        try {
            switch (type) {
                case CAKE -> cakes += amount;
                case TEA -> teas += amount;
                case COFFEE -> coffees += amount;
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            }
            logger.accept(staffName + " brings " + amount + " " + type.pluralName(amount) + ".");
            logger.accept(buffetTextUnsafe());
            logger.accept(staffName + " returns to kitchen.");
            stockChanged.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Signals all waiting threads that the buffet is closed, allowing them to exit.
    public void signalClosed() {
        lock.lock();
        try {
            stockChanged.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Returns a string representation of the current buffet stock, ensuring thread safety.
    public String buffetText() {
        lock.lock();
        try {
            return buffetTextUnsafe();
        } finally {
            lock.unlock();
        }
    }

    // Checks if the buffet can fulfill the given order based on the current stock levels.
    private boolean canFulfill(Order order) {
        return cakes >= order.cakes() && teas >= order.teas() && coffees >= order.coffees();
    }

    public int getCakes() {
        lock.lock();
        try {
            return cakes;
        } finally {
            lock.unlock();
        }
    }

    public int getTeas() {
        lock.lock();
        try {
            return teas;
        } finally {
            lock.unlock();
        }
    }

    public int getCoffees() {
        lock.lock();
        try {
            return coffees;
        } finally {
            lock.unlock();
        }
    }

    // Returns a string representation of the current buffet stock without acquiring the lock, is only called when the lock is already held.
    private String buffetTextUnsafe() {
        return "Buffet = (" + format(cakes, "cake", "cakes") + ", "
                + format(teas, "tea", "teas") + ", "
                + format(coffees, "coffee", "coffees") + ").";
    }

    private static String format(int value, String singular, String plural) {
        return value + " " + (value == 1 ? singular : plural);
    }
}
