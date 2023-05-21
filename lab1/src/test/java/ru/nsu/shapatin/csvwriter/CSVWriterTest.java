package ru.nsu.shapatin.csvwriter;

import ru.nsu.shapatin.wordfrequency.WordFrequency;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CSVWriterTest {
    private CSVWriter csvWriter;
    private Path testOutputPath;
    private final String projectPath = new File("").getAbsolutePath();
    private final Path resourcesPath = Paths.get(projectPath, "src", "test", "resources");
    private final String csvFilePath = Paths.get(resourcesPath.toString(), "word_frequencies_test.csv").toString();

    @BeforeEach
    void setUp() throws IOException {
        csvWriter = new CSVWriter();
        testOutputPath = Files.createTempFile(resourcesPath,"word_frequencies_test", ".csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testOutputPath);
    }

    @Test
    void writeToCSV_ValidInput_CreatesCSVFile() throws IOException {
        // Arrange
        List<WordFrequency> wordFrequencyList = new ArrayList<>();
        wordFrequencyList.add(new WordFrequency("apple", 5));
        wordFrequencyList.add(new WordFrequency("banana", 3));
        wordFrequencyList.add(new WordFrequency("orange", 2));

        // Act
        csvWriter.writeToCSV(wordFrequencyList, testOutputPath.toString());

        // Assert
        assertTrue(Files.exists(testOutputPath));
    }

    @Test
    void writeToCSV_ValidInput_WritesCorrectData() throws IOException {
        // Arrange
        List<WordFrequency> wordFrequencyList = new ArrayList<>();
        wordFrequencyList.add(new WordFrequency("apple", 5));
        wordFrequencyList.add(new WordFrequency("banana", 3));
        wordFrequencyList.add(new WordFrequency("orange", 2));

        // Act
        csvWriter.writeToCSV(wordFrequencyList, testOutputPath.toString());

        // Assert
        List<String> lines = Files.readAllLines(testOutputPath, StandardCharsets.UTF_8);
        assertEquals(4, lines.size(), "Expected 4 lines in the CSV file");  // Including the column headers
        assertEquals("Word,Frequency,Frequency (%)", lines.get(0));
        assertEquals("apple,5,50.00%", lines.get(1));
        assertEquals("banana,3,30.00%", lines.get(2));
        assertEquals("orange,2,20.00%", lines.get(3));
    }
}