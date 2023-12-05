// reference: An improved branch and bound  algorithm for a strongly correlated unbounded knapsack problem
// Y-J Seong, Y-G G, M-K Kang & C-W Kang

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BranchandBound {
    private int capacity;
    private static List<Item> items;
    private static int[] bestConfiguration;
    private static long bestValue;
    static class Item {
        int value;
        int weight;

        Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

    public BranchandBound(int capacity, List<Item> items) {
        this.capacity = capacity;
        this.items = new ArrayList<>(items);
        eliminateDominatedItems();
        this.items.sort((a, b) -> Double.compare(b.value / (double) b.weight, a.value / (double) a.weight));
        this.bestConfiguration = new int[items.size()];
        this.bestValue = 0;
    }

    private void eliminateDominatedItems() {
        List<Item> nonDominated = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            boolean dominated = false;
            for (int j = 0; j < items.size(); j++) {
                if (i != j && items.get(j).weight <= items.get(i).weight && items.get(j).value >= items.get(i).value) {
                    dominated = true;
                    break;
                }
            }
            if (!dominated) {
                nonDominated.add(items.get(i));
            }
        }
        items = nonDominated;
    }

    private static int calculateUpperBound(int remainingCapacity, int currentValue, int index) {
        // Assuming items are sorted by value/weight ratio in descending order
        if (index < items.size() - 1) {
            int nextItemValue = items.get(index + 1).value;
            int nextItemWeight = items.get(index + 1).weight;
            return currentValue + (remainingCapacity / nextItemWeight) * nextItemValue;
        } else {
            return currentValue;
        }
    }

    public void solve() {
        initialize();
        branchAndBound(0, 0, capacity);
    }

    private void initialize() {
        Arrays.fill(this.bestConfiguration, 0);
        this.bestValue = 0;
    }

    private static void branchAndBound(int index, int currentValue, int remainingCapacity) {
        if (index == items.size()) {
            if (currentValue > bestValue) {
                bestValue = currentValue;
            }
            return;
        }

        int maxQty = remainingCapacity / items.get(index).weight;
        for (int qty = 0; qty <= maxQty; qty++) {
            int newValue = currentValue + qty * items.get(index).value;
            int newCapacity = remainingCapacity - qty * items.get(index).weight;
            int upperBound = calculateUpperBound(newCapacity, newValue, index);

            if (upperBound > bestValue) {
                bestConfiguration[index] = qty;
                branchAndBound(index + 1, newValue, newCapacity);
            }
        }
    }
    public int[] getBestConfiguration() {
        return bestConfiguration;
    }

    public long getBestValue() {
        return bestValue;
    }

    public static void main(String[] args) {
        int capacity = 100;
        List<Item> items = Arrays.asList(
                new Item(1, 1), new Item(30, 50)
        );

        BranchandBound solver = new BranchandBound(capacity, items);
        solver.solve();
        System.out.println("Best value: " + solver.getBestValue());
        System.out.println("Best item configuration: " + Arrays.toString(solver.getBestConfiguration()));
    }
}