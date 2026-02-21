import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SessionController {
    private final AtomicBoolean open = new AtomicBoolean(false);
    private final AtomicInteger customerIds = new AtomicInteger(0);
    private final AtomicInteger staffIds = new AtomicInteger(0);
    private final List<Thread> customerThreads = Collections.synchronizedList(new ArrayList<>());
    private final List<Thread> staffThreads = Collections.synchronizedList(new ArrayList<>());
    private final Consumer<String> logger;

    private volatile long sessionEndMillis = 0L;
    private volatile double speedMultiplier = 1.0;
    private volatile Buffet buffet;
    private volatile Piano piano;
    private volatile Thread sessionWatcher;

    public SessionController(Consumer<String> logger) {
        this.logger = logger;
    }

    public synchronized void startSession(
            int initialCustomers,
            int staffCount,
            int initialCakes,
            int initialTeas,
            int initialCoffees,
            long sessionDurationMillis,
            double speedMultiplier) {
        if (isOpen()) {
            throw new IllegalStateException("Session already running.");
        }
        this.buffet = new Buffet(initialCakes, initialTeas, initialCoffees);
        this.piano = new Piano();
        this.speedMultiplier = speedMultiplier <= 0.0 ? 1.0 : speedMultiplier;
        this.customerThreads.clear();
        this.staffThreads.clear();
        this.customerIds.set(0);
        this.staffIds.set(0);

        this.sessionEndMillis = System.currentTimeMillis() + sessionDurationMillis;
        open.set(true);

        logger.accept("Starting program with " + initialCustomers + " clients and " + staffCount + " staff");
        logger.accept(buffet.buffetText());

        ItemType[] roles = {ItemType.TEA, ItemType.CAKE, ItemType.COFFEE};
        for (int i = 0; i < staffCount; i++) {
            int id = staffIds.incrementAndGet();
            String staffName = "Staff-" + id;
            ItemType role = roles[i % roles.length];
            Thread t = new Thread(new Staff(staffName, role, this, buffet, logger), staffName);
            staffThreads.add(t);
            t.start();
        }

        for (int i = 0; i < initialCustomers; i++) {
            addCustomer();
        }

        sessionWatcher = new Thread(() -> {
            while (isOpen() && System.currentTimeMillis() < sessionEndMillis) {
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            stopSession();
        }, "Session-Watcher");
        sessionWatcher.setDaemon(true);
        sessionWatcher.start();
    }

    public synchronized void stopSession() {
        if (!open.compareAndSet(true, false)) {
            return;
        }

        logger.accept("Session is closing.");
        if (buffet != null) {
            buffet.signalClosed();
        }

        List<Thread> actors = new ArrayList<>();
        synchronized (staffThreads) {
            actors.addAll(staffThreads);
        }
        synchronized (customerThreads) {
            actors.addAll(customerThreads);
        }

        for (Thread actor : actors) {
            actor.interrupt();
        }
        if (sessionWatcher != null && Thread.currentThread() != sessionWatcher) {
            sessionWatcher.interrupt();
        }

        Thread summary = new Thread(() -> {
            for (Thread actor : actors) {
                try {
                    actor.join(1500L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            logger.accept("Session ended. Active customers=" + getActiveCustomers()
                    + ", active staff=" + getActiveStaff() + ".");
        }, "Session-Summary");
        summary.setDaemon(true);
        summary.start();
    }

    public void addCustomer() {
        if (!isOpen()) {
            logger.accept("Cannot add customer: session is not running.");
            return;
        }
        int id = customerIds.incrementAndGet();
        String customerName = "Client-" + id;
        Thread t = new Thread(new Customer(customerName, this, buffet, piano, logger), customerName);
        customerThreads.add(t);
        t.start();
    }

    public boolean isOpen() {
        return open.get();
    }

    public long randomMs(int min, int max) {
        long raw = ThreadLocalRandom.current().nextLong(min, (long) max + 1L);
        return Math.max(1L, Math.round(raw * speedMultiplier));
    }

    public int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int getCakes() {
        return buffet == null ? 0 : buffet.getCakes();
    }

    public int getTeas() {
        return buffet == null ? 0 : buffet.getTeas();
    }

    public int getCoffees() {
        return buffet == null ? 0 : buffet.getCoffees();
    }

    public int getActiveCustomers() {
        int count = 0;
        synchronized (customerThreads) {
            for (Thread t : customerThreads) {
                if (t.isAlive()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getActiveStaff() {
        int count = 0;
        synchronized (staffThreads) {
            for (Thread t : staffThreads) {
                if (t.isAlive()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getTotalCustomers() {
        return customerIds.get();
    }

    public int getTotalStaff() {
        return staffIds.get();
    }

    public long getTimeLeftMillis() {
        return Math.max(0L, sessionEndMillis - System.currentTimeMillis());
    }
}
