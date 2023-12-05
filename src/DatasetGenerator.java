import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DatasetGenerator {
    public static void main(String[] args) {
        generateAndSaveDataset(100, "kecil");
        generateAndSaveDataset(1000, "sedang");
        generateAndSaveDataset(10000, "besar");
    }

    public static void generateAndSaveDataset(int size, String datasetType) {
        int[] weights = new int[size];
        int[] values = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            weights[i] = random.nextInt(50) + 1;  // Random weights between 1 and 50
            values[i] = random.nextInt(100) + 1;   // Random values between 1 and 100
        }

        String fileName = "dataset_" + datasetType + ".txt";
        try {
            FileWriter writer = new FileWriter(fileName);
            for (int i = 0; i < size; i++) {
                writer.write(weights[i] + " " + values[i] + "\n");
            }
            writer.close();
            System.out.println("Dataset saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
