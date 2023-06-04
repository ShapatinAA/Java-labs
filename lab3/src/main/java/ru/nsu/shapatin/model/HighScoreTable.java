package ru.nsu.shapatin.model;

import org.apache.logging.log4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreTable {
    private List<HighScoreEntry> entries;
    private static Logger logger = LogManager.getLogger(HighScoreTable.class);

    public HighScoreTable() {
        entries = new ArrayList<>();
        logger.info("New instance of HighScore table created.");
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
        logger.info("Loading HighScore table form file.");
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
        logger.info("Saving HighScore table to file.");
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