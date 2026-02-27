/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author admin
 */
public class MyBoundedBufferTest {
    
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;

    @Test
    public void testIsEmptyWhenConstructed() {
        MyBoundedBuffer<Integer> bb = new MyBoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        MyBoundedBuffer<Integer> bb = new MyBoundedBuffer<Integer>(10);
        for (int i = 0; i < 10; i++) {
            bb.put(i);
        }
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    @Test
    public void testTakeBlocksWhenEmpty() {
        final MyBoundedBuffer<Integer> bb = new MyBoundedBuffer<Integer>(10);
        Thread taker = new Thread() {
            public void run() {
                try {
                    int unused = bb.take();
                    fail(); // if we get here, it's an error
                } catch (InterruptedException success) {
                }
            }
        };
        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }

    /*
    class Big {
        double[] data = new double[100000];
    }
    
    @Test
    public void testLeak() throws InterruptedException {
        MyBoundedBuffer<Big> bb = new MyBoundedBuffer<Big>(CAPACITY);
        int heapSize1 = snapshotHeap();
        for (int i = 0; i < CAPACITY; i++)
            bb.put(new Big());
        for (int i = 0; i < CAPACITY; i++)
            bb.take();
        int heapSize2 = snapshotHeap();
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

    */
    private int snapshotHeap() {
        /* Snapshot heap and return heap size */
        return 0;
    }
}
