package ru.nsu.shapatin.model;

import ru.nsu.shapatin.model.Cell;
import ru.nsu.shapatin.model.HighScoreTable;

import java.awt.Point;
import java.time.*;
import java.util.Random;

public class Game {
    private Cell[][] grid;
    private int mineCount;
    private int flagCount;
    private Instant startTime;
    private Instant endTime;
    private String playerName;
    private int uncoveredCount;
    private HighScoreTable highScoreTable;

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

    }

    // Reveal a cell

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public void startTime() {
        startTime = Instant.now();
    }

    // вызывается, когда игра заканчивается
    public void endTime() {
        endTime = Instant.now();
    }

    public Cell getCell(Point point) {
        return grid[point.x][point.y];
    }

    public HighScoreTable getHighScoreTable() {
        return highScoreTable;
    }

    public void updateHighScores(String playerName, int score) {
        HighScoreEntry newEntry = new HighScoreEntry(playerName, score);
        highScoreTable.addEntry(newEntry);
        highScoreTable.saveToFile();
    }

    public void startNewGame() {
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
        startTime();
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

    public int getScore() {
        endTime();
        if (startTime == null || endTime == null) {
            return 0;
        }

        Duration timeSpent = Duration.between(startTime, endTime);

        int score = (int) (1000 - timeSpent.getSeconds());

        // гарантировать, что счет не станет отрицательным
        return Math.max(score, 0);
    }

    public void setFlagged(Cell cell) {
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

    // ... other game logic omitted for brevity
}
