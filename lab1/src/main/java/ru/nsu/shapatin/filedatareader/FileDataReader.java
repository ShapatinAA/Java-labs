package ru.nsu.shapatin.filedatareader;


import java.io.*;
import java.util.*;

public class FileDataReader {
    public List<String> readWords(String filename) throws IOException {
        List<String> words = new ArrayList<>();

        File file = new File(filename);
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
            throw new IOException("Файл для чтения не найден: " + filename);
        }

        return words;
    }
}
