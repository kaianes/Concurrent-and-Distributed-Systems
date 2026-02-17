# Coursework

### Undestanding of the context

Betty's Café

2 hour session - cakes, teas, coffees

alternative periods - eating and drinking // playing the piano and listening to music

items of the cafe may become unavailable, if that happens, the costumer has to wait.

meanwhile, the café staff continue to prepare new items

piano: maximum of 2 people can play at the same time

### Threads

- Customers: they will perform actions such as ordering, eating, drinking, playing the piano, and listening to music. They will also have to wait if certain items are unavailable.

- Staff: they will be responsible for preparing items, managing inventory, and ensuring that the café runs smoothly. They will also have to handle customer requests and manage the piano usage.

remember to: 
- assign a random execution time to each action performed by customers and staff.
- log portant events.

## Question 1 

> Explain the concept of interference in concurrent systems and provide an example of how it could occur within the context of this scenario.

In concurrent systems, interference occurs when multiple threads access shared resources without proper synchronization, causing unexpected or incorrect results. Consequently, the outcome can suffer from interference when different processes try to read and write the same resource, manipulating it wrongly and overwriting data. For example, in Betty's Café, if two customers try to order the last slice of cake at the same time, without proper synchronization, both customers might end up being served the same slice, leading to a conflict and an incorrect output of the system. Technically speaking, the shared resource (the last slice of cake) is accessed by multiple threads (the customers) without proper coordination, resulting in an inconsistent state. 

## Question 2

> Describe what a deadlock is, explaining its causes and the strategies used to avoid it.

A Deadlock is a state in concurrent systems where two or more threads are blocked forever, waiting for each other to release resources to continue their execution. For example, in Betty's Café, if the staff stop to prepare new items and one customer is waiting for a cup of tea while holding the last slice of cake while another customer is waiting for a slice of cake while holding the last cup of tea, both customers will be stuck in a deadlock, as they are waiting for each other to release the resources they need.

For a deadlock to occur, four conditions must be present simultaneously. The first is the "Mutual Exclusion" concept, where only one thread can access a resource at a time. Secondly, the "Hold and Wait" condition explains how a thread  holds one resource and waits for another, still not releasing the one that it already has. The third condition "No Preemption" defines that resources cannot be forcibly taken from threads, and the final one is called  "Circular Wait": a chain of threads exists where each thread is waiting for a resource held by the next thread in the chain. 

To avoid deadlocks, it is necessary to break at least one of these conditions. One common approach is to use a lock hierarchy (also known as lock ordering), where all threads acquire locks in a predefined order. This prevents circular wait conditions, as threads will always acquire locks in the same sequence. For example, if the café staff always acquire locks for the tea before acquiring locks for the cake, and customers do the same, it will prevent incorrect manipulation of the items. This is especially important in situations where the order of locks depends on method arguments, such as `getOrder(cake, tea)` and `getOrder(tea, cake)`, which can lead to deadlocks if the locking order changes dynamically.

Another strategy is to implement a timeout mechanism, where threads give up waiting for a resource after a certain period. This solution does not guarantee that a contention will never happen, but it allows the system to recover from potential deadlocks. Additionally, using non-blocking algorithms and thread-safe structures such as `ConcurrentHashMap` or `BlockingQueue` can also help prevent deadlocks by avoiding the use of locks altogether.

To conclude, it is important to say that even if a deadlock happens, it is possible to detect and recover from it. The system can periodically check for deadlocks by analyzing the state of threads and resources. If a deadlock is detected, the system can take corrective actions, such as terminating one of the threads involved (to release resources) in the deadlock or rolling back some operations to break the circular wait.

## Question 3

> Explain how a faulty implementation of this scenario could lead to a deadlock, providing a concrete example. Then propose a suitable strategy to prevent it.