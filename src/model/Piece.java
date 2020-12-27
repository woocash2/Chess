package model;

import javafx.util.Pair;

import java.util.ArrayList;

public abstract class Piece {
    public enum team{WHITE, BLACK};
    protected team color;
    protected char onBoard;
    protected Board board;
    protected int x, y; // position on board
    protected ArrayList<Pair<Integer, Integer>> reachablePositions;

    public Piece(int x, int y, team col) {
        this.x = x;
        this.y = y;
        this.color = col;
    }

    public abstract void updateReachablePositions();

    public void move(int nx, int ny) {
        board.positions[x][y] = '-';
        board.positions[nx][ny] = onBoard;
    };

    public void die() {

    };
}
