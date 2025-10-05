package com.cli;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import com.algorithms.MinHeap;
import com.metrics.PerformanceTracker;



@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2) 
@Measurement(iterations = 3) 
public class MinHeapBenchmark {

    @Param({"100", "1000", "10000"}) 
    public int n;

    @Param({"random", "sorted", "reversed", "nearly_sorted"}) 
    public String distribution;

    @Param({"heap_results.csv"}) 
    public String outputFile;

    private int[] data;
    private PerformanceTracker tracker;
    private MinHeap heap;
    private final Random rnd = new Random(42);

    private double lastElapsed;
    private long lastInserts, lastExtracts, lastSwaps, lastArrayAccesses, lastComparisons;

    @Setup(Level.Iteration)
    public void setup() {
        tracker = new PerformanceTracker();
        heap = new MinHeap(tracker);
        data = generateData(n, distribution);
    }

    private int[] generateData(int n, String distribution) {
        int[] arr = new int[n];
        switch (distribution) {
            case "sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;
            case "reversed":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;
            case "nearly_sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                for (int i = 0; i < n / 10; i++) {
                    int a = rnd.nextInt(n);
                    int b = rnd.nextInt(n);
                    int tmp = arr[a];
                    arr[a] = arr[b];
                    arr[b] = tmp;
                }
                break;
            case "random":
            default:
                for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(n * 10);
                break;
        }
        return arr;
    }

    @Benchmark
    public void insertAndExtractHalf() {
        tracker.reset();
        tracker.startTimer();

        for (int val : data) {
            heap.insert(val);
        }

        for (int i = 0; i < n / 2; i++) {
            heap.extractMin();
        }

        tracker.stopTimer();

        lastElapsed = tracker.getElapsedMillis();
        lastInserts = tracker.getInsertCount();
        lastExtracts = tracker.getExtractCount();
        lastSwaps = tracker.getSwapCount();
        lastArrayAccesses = tracker.getArrayAccesses();
        lastComparisons = tracker.getComparisonCount();
    }

    @TearDown(Level.Iteration)
    public void exportCsvAfterIteration() {
        boolean writeHeader = !new java.io.File(outputFile).exists();
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile, true))) {
            if (writeHeader) {
                pw.println("n,distribution,time_ms,inserts,extracts,swaps,arrayAccesses,comparisons");
            }
            String line = String.format("%d,%s,%.3f,%d,%d,%d,%d,%d",
                    n,
                    distribution,
                    lastElapsed,
                    lastInserts,
                    lastExtracts,
                    lastSwaps,
                    lastArrayAccesses,
                    lastComparisons
            );
            pw.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
