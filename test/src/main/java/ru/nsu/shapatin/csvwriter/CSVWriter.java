package ru.nsu.shapatin.csvwriter;

import ru.nsu.shapatin.wordfrequency.WordFrequency;

import java.io.*;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class CSVWriter {
    public void writeToCSV(List<WordFrequency> wordFrequencyList) throws IOException {
        String projectPath = new File("").getAbsolutePath();
        String resourcesPath = Paths.get(projectPath, "src", "main", "resources").toString();
        String csvFilePath = Paths.get(resourcesPath, "word_frequencies.csv").toString();

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            StringBuilder csvContent = new StringBuilder("Word,Frequency,Frequency (%)\n");

            int totalWords = wordFrequencyList.stream()
                    .mapToInt(WordFrequency::frequency)
                    .sum();

            DecimalFormat decimalFormat = new DecimalFormat("#0.00",
                    DecimalFormatSymbols.getInstance(Locale.US));


            for (WordFrequency wordFrequency : wordFrequencyList) {
                double frequencyPercentage = (double) wordFrequency.frequency() * 100 / totalWords;
                csvContent.append(wordFrequency.word())
                        .append(",")
                        .append(wordFrequency.frequency())
                        .append(",")
                        .append(decimalFormat.format(frequencyPercentage))
                        .append("%\n");
            }

            writer.write(csvContent.toString());
        }
    }
}