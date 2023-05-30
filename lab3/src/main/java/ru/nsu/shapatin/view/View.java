package ru.nsu.shapatin.view;

import ru.nsu.shapatin.model.Cell;

import javax.swing.*;
import java.awt.*;

public interface View {
    void startNewGame();
    void render();
    void displayHighScores();
    void displayAbout();
    void exitGame();
    void displayWin(int score);
    void displayLose(int score);
}
