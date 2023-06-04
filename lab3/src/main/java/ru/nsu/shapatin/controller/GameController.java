package ru.nsu.shapatin.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.shapatin.model.Cell;
import ru.nsu.shapatin.model.Game;
import ru.nsu.shapatin.view.GraphicalView;
import ru.nsu.shapatin.view.TextView;
import ru.nsu.shapatin.view.View;

import java.awt.*;
import java.awt.event.MouseEvent;
public class GameController {
    private Game game;
    private View view;
    private boolean isRunning;
    private static Logger logger = LogManager.getLogger(GameController.class.getName());

    public GameController(Game game, String mode) {
        this.game = game;
        this.isRunning = true;
        if (mode.equals("text")) {
            this.view = new TextView(game, this);
        }
        if (mode.equals("graphic")) {
            this.view = new GraphicalView(game, this);
        }
        logger.info("Instance of GameController is created.");
    }

    public void displayAbout() {
        // Отображение информации об игре
        view.displayAbout();
        logger.info("Info about displayed.");
    }

    public void displayHighScores() {
        view.displayHighScores();
        logger.info("HighScore displayed.");
    }

    public void cellClicked(Point point, int mouseButton) {
        try {
        if (mouseButton == MouseEvent.BUTTON1) { // Левая кнопка мыши
            logger.info("Reveal cell is in action.");
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
            logger.info("Flag cell is in action.");
            Cell cell = game.getCell(point);
            if (!cell.isRevealed()) {
                game.setFlagged(cell, point);
                view.render();
            }
            if (checkWin()) {
                win();
            }
        }
        } catch (Exception e) {
            logger.fatal("Error during cell click processing");
            throw new RuntimeException("Error during cell click processing", e);
        }

    }

    public void startNewGame() {
        try {
            game.startNewGame();
            view.startNewGame();
        } catch (Exception e) {
            logger.fatal("Error starting new game");
            throw new RuntimeException("Error starting new game", e);
        }
    }

    private void win() {
        logger.info("You won.");
        int score = game.getScore();
        game.updateHighScores(game.getPlayerName() ,score);
        view.displayWin(score);
    }

    private boolean checkWin() {
        logger.info("Checking win.");
        return game.checkWin();
    }

    private void lose() {
        logger.info("You lost.");
        int score = game.getScore();
        view.displayLose(score);
    }

    public void exitGame() {
        try {
            view.exitGame();
        } catch (Exception e) {
            logger.fatal("Error exiting game");
            throw new RuntimeException("Error exiting game", e);
        } finally {
            logger.info("Exiting Game.");
            isRunning = false;
            game.stopTimeThread();
        }
    }

    public boolean isRunning(boolean b) {
        if (!b) {
            isRunning = !isRunning;
        }
        return this.isRunning;
    }
}