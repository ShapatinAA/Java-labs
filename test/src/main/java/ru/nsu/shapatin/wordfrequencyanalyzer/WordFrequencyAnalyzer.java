package ru.nsu.shapatin.wordfrequencyanalyzer;

import java.util.*;
import ru.nsu.shapatin.wordfrequency.WordFrequency;
public class WordFrequencyAnalyzer {
    public List<WordFrequency> analyze(List<String> words) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        for (String word : words) {
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }

        List<WordFrequency> wordFrequencyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            wordFrequencyList.add(new WordFrequency(entry.getKey(), entry.getValue()));
        }

        wordFrequencyList.sort(Collections.reverseOrder());

        return wordFrequencyList;
    }
}
