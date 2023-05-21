package ru.nsu.shapatin.wordfrequency;

public record WordFrequency(String word, int frequency) implements Comparable<WordFrequency> {

    @Override
    public int compareTo(WordFrequency other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}