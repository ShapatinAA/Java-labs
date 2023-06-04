package ru.nsu.shapatin.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Game {
    private Cell[][] grid;
    private int mineCount;
    private int flagCount;
    private Thread timeCounterThread;
    private TimeCounter timeCounter;
    private String playerName;
    private int uncoveredCount;
    private HighScoreTable highScoreTable;
    private static Logger logger = LogManager.getLogger(Game.class.getName());

    public Game(String playerName, int width, int height, int mineCount) {
        this.grid = new Cell[width][height];
        this.mineCount = mineCount;
        this.uncoveredCount = width*height;
        this.highScoreTable = new HighScoreTable();
        highScoreTable.loadFromFile();
        this.playerName = playerName;
        this.flagCount = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.grid[x][y] = new Cell();
            }
        }
        logger.info("New instance of Game created.");
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public Cell getCell(Point point) {
        return grid[point.x][point.y];
    }

    public HighScoreTable getHighScoreTable() {
        logger.info("Get High Score table.");
        return highScoreTable;
    }

    public void updateHighScores(String playerName, int score) {
        logger.info("HighScores table start update.");
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score);
        highScoreTable.addEntry(newEntry);
        highScoreTable.saveToFile();
        logger.info("HighScores table updated.");
    }

    public void startNewGame() {
        logger.info("New game is started. Making layout (mines, etc).");
        this.flagCount = 0;

        // Очистка поля
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].reset();
            }
        }

        // Расставление мин
        Random random = new Random();
        for (int i = 0; i < mineCount; i++) {
            int x, y;
            do {
                x = random.nextInt(grid.length);
                y = random.nextInt(grid[0].length);
            } while (grid[x][y].isMine());
            logger.info("Set mine to x=" + x + "; y=" + y + ".");
            grid[x][y].setMine(true);
        }

        // Расчет чисел для всех клеток, не содержащих мин
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (!grid[x][y].isMine()) {
                    int adjacentMines = countAdjacentMines(x, y);
                    grid[x][y].setAdjacentMines(adjacentMines);
                }
            }
        }

        // Запуск таймера для новой игры
        if (timeCounterThread != null) {
            timeCounter.stop();
            try {
                timeCounterThread.join(); // ожидаем завершения потока
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.fatal("Thread was interrupted");
                throw new RuntimeException(e);
            }
        }

        // Запуск таймера для новой игры
        timeCounter = new TimeCounter();
        timeCounterThread = new Thread(timeCounter);
        timeCounterThread.start();
    }

    private int countAdjacentMines(int x, int y) {
        int mineCount = 0;
        int width = grid.length;
        int height = grid[0].length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                // Проверяем, что координаты находятся в пределах игрового поля
                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    Cell cell = grid[nx][ny];
                    if (cell.isMine()) {
                        mineCount++;
                    }
                }
            }
        }
        return mineCount;
    }

    private class TimeCounter implements Runnable {
        private final AtomicLong timeInSeconds = new AtomicLong();
        private volatile boolean isRunning = true;

        public void stop() {
            isRunning = false;
        }

        public long getTime() {
            return timeInSeconds.get();
        }

        @Override
        public void run() {
            while (isRunning) {
                // Обновляем время каждую секунду
                timeInSeconds.incrementAndGet();

                try {
                    Thread.sleep(1000); // Задержка в 1 секунду
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.fatal("Thread was interrupted");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public int getScore() {
        logger.info("Getting score of this play.");
        if (timeCounter == null) {
            return 0;
        }

        int timeSpent = (int) timeCounter.getTime();

        int score = 1000 - timeSpent;

        stopTimeThread();

        // гарантировать, что счет не станет отрицательным
        return Math.max(score, 0);
    }

    public void stopTimeThread() {
        timeCounter.stop();
        try {
            timeCounterThread.join(); // ожидаем завершения потока
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.fatal("Thread was interrupted");
            throw new RuntimeException(e);
        }
        timeCounterThread = null;
        timeCounter = null;
    }

    public void setFlagged(Cell cell, Point point) {
        logger.info("Flag cell x=" + point.x + "; y=" + point.y + ". This cell was" + cell.getState() + ".");
        Boolean b = !cell.isFlagged();
        cell.setFlagged(b);
        if (b) {
            flagCount++;
        }
        else {
            flagCount--;
        }
    }

    public void setRevealed(Point point, Cell cell) {
        logger.info("Reveal cell x=" + point.x + "; y=" + point.y + ".");
        // Проверяем, была ли ячейка уже открыта
        if (cell.isRevealed()) {
            return;
        }

        // Открываем текущую ячейку
        cell.setRevealed();
        uncoveredCount--;

        // Если ячейка не содержит мину и имеет 0 соседей с минами, рекурсивно открываем соседние ячейки
        if (!cell.getHasMine() && cell.getNearbyMines() == 0) {
            int x = point.x;
            int y = point.y;

            // Перебираем все соседние ячейки
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x + dx;
                    int ny = y + dy;

                    // Проверяем, что соседняя ячейка находится в пределах игрового поля
                    if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[0].length) {
                        Cell neighborCell = grid[nx][ny];
                        Point neighborPoint = new Point(nx,ny);

                        // Рекурсивно открываем соседнюю ячейку, если она еще не открыта
                        if (!neighborCell.isRevealed()) {
                            setRevealed(neighborPoint, neighborCell);
                        }
                    }
                }
            }
        }
    }

    public boolean checkWin() {
        if ((uncoveredCount == mineCount) && (flagCount == mineCount)) return true;
        else return false;
    }

    public Cell[][] getCells() {
        return grid;
    }

    public String getPlayerName() {
        return playerName;
    }

}
