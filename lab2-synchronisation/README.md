# Synchronisation 

* In concurrent programming, multiple threads can run at the same time.
* Threads often share resources such as variables, files, or database connections.
* When more than one thread accesses the same resource at the same time, problems can occur.
* A **race condition** happens when the program result depends on the order in which threads execute.
* This can lead to incorrect results or inconsistent data.
* Another issue is **visibility**, where one thread updates a variable but other threads do not see the change.
* To avoid these problems, programs use **critical sections**.
* A critical section is a block of code that only one thread can execute at a time.
* Java provides synchronization mechanisms to protect critical sections:

  * the `synchronized` keyword
  * the `Lock` interface and its implementations


## `synchronized` keyword

Here it is in **simple English** 👇

* `synchronized` controls access to a piece of code in a multi-threaded program.
* It ensures that **only one thread at a time** can execute that code.
* It protects **shared resources** such as variables or objects.
* It prevents **race conditions**.
* It also guarantees **visibility**, meaning changes made by one thread are visible to others.

### How it works

* When a thread enters a `synchronized` method or block, it **acquires a lock**.
* While the lock is held, **other threads must wait**.
* When the thread finishes, the lock is released and another thread may enter.

### Where it can be used

* **Synchronized methods**
* **Synchronized blocks**

Example:

```java
synchronized void increment() {
    counter++;
}
```
Only one thread can execute the `increment` method at a time.

## `Lock` interface
Aqui vai em **inglês, bem simples** 👇

### What does the `Lock` mechanism do?

* `Lock` is a synchronization mechanism from `java.util.concurrent`.
* It also ensures that **only one thread at a time** accesses a shared resource.
* It is an alternative to `synchronized`, but **more flexible**.
* It helps prevent **race conditions** and ensures **visibility**.

### How it works

* A thread must **explicitly acquire the lock** before entering the critical section.
* When the thread finishes, it must **explicitly release the lock**.
* Other threads wait until the lock is released.

### Example

```java
Lock lock = new ReentrantLock();

lock.lock();
try {
    counter++;
} finally {
    lock.unlock();
}
```

Only one thread can execute the critical section at a time.

### Why use `Lock` instead of `synchronized`?

* Allows **more control** over locking and unlocking.
* Supports features like:

  * `tryLock()` (non-blocking attempts)
  * fairness policies
  * interruptible locks


## Buffer

* A buffer is a temporary storage area for data.
* It is used to hold data while it is being transferred from one place to another.
* In concurrent programming, a buffer can be shared between threads.
* A common example is the **producer-consumer problem**, where one thread produces data and another thread consumes it.
* A  producer can't save data in the buffer if it's full, and a consumer can't take data from the buffer if it's empty.
* For these types of situations, Java provides the `wait()`, `notify()`, and `notifyAll()` methods implemented in the Object class. A thread can call the wait() method inside a synchronized block of code.

