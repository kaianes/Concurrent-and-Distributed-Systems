# Deadlock

- **Deadlock** is a situation where **two or more threads are blocked forever**
- Each thread is **waiting for another thread to release a resource**
- Because everyone is waiting, **no progress is made**

## Why does deadlock happen?
- **Resource contention**: Multiple threads need the same resources
- **Circular wait**: Each thread is waiting for a resource held by another thread
- **Hold and wait**: Threads hold resources while waiting for others
- **No preemption**: Resources cannot be forcibly taken away from threads

## How to prevent deadlock?
- **Avoid circular wait**: Ensure that threads acquire resources in a consistent order
- **Use a timeout**: If a thread cannot acquire a resource within a certain time, it should release any resources it holds and try again
- **Resource hierarchy**: Assign a hierarchy to resources and ensure that threads acquire resources in a specific order
- **Deadlock detection**: Implement a mechanism to detect deadlocks and take corrective action, such as terminating one of the threads or rolling back transactions

## Example of Deadlock
```java
public class DeadlockExample {
    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resource2) {
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked resource 2");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (resource1) {
                    System.out.println("Thread 2: Locked resource 1");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
```

In this example, `thread1` locks `resource1` and waits for `resource2`, while `thread2` locks `resource2` and waits for `resource1`. This creates a deadlock situation where neither thread can proceed.