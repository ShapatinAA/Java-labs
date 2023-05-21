package ru.nsu.shapatin.wordfrequencyanalyzer;

import ru.nsu.shapatin.wordfrequency.WordFrequency;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WordFrequencyAnalyzerTest {
    private final WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer();

    @Test
    void analyze_ValidWords_ReturnsWordFrequencyList() {
        // Arrange
        List<String> words = Arrays.asList("apple", "banana", "apple", "orange", "orange", "banana", "apple");

        // Act
        List<WordFrequency> wordFrequencyList = analyzer.analyze(words);

        // Assert
        assertEquals(3, wordFrequencyList.size());
        assertEquals(new WordFrequency("apple", 3), wordFrequencyList.get(0));
        assertEquals(new WordFrequency("banana", 2), wordFrequencyList.get(1));
        assertEquals(new WordFrequency("orange", 2), wordFrequencyList.get(2));
    }

    @Test
    void analyze_EmptyWords_ReturnsEmptyList() {
        // Arrange
        List<String> words = new ArrayList<>();

        // Act
        List<WordFrequency> wordFrequencyList = analyzer.analyze(words);

        // Assert
        assertEquals(0, wordFrequencyList.size());
    }
}