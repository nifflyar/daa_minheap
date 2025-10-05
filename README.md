# Assignment 2: Min-Heap Implementation

## Overview
This repository contains the implementation of a **Min-Heap** as part of **Assignment 2**.

- Implemented in Java.
- Performance metrics tracked using `PerformanceTracker`:
  - Comparisons
  - Swaps
  - Array accesses
  - Insert and extract operations
- CLI interface for running benchmarks on different input sizes and input distributions (random, sorted, reversed).
- Empirical validation via CSV export and plots.

---

## Usage Instructions

### Requirements
- Java 17+ (OpenJDK)
- Maven 3.6+

### Compile Project
```bash
mvn clean compile
```

### Run Benchmark via CLI

- Types: random, sorted, reversed

```bash
java -cp target/classes com.cli.BenchmarkRunner --n <number> --distribution <type> --output <file.csv>
```

The CSV file contains the following columns:
n, distribution, time_ms, inserts, extracts, swaps, arrayAccesses, comparisons



## Complexity Analysis
| Algorithm | Best Case      | Average Case  | Worst Case    | Space Complexity |
|-----------|----------------|---------------|---------------|-----------------|
| Min-Heap  | Θ(n)           | Θ(n log n)    | Θ(n log n)    | O(n)            |


### Notes
- **Best Case:** Already sorted input allows fast insertion but heapify is still required (Θ(n) for bulk insert with bottom-up heapify).  
- **Average Case:** Random input requires full heapify for each insertion/extraction.  
- **Worst Case:** Reversed or highly unsorted input triggers maximum heap operations.  
- **Space:** In-place storage for the heap array; auxiliary memory only for metrics tracking.  


## Metrics Collected

- `comparisons`: number of element comparisons  
- `swaps`: number of swaps between elements  
- `arrayAccesses`: reads/writes in heap array  
- `inserts/extracts`: number of insert/extract operations performed 


## Performance Plots

![time](/docs/performance-plots/time_and_n.png)
![swaps](/docs/performance-plots/swaps_and_n.png)
![inserts](/docs/performance-plots/inserts_and_n.png)
![extracts](/docs/performance-plots/extracts_and_n.png)
![comparisons](/docs/performance-plots/comparisons_and_n.png)
![arrayaccess](/docs/performance-plots/array_access_and_n.png)



