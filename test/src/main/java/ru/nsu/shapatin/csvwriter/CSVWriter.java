package ru.nsu.shapatin.csvwriter;

import ru.nsu.shapatin.wordfrequency.WordFrequency;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class CSVWriter {
    public void writeToCSV(List<WordFrequency> wordFrequencyList) throws IOException {
        String projectPath = new File("").getAbsolutePath();
        String resourcesPath = Paths.get(projectPath, "src", "main", "resources").toString();
        String csvFilePath = Paths.get(resourcesPath, "word_frequencies.csv").toString();

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            StringBuilder csvContent = new StringBuilder("Word\tFrequency\tFrequency (%)\n");

            int totalWords = wordFrequencyList.stream()
                    .mapToInt(WordFrequency::frequency)
                    .sum();

            for (WordFrequency wordFrequency : wordFrequencyList) {
                double frequencyPercentage = (double) wordFrequency.frequency() * 100 / totalWords;
                csvContent.append(wordFrequency.word())
                        .append("\t")
                        .append(wordFrequency.frequency())
                        .append("\t")
                        .append(String.format("%.2f", frequencyPercentage))
                        .append("%\n");
            }

            writer.write(csvContent.toString());
        }
    }
}