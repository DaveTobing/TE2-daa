import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            String[] files = {"dataset_kecil.txt", "dataset_sedang.txt", "dataset_besar.txt"};
        try {
            for (String filename : files) {
                // Read the dataset from the text file
                int[][] weightsAndValues = readDataset(filename);
                int[] weights = weightsAndValues[0];
                int[] values = weightsAndValues[1];

                // Determine W based on the file name
                int W = determineCapacity(filename);

                // Measure DP execution time
                long knapsackStart = System.nanoTime();
                System.gc();
                System.gc();
                System.gc();
                long knapsackStartMemoryUsage = getMemoryUsage();
                int result = DynamicPrograming.unboundedKnapsack(W, values.length, values, weights);
                long knapsackEndMemoryUsage = getMemoryUsage();
                long knapsackEnd = System.nanoTime();

                double knapsackExecutionTime = ((double) (knapsackEnd - knapsackStart) / 1_000_000);
                double knapsackMemoryUsage = (double) ((knapsackEndMemoryUsage - knapsackStartMemoryUsage) / 1024);

                System.out.println("Dynamic Programing on " + filename);
                System.out.println("Result: " + result);
                System.out.println("Execution Time (ms): " + knapsackExecutionTime);
                System.out.println("Memory Usage (KB): " + knapsackMemoryUsage);

                System.out.println("--------------------------------------");

                // Restore the dataset for ShellSort
                weightsAndValues = readDataset(filename);

                // Measure BnB execution time and memory usage
                List<BranchandBound.Item> items = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    items.add(new BranchandBound.Item(values[i], weights[i]));
                }

                long BnBStart = System.nanoTime();
                System.gc();
                System.gc();
                System.gc();
                long BnBStartMemoryUsage = getMemoryUsage();
                BranchandBound branchandBoundSolver = new BranchandBound(W, items);
                branchandBoundSolver.solve();
                long bestValue = branchandBoundSolver.getBestValue();
                long BnBEndMemoryUsage = getMemoryUsage();
                long BnBEnd = System.nanoTime();

                double BnBExecutionTime = (double) ((BnBEnd - BnBStart) / 1_000_000);
                double BnBMemoryUsage = (double) ((BnBEndMemoryUsage - BnBStartMemoryUsage) / 1024);

                System.out.println("BranchandBound on " + filename);
                System.out.println("Result: " + bestValue);
                System.out.println("Execution Time (ms): " + BnBExecutionTime);
                System.out.println("Memory Usage (KB): " + BnBMemoryUsage);

                System.out.println("--------------------------------------");
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read the dataset from a text file and return it as an integer array
    public static int[][] readDataset(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        List<Integer> weightsList = new ArrayList<>();
        List<Integer> valuesList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split(" ");
            int weight = Integer.parseInt(parts[0]);
            int value = Integer.parseInt(parts[1]);
            weightsList.add(weight);
            valuesList.add(value);
        }
        reader.close();

        int[][] weightsAndValues = new int[2][weightsList.size()];
        for (int i = 0; i < weightsList.size(); i++) {
            weightsAndValues[0][i] = weightsList.get(i);
            weightsAndValues[1][i] = valuesList.get(i);
        }
        return weightsAndValues;
    }

    // Get the current memory usage in bytes
    public static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static int determineCapacity(String filename) {
        if (filename.contains("kecil")) {
            return 100;
        }
        else if (filename.contains("sedang")) {
            return 1000;
        }
        else if (filename.contains("besar")) {
            return 10000;
        }
        // Default value if the file name doesn't match expectations
        return 0;
    }
}
