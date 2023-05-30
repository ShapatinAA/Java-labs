package ru.nsu.shapatin.view;

import ru.nsu.shapatin.controller.*;
import ru.nsu.shapatin.model.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

public class TextView implements View {
    private Game game;
    private GameController gameController;
    private Scanner scanner;

    public TextView(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void startNewGame() {
        System.out.println("A new game has started.");
        handleInput();
    }
    private void handleInput() {
        while (gameController.isRunning(true)) {
            System.out.print("Enter your command: ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "exit":
                    gameController.isRunning(false);
                    break;
                case "new":
                    gameController.startNewGame();
                    break;
                case "about":
                    gameController.displayAbout();
                    break;
                case "scores":
                    gameController.displayHighScores();
                    break;
                case "reveal":
                    Point cellToReveal = parseCoordinates(parts[1], parts[2]);
                    gameController.cellClicked(cellToReveal, MouseEvent.BUTTON1);
                    break;
                case "flag":
                    Point cellToFlag = parseCoordinates(parts[1], parts[2]);
                    gameController.cellClicked(cellToFlag, MouseEvent.BUTTON3);
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
        gameController.exitGame();
    }
    private Point parseCoordinates(String xStr, String yStr) {
        int x = Integer.parseInt(xStr);
        int y = Integer.parseInt(yStr);
        return new Point(x, y);
    }
    @Override
    public void render() {
        Cell[][] cells = game.getCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];
                if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print("M ");
                    } else {
                        System.out.print(cell.getNearbyMines() + " ");
                    }
                } else {
                    if (cell.isFlagged()) {
                        System.out.print("F ");
                    } else {
                        System.out.print(". ");
                    }
                }
            }
            System.out.println(); // переход на новую строку после каждой строки игрового поля
        }
    }
    @Override
    public void displayHighScores() {
        HighScoreTable highScores = game.getHighScoreTable();

        // Создание строки с заголовками
        StringBuilder sb = new StringBuilder();
        sb.append("Name\t\tScore\n");

        // Добавление записей из таблицы рекордов
        for (HighScoreEntry entry : highScores.getEntries()) {
            sb.append(entry.getName()).append("\t\t").append(entry.getScore()).append("\n");
        }
        System.out.println(sb);
    }
    @Override
    public void displayAbout() {
        String aboutText = "Minesweeper\n\n" +
                "Minesweeper is a classic puzzle game where the player\n" +
                "needs to uncover all the cells on the grid without\n" +
                "triggering any mines.\n\n" +
                "Developed by: me\n" +
                "Version: 1.0";
        System.out.println(aboutText);
    }
    @Override
    public void exitGame() {
        System.out.close();
    }
    @Override
    public void displayWin(int score) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Поздравляем! Вы выиграли!\nВаш счет: " + score);
        System.out.println("1. Новая игра\n2. Выход");
        System.out.print("Выберите опцию: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                gameController.startNewGame();
                break;
            case 2:
                gameController.exitGame();
                break;
            default:
                System.out.println("Недопустимая опция. Повторите попытку.");
                displayWin(score); // Вызов метода снова, если введенная опция недействительна
                break;
        }
    }
    @Override
    public void displayLose(int score) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Вы проиграли.\nВаш счет: " + score);
        System.out.println("1. Новая игра\n2. Выход");
        System.out.print("Выберите опцию: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                gameController.startNewGame();
                break;
            case 2:
                gameController.exitGame();
                break;
            default:
                System.out.println("Недопустимая опция. Повторите попытку.");
                displayLose(score); // Вызов метода снова, если введенная опция недействительна
                break;
        }
    }
}
