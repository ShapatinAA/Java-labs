package ru.nsu.shapatin.filedatareader;


import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class FileDataReader {
    public List<String> readWords(String filename) throws IOException {
        List<String> words = new ArrayList<>();

        String projectPath = new File("").getAbsolutePath();
        String resourcesPath = Paths.get(projectPath, "src", "main", "resources").toString();
        String filePath = Paths.get(resourcesPath, filename).toString();

        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lineWords = line.split("[^\\p{L}\\p{N}]+");
                    for (String word : lineWords) {
                        if (!word.isEmpty()) {
                            words.add(word);
                        }
                    }
                }
            }
        } else {
            throw new IOException("Файл для чтения не найден: " + filePath);
        }

        return words;
    }
}
