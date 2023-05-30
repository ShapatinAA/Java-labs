package ru.nsu.shapatin.model;

public class Cell {

    public enum State {
        COVERED,
        UNCOVERED,
        FLAGGED
    }
    boolean hasMine;
    private State state;
    private int nearbyMines;

    // Конструктор для Cell. По умолчанию ячейка покрыта, не помечена и не содержит мину
    public Cell() {
        this.state = State.COVERED;
        this.hasMine = false;
        this.nearbyMines = 0;
    }

    public void reset() {
        state = State.COVERED;
        hasMine = false;
        nearbyMines = 0;
    }

    public State getState() {
        return this.state;
    }

    public boolean getHasMine() {
        return this.hasMine;
    }

    public void setMine(boolean b) {
        hasMine = b;

    }

    public int getNearbyMines() {
        return this.nearbyMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        nearbyMines = adjacentMines;
    }

    public boolean isRevealed() {
        if (this.state == State.UNCOVERED) return true; else return false;
    }

    public void setRevealed() {
        this.state = State.UNCOVERED;
    }

    public boolean isMine() {
        return this.hasMine;
    }

    public void setFlagged(boolean b) {
        if (b) this.state = State.FLAGGED; else this.state = State.COVERED;
    }

    public boolean isFlagged() {
        return (this.state == State.FLAGGED);
    }
}

