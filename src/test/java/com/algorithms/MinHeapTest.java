package com.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.metrics.PerformanceTracker;

public class MinHeapTest {



    @Test
    public void testEmptyHeapThrowsException() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        assertThrows(IllegalStateException.class, heap::extractMin);
    }

    @Test
    public void testSingleElement() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        heap.insert(42);
        assertEquals(42, heap.extractMin());
    }

    @Test
    public void testDuplicateElements() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        heap.insert(5);
        heap.insert(5);
        heap.insert(5);
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }

    @Test
    public void testSortedInput() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        for (int i = 1; i <= 5; i++) heap.insert(i);

        List<Integer> result = new ArrayList<>();
        while (true) {
            try {
                result.add(heap.extractMin());
            } catch (IllegalStateException e) {
                break;
            }
        }

        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    public void testReverseSortedInput() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        for (int i = 5; i >= 1; i--) heap.insert(i);

        List<Integer> result = new ArrayList<>();
        while (true) {
            try {
                result.add(heap.extractMin());
            } catch (IllegalStateException e) {
                break;
            }
        }

        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    public void testDecreaseKeyValid() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        heap.insert(10);
        heap.insert(20);
        heap.insert(30);
        heap.decreaseKey(2, 5);
        assertEquals(5, heap.extractMin());
    }

    @Test
    public void testInvalidDecreaseKey() {
        MinHeap heap = new MinHeap(new PerformanceTracker());
        heap.insert(10);
        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(1, 2));
        assertThrows(IllegalArgumentException.class, () -> heap.decreaseKey(0, 20));
    }

    @Test
    public void testMergeHeaps() {
        MinHeap h1 = new MinHeap(new PerformanceTracker());
        MinHeap h2 = new MinHeap(new PerformanceTracker());
        h1.insert(10);
        h1.insert(40);
        h2.insert(5);
        h2.insert(15);
        h1.merge(h2);

        List<Integer> merged = new ArrayList<>();
        while (true) {
            try {
                merged.add(h1.extractMin());
            } catch (IllegalStateException e) {
                break;
            }
        }

        assertEquals(List.of(5, 10, 15, 40), merged);
    }



    @Test
    public void testRandomInputMatchesJavaSort() {
        Random rnd = new Random(42);
        for (int t = 0; t < 100; t++) {
            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                data.add(rnd.nextInt(1000));
            }

            List<Integer> expected = new ArrayList<>(data);
            Collections.sort(expected);

            MinHeap heap = new MinHeap(new PerformanceTracker());
            for (int val : data) heap.insert(val);

            List<Integer> actual = new ArrayList<>();
            while (true) {
                try {
                    actual.add(heap.extractMin());
                } catch (IllegalStateException e) {
                    break;
                }
            }

            assertEquals(expected, actual);
        }
    }


    @Test
    public void testScalability() {
        int[] sizes = {100, 1000, 10000};
        for (int n : sizes) {
            PerformanceTracker tracker = new PerformanceTracker();
            MinHeap heap = new MinHeap(tracker);
            Random rnd = new Random();

            long start = System.nanoTime();
            for (int i = 0; i < n; i++) heap.insert(rnd.nextInt(n * 10));
            for (int i = 0; i < n / 2; i++) heap.extractMin();
            long duration = System.nanoTime() - start;

            System.out.printf("n=%d -> %.2f ms, inserts=%d, extracts=%d%n",
                    n, duration / 1_000_000.0,
                    tracker.getInsertCount(),
                    tracker.getExtractCount());

            assertTrue(duration > 0);
        }
    }

    @Test
    public void testInputDistributions() {
        String[] types = {"random", "sorted", "reversed", "nearly_sorted"};
        int n = 1000;
        Random rnd = new Random();

        for (String dist : types) {
            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < n; i++) data.add(i);

            if (dist.equals("reversed")) Collections.reverse(data);
            if (dist.equals("random")) Collections.shuffle(data);
            if (dist.equals("nearly_sorted")) {
                for (int i = 0; i < n / 10; i++) {
                    int a = rnd.nextInt(n);
                    int b = rnd.nextInt(n);
                    Collections.swap(data, a, b);
                }
            }

            MinHeap heap = new MinHeap(new PerformanceTracker());
            for (int val : data) heap.insert(val);
            int prev = Integer.MIN_VALUE;
            while (true) {
                try {
                    int cur = heap.extractMin();
                    assertTrue(cur >= prev);
                    prev = cur;
                } catch (IllegalStateException e) {
                    break;
                }
            }
        }
    }

}
