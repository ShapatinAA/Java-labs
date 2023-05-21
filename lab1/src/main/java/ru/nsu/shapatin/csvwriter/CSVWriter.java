package ru.nsu.shapatin.csvwriter;

import ru.nsu.shapatin.wordfrequency.WordFrequency;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class CSVWriter {
    public void writeToCSV(List<WordFrequency> wordFrequencyList, String outputPath) throws IOException {

        try (FileWriter writer = new FileWriter(outputPath)) {
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