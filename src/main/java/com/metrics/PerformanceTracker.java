package com.metrics;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceTracker {
    private final AtomicLong insertCount = new AtomicLong(0);
    private final AtomicLong extractCount = new AtomicLong(0);
    private final AtomicLong swapCount = new AtomicLong(0);
    private final AtomicLong arrayAccesses = new AtomicLong(0);


    private long startTimeNs = 0;
    private long endTimeNs = 0;


    public void startTimer() { startTimeNs = System.nanoTime(); }


    public void stopTimer() { endTimeNs = System.nanoTime(); }

    public void incrementInsert() { insertCount.incrementAndGet(); }
    public void incrementExtract() { extractCount.incrementAndGet(); }
    public void incrementSwap() { swapCount.incrementAndGet(); }
    public void incrementArrayAccesses(long delta) { arrayAccesses.addAndGet(delta); }

    public long getInsertCount() { return insertCount.get(); }
    public long getExtractCount() { return extractCount.get(); }
    public long getSwapCount() { return swapCount.get(); }
    public long getArrayAccesses() { return arrayAccesses.get(); }

    public double getElapsedMillis() {
        if (startTimeNs == 0) return 0.0;
        long end = (endTimeNs == 0) ? System.nanoTime() : endTimeNs;
        return (end - startTimeNs) / 1_000_000.0;
    }


    public void exportCsv(String filePath, int n, String distribution) throws IOException {
        boolean writeHeader = !new java.io.File(filePath).exists();
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            if (writeHeader) {
                pw.println("n,distribution,time_ms,inserts,extracts,swaps,decreaseKeys,merges,arrayAccesses");
            }
            String line = String.format("%d,%s,%.3f,%d,%d,%d,%d",
                    n,
                    distribution,
                    getElapsedMillis(),
                    getInsertCount(),
                    getExtractCount(),
                    getSwapCount(),
                    getArrayAccesses()
            );
            pw.println(line);
        }
    }

    public void reset() {
        insertCount.set(0);
        extractCount.set(0);
        swapCount.set(0);
        arrayAccesses.set(0);
        startTimeNs = 0;
        endTimeNs = 0;
    }

    public void printToStdout() {
        System.out.printf("time=%.3fms, inserts=%d, extracts=%d, swaps=%d, accesses=%d%n",
                getElapsedMillis(),
                getInsertCount(),
                getExtractCount(),
                getSwapCount(),
                getArrayAccesses());
    }
}
