package ru.nsu.shapatin.view;

import ru.nsu.shapatin.controller.GameController;
import ru.nsu.shapatin.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GraphicalView implements View {
    private Game game;
    private GameController gameController;
    private JFrame frame;
    private JPanel gamePanel;
    private JButton newGameButton;
    private JButton highScoresButton;
    private JButton aboutButton;
    private JButton exitButton;

    public GraphicalView(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
        setupUI();
    }
    private void setupUI() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(game.getWidth(), game.getHeight()));

        startNewGame();

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> gameController.startNewGame());

        highScoresButton = new JButton("High Scores");
        highScoresButton.addActionListener(e -> gameController.displayHighScores());

        aboutButton = new JButton("About");
        aboutButton.addActionListener(e -> gameController.displayAbout());

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exitGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(newGameButton);
        buttonPanel.add(highScoresButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(exitButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void startNewGame() {
        // Удаление всех кнопок ячеек из игровой панели
        gamePanel.removeAll();

        // Пересоздание кнопок ячеек
        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(50, 50));
                cellButton.putClientProperty("point", new Point(x, y));

                // Добавление слушателя действий для кнопки ячейки
                final int xPos = x;
                final int yPos = y;
                cellButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            gameController.cellClicked(new Point(xPos, yPos), MouseEvent.BUTTON1);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            gameController.cellClicked(new Point(xPos, yPos), MouseEvent.BUTTON3);
                        }
                    }
                });

                gamePanel.add(cellButton);
            }
        }

        // Обновление представления игры
        frame.revalidate();
        frame.repaint();
    }
    @Override
    public void render() {
        // Обновление состояния кнопок ячеек в графическом интерфейсе
        for (Component component : gamePanel.getComponents()) {
            if (component instanceof JButton) {
                JButton cellButton = (JButton) component;
                Point point = (Point) cellButton.getClientProperty("point");
                Cell cell = game.getCell(point);
                // Обновление внешнего вида кнопки ячейки в соответствии с состоянием ячейки
                updateCellButton(cellButton, cell);
            }
        }
    }
    private void updateCellButton(JButton cellButton, Cell cell) {
        if (cell.getState() == Cell.State.COVERED) {
            cellButton.setText(""); // Очистка текста на кнопке ячейки
            cellButton.setEnabled(true); // Включение кнопки ячейки
        } else if (cell.getState() == Cell.State.FLAGGED) {
            cellButton.setText("F"); // Установка текста "F" на кнопке ячейки для обозначения флага
            cellButton.setEnabled(true); // Включение кнопки ячейки
        } else if (cell.getState() == Cell.State.UNCOVERED) {
            // Отображение содержимого открытой ячейки
            if (cell.getHasMine()) {
                cellButton.setText("M"); // Установка текста "M" на кнопке ячейки для обозначения мины
            } else {
                int nearbyMines = cell.getNearbyMines();
                if (nearbyMines > 0) {
                    cellButton.setText(Integer.toString(nearbyMines)); // Установка числа соседних мин на кнопке ячейки
                } else {
                    cellButton.setText(""); // Очистка текста на кнопке ячейки, если нет соседних мин
                }
            }
            cellButton.setEnabled(false); // Отключение кнопки ячейки после открытия
        }
    }
    @Override
    public void displayHighScores() {
        // Получение таблицы рекордов
        HighScoreTable highScores = game.getHighScoreTable();

        // Создание строки с заголовками
        StringBuilder sb = new StringBuilder();
        sb.append("Name\t\tScore\n");

        // Добавление записей из таблицы рекордов
        for (HighScoreEntry entry : highScores.getEntries()) {
            sb.append(entry.getName()).append("\t\t").append(entry.getScore()).append("\n");
        }

        // Отображение таблицы рекордов в диалоговом окне
        JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void displayAbout() {
        // Текст с информацией об игре
        String aboutText = "Minesweeper\n\n" +
                "Minesweeper is a classic puzzle game where the player\n" +
                "needs to uncover all the cells on the grid without\n" +
                "triggering any mines.\n\n" +
                "Developed by: me\n" +
                "Version: 1.0";

        // Отображение информации об игре в диалоговом окне
        JOptionPane.showMessageDialog(frame, aboutText, "About Minesweeper", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void exitGame() {
        // Закрытие главного окна игры
        frame.dispose();
    }
    @Override
    public void displayWin(int score) {
        String message;
            message = "Поздравляем! Вы выиграли!\nВаш счет: " + score;

        Object[] options = {"Новая игра", "Выход"};
        int n = JOptionPane.showOptionDialog(frame,
                message,
                "Game is Over!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == 0) {
            gameController.startNewGame();
        } else {
            gameController.exitGame();
        }
    }
    @Override
    public void displayLose(int score) {
        String message;
        message = "Вы проиграли.\nВаш счет: " + score;

        Object[] options = {"Новая игра", "Выход"};
        int n = JOptionPane.showOptionDialog(frame,
                message,
                "Game is Over!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == 0) {
            gameController.startNewGame();
        } else {
            gameController.exitGame();
        }
    }
}