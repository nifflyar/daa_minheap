package com.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.metrics.PerformanceTracker;

public class MinHeap {
    private final List<Integer> heap;
    private final PerformanceTracker tracker;

    public MinHeap(PerformanceTracker tracker) {
        this.heap = new ArrayList<>();
        this.tracker = tracker;
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    public void insert(int value) {
        heap.add(value);
        tracker.incrementArrayAccesses(1); 
        tracker.incrementInsert();
        heapifyUp(heap.size() - 1);
    }

    public int extractMin() {
        if (heap.isEmpty()) throw new IllegalStateException("Heap is empty");
        int min = heap.get(0);
        tracker.incrementArrayAccesses(1);
        int last = heap.remove(heap.size() - 1);
        tracker.incrementArrayAccesses(1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            tracker.incrementArrayAccesses(1);
            heapifyDown(0);
        }
        tracker.incrementExtract();
        return min;
    }

    public void decreaseKey(int index, int newVal) {
        if (index < 0 || index >= heap.size()) throw new IllegalArgumentException("Invalid index");
        int current = heap.get(index);
        tracker.incrementArrayAccesses(1);
        if (newVal > current) throw new IllegalArgumentException("New value is greater than current value");
        heap.set(index, newVal);
        tracker.incrementArrayAccesses(1);
        tracker.incrementDecreaseKey();
        heapifyUp(index);
    }

    public void merge(MinHeap other) {

        for (Integer v : other.heap) {
            insert(v);
        }
        tracker.incrementMerge();
    }

    private void heapifyUp(int i) {
        while (i > 0) {
            int p = parent(i);
            tracker.incrementArrayAccesses(2); 
            if (heap.get(p) <= heap.get(i)) break;
            swap(i, p);
            i = p;
        }
    }

    private void heapifyDown(int i) {
        int n = heap.size();
        while (true) {
            int l = left(i), r = right(i), smallest = i;
            if (l < n) {
                tracker.incrementArrayAccesses(1);
                if (heap.get(l) < heap.get(smallest)) smallest = l;
            }
            if (r < n) {
                tracker.incrementArrayAccesses(1);
                if (heap.get(r) < heap.get(smallest)) smallest = r;
            }
            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        int tmp = heap.get(i);
        int a = heap.get(j);
        tracker.incrementArrayAccesses(2);
        heap.set(i, a);
        heap.set(j, tmp);
        tracker.incrementArrayAccesses(2);
        tracker.incrementSwap();
    }

    public List<Integer> getHeap() { return heap; }
    public PerformanceTracker getTracker() { return tracker; }
}
