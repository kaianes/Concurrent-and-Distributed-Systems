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

| Explain the concept of interference in concurrent systems and provide an example of how it could occur within the context of this scenario.

In concurrent systems, interference is a scenario that can lead to incorrect results. It occurs when multiple threads access shared resources without efficient synchronization.Consequently, the outcome could have suffered from interference of different processes that tried to read and write the same resource, manipulating it wrongly and overwriting data. For example, in Betty's Café, if two customers try to order the last slice of cake at the same time, without proper synchronization, both customers might end up being served the same slice, leading to a conflict and an incorrect output of the system. Technically speaking, the the shared resource (the last slice of cake) is accessed by multiple threads (the customers) without proper coordination, resulting in an inconsistent state. 

## Question 2

| Describe what a deadlock is, explaining its causes and the strategies used to avoid it.

Deadlocks are a state in concurrent systems where two or more threads are blocked forever, waiting for each other to release resources to continue their execution. For example, in Betty's Café, if the staff stop to prepare new items and one customer is waiting for a cup of tea while holding a slice of cake while another customer is waiting for a slice of cake while holding a cup of tea, both customers will be stuck in a deadlock, as they are waiting for each other to release the resources they need.

For a deadlock to occur, four conditions must be present. The first is the "Mutual Exclusion" concept, where only one thread can access a resource at a time. Secondly, "Hold and Wait" condition tells about how a thread is holds one resource and waits for another, still not releasing the one that it already has. The third condition "No Preemption" explains that resources cannot be forcibly taken from threads, and the final one is "Circular Wait": a chain of threads exists where each thread is waiting for a resource held by the next thread in the chain. 


--- review 
To avoid deadlocks, several strategies can be employed. One common approach is to use a lock hierarchy, where resources are assigned a specific order, and threads must acquire locks in that order. This prevents circular wait conditions. Another strategy is to implement a timeout mechanism, where threads will give up waiting for a resource after a certain period, allowing the system to recover from potential deadlocks. Additionally, using non-blocking algorithms or avoiding the use of locks altogether can also help prevent deadlocks in concurrent systems.

