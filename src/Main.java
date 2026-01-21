import java.util.List;

public class Main {
    public static void main(String[] args) {

        //"SSF", "PAF"
        String hashType = "PAF";
        //"LP" (Linear), "DH" (Double)
        String collisionType = "DH";
        //0.5 or 0.8
        double loadFactor = 0.5;

        System.out.println("Test Configuration: " + hashType + " | " + collisionType + " | LF: " + loadFactor);

        IHashFunction hashFunc;
        if (hashType.equals("SSF")) hashFunc = new SimpleSummationHash();
        else hashFunc = new PolynomialHash();

        ICollisionResolver resolver;
        if (collisionType.equals("LP")) resolver = new LinearProbing();
        else resolver = new DoubleHashing();

        HashTable<String, String> table = new HashTable<>(1009, loadFactor, hashFunc, resolver);
        FileProcessor fp = new FileProcessor();

        fp.loadStopWords("resources/stop_words_en.txt");
        long startIndexTime = System.nanoTime();

        String basePath = "resources/bbc/bbc";
        String[] categories = {"business", "entertainment", "politics", "sport", "tech"};
        for (String cat : categories) {
            fp.processFolder(basePath + "/" + cat, table);
        }

        long endIndexTime = System.nanoTime();
        double indexingTimeSeconds = (endIndexTime - startIndexTime) / 1_000_000_000.0;

        System.out.println("\n---------- RESULTS ----------");
        System.out.println("Indexing Time: " + String.format("%.4f", indexingTimeSeconds) + " seconds");
        System.out.println("Total Collisions: " + table.getCollisionCount());

        //search any word
        table.get("computer");
        findSearchTime("resources/1000.txt", table);
    }


    public static void findSearchTime(String filePath, HashTable<String, String> table) {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filePath));

            double totalSec = 0;
            double minSec = Double.MAX_VALUE;
            double maxSec = Double.MIN_VALUE;

            for(String word : lines) {
                String searchKey = word.trim().toLowerCase();

                long s = System.nanoTime();
                PostingList pl = table.getSearch(searchKey);
                long e = System.nanoTime();
                double diffMs = (e - s) / 1_000_000.0;
                totalSec += diffMs;
                minSec = Math.min(minSec, diffMs);
                maxSec = Math.max(maxSec, diffMs);
            }

            System.out.println("Min Search time: " + String.format("%.4f", minSec) + " ms");
            System.out.println("Max Search time: " + String.format("%.4f", maxSec) + " ms");
            System.out.println("Avg Search time: " + String.format("%.4f", (totalSec / lines.size())) + " ms");

        } catch (Exception e) {
            System.out.println("search.txt not found: " + e.getMessage());
        }
    }
}