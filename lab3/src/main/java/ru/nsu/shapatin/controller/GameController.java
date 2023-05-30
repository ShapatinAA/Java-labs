package ru.nsu.shapatin.controller;

import ru.nsu.shapatin.model.*;
import ru.nsu.shapatin.view.*;

import java.awt.Point;
import java.awt.event.*;

public class GameController {
    private Game game;
    private View view;
    private boolean isRunning;

    public GameController(Game game, String mode) {
        this.game = game;
        this.isRunning = true;
        if (mode.equals("text")) {
            this.view = new TextView(game, this);
        }
        if (mode.equals("graphic")) {
            this.view = new GraphicalView(game, this);
        }
    }

    public void displayAbout() {
        // Отображение информации об игре
        view.displayAbout();
    }

    public void displayHighScores() {
        view.displayHighScores();
    }

    public void cellClicked(Point point, int mouseButton) {
        if (mouseButton == MouseEvent.BUTTON1) { // Левая кнопка мыши
            Cell cell = game.getCell(point);
            if (!cell.isRevealed()) {
                game.setRevealed(point, cell);
                view.render();
                if (cell.isMine()) {
                    lose();
                } else {
                    if (checkWin()) {
                        win();
                    }
                }
            }
        } else if (mouseButton == MouseEvent.BUTTON3) { // Правая кнопка мыши
            Cell cell = game.getCell(point);
            if (!cell.isRevealed()) {
                game.setFlagged(cell);
                view.render();
            }
            if (checkWin()) {
                win();
            }
        }

    }

    public void startNewGame() {
        game.startNewGame();
        view.startNewGame();
    }

    private void win() {
        int score = game.getScore();
        game.updateHighScores(game.getPlayerName() ,score);
        view.displayWin(score);
    }

    private boolean checkWin() {
        return game.checkWin();
    }

    private void lose() {
        int score = game.getScore();
        view.displayLose(score);
    }

    public void exitGame() {
        isRunning = false;
        view.exitGame();
    }

    public boolean isRunning(boolean b) {
        if (!b) {
            isRunning = !isRunning;
        }
        return this.isRunning;
    }
}