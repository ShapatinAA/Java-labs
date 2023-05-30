package ru.nsu.shapatin.model;

import java.io.*;
import java.util.*;
public class HighScoreTable {
    private List<HighScoreEntry> entries;

    public HighScoreTable() {
        entries = new ArrayList<>();
    }

    public void addEntry(HighScoreEntry entry) {
        entries.add(entry);
        Collections.sort(entries, (entry1, entry2) -> Integer.compare(entry2.getScore(), entry1.getScore()));
        if (entries.size() > 10) {
            entries.remove(10);
        }
    }
    public List<HighScoreEntry> getEntries() {
        return entries;
    }
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/HighScore.dat"))) {
            String line;
            this.entries.clear(); // очищаем текущую таблицу
            while ((line = reader.readLine()) != null) {
                // Предполагая, что каждая строка представляет собой одну запись,
                // и имя игрока и его счет разделены запятой
                String[] parts = line.split(",");
                String playerName = parts[0].trim();
                int score = Integer.parseInt(parts[1].trim());

                HighScoreEntry entry = new HighScoreEntry(playerName, score);
                this.entries.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/HighScore.dat"))) {
            for (HighScoreEntry entry : this.entries) {
                // Запись в файл в виде: Имя игрока,Счет
                writer.write(entry.getName() + "," + entry.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}