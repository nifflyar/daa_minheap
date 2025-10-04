package com.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.metrics.PerformanceTracker;

public class MinHeapTest {

    @Test
    public void testInsertAndExtractMin() {
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap heap = new MinHeap(tracker);

        heap.insert(5);
        heap.insert(3);
        heap.insert(8);

        assertEquals(3, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(8, heap.extractMin());


        assertTrue(tracker.getInsertCount() >= 3, "Insert count should be at least 3");
        assertTrue(tracker.getExtractCount() >= 3, "Extract count should be at least 3");
    }

    @Test
    public void testDecreaseKey() {
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap heap = new MinHeap(tracker);

        heap.insert(10);
        heap.insert(20);
        heap.insert(30);

        heap.decreaseKey(2, 5);
        assertEquals(5, heap.extractMin());
    }

    @Test
    public void testMerge() {
        PerformanceTracker tracker1 = new PerformanceTracker();
        PerformanceTracker tracker2 = new PerformanceTracker();

        MinHeap h1 = new MinHeap(tracker1);
        MinHeap h2 = new MinHeap(tracker2);

        h1.insert(10);
        h1.insert(40);
        h2.insert(5);
        h2.insert(15);

        h1.merge(h2);

        assertEquals(5, h1.extractMin());
        assertEquals(10, h1.extractMin());
        assertEquals(15, h1.extractMin());
        assertEquals(40, h1.extractMin());
    }

    @Test
    public void testEmptyHeapException() {
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap heap = new MinHeap(tracker);

        assertThrows(IllegalStateException.class, heap::extractMin);
    }

    @Test
    public void testInvalidDecreaseKey() {
        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap heap = new MinHeap(tracker);
        heap.insert(10);

        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(5, 2));

        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(0, 20));
    }
}
