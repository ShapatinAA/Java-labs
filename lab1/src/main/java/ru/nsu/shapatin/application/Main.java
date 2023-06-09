package ru.nsu.shapatin.application;

import ru.nsu.shapatin.csvwriter.CSVWriter;
import ru.nsu.shapatin.filedatareader.FileDataReader;
import ru.nsu.shapatin.wordfrequency.WordFrequency;
import ru.nsu.shapatin.wordfrequencyanalyzer.WordFrequencyAnalyzer;
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
// Press Alt+Enter with your caret at the highlighted text to see how
// IntelliJ IDEA suggests fixing it.
// Press Shift+F10 or click the green arrow button in the gutter to run the code.
// Press Shift+F9 to start debugging your code. We have set one breakpoint
// for you, but you can always add more by pressing Ctrl+F8.

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Необходимо указать имя текстового файла в качестве аргумента");
            return;
        }

        String filename = args[0];

        FileDataReader fileDataReader = new FileDataReader();
        String projectPath = new File("").getAbsolutePath();
        String resourcesPath = Paths.get(projectPath, "src", "main", "resources").toString();
        List<String> words;
        try {
            String inputFilePath = Paths.get(resourcesPath, filename).toString();
            words = fileDataReader.readWords(inputFilePath);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer();
        List<WordFrequency> wordFrequencyList = analyzer.analyze(words);

        CSVWriter csvWriter = new CSVWriter();
        String csvFilePath = Paths.get(resourcesPath, "word_frequencies.csv").toString();

        try {
            csvWriter.writeToCSV(wordFrequencyList, csvFilePath);
            System.out.println("CSV файл успешно создан: word_frequencies.csv");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в CSV файл: " + e.getMessage());
        }

    }
}