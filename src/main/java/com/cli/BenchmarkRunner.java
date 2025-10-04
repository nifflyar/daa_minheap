package com.cli;

import java.util.Random;

import com.algorithms.MinHeap;
import com.metrics.PerformanceTracker;

public class BenchmarkRunner {

    public static void main(String[] args) {
        int n = 1000;
        String distribution = "random";
        String output = "/docs/performance-plots/minheap_metrics.csv";

        // CLI arguments
        // java -cp target/classes com.cli.BenchmarkRunner --n <number> --distribution <type> --output <file.csv>
        // types: random, sorted, reversed

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--n":
                    n = Integer.parseInt(args[++i]);
                    break;
                case "--distribution":
                    distribution = args[++i];
                    break;
                case "--output":
                    output = args[++i];
                    break;
            }
        }

        System.out.printf("Running MinHeap benchmark: n=%d, distribution=%s%n", n, distribution);

        int[] data = generateData(n, distribution);

        PerformanceTracker tracker = new PerformanceTracker();
        MinHeap heap = new MinHeap(tracker);

        tracker.startTimer();
        for (int value : data) {
            heap.insert(value);
        }
        int extractCount = n / 2;
        for (int i = 0; i < extractCount; i++) {
            heap.extractMin();
        }
        tracker.stopTimer();

        tracker.printToStdout();
        try {
            tracker.exportCsv(output, n, distribution);
            System.out.println("Results exported to " + output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] generateData(int n, String distribution) {
        int[] arr = new int[n];
        Random rnd = new Random();

        switch (distribution) {
            case "sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;
            case "reversed":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;
            case "random":
            default:
                for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(n * 10);
                break;
        }

        return arr;
    }
}
