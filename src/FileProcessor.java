import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileProcessor {
    private Set<String> stopWords = new HashSet<>();
    private static final String DELIMITERS = "[-+=" + " " + "\r\n " + "1234567890" + "’'\"" + "(){}<>\\[\\]" + ":" + "," + "‒–—―" + "…" + "!" + "." + "«»" + "-‐" + "?" + "‘’“”" + ";" + "/" + "⁄" + "␠" + "·" + "&" + "@" + "*" + "\\" + "•" + "^" + "¤¢$€£¥₩₪" + "†‡" + "°" + "¡" + "¿" + "¬" + "#" + "№" + "%‰‱" + "¶" + "′" + "§" + "~" + "¨" + "_" + "|¦" + "⁂" + "☞" + "∴" + "‽" + "※" + "]";
    public void loadStopWords(String path) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for(String w : words) stopWords.add(w.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error: Stop words file not found.");
        }
    }
    public void processFolder(String folderPath, HashTable<String, String> table) {
        final int[] counts = {0, 0};
        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            counts[0]++;
                            String fileName = path.getFileName().toString();
                            String content = Files.readString(path);

                            String[] words = content.split(DELIMITERS);

                            for (String w : words) {
                                String cleanWord = w.toLowerCase().trim();
                                if (cleanWord.length() > 2 && !stopWords.contains(cleanWord)) {
                                    table.put(cleanWord, fileName);
                                    counts[1]++;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("File error: " + path + " -> " + e.getMessage());
                        }
                    });
            System.out.println("-> " + counts[0] + " files and total " + counts[1] + " words processed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}