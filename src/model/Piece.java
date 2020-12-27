package model;

import javafx.util.Pair;

import java.util.ArrayList;

public abstract class Piece {
    public enum team{WHITE, BLACK};
    protected team color;
    protected char onBoard;
    protected Board board;
    public int x, y; // position on board
    public ArrayList<Pair<Integer, Integer>> reachablePositions = new ArrayList<>();

    public Piece(int x, int y, team col, Board board) {
        this.x = x;
        this.y = y;
        this.color = col;
        this.board = board;
    }

    public abstract void updateReachablePositions();

    public void move(int nx, int ny) {
        board.positions[x][y] = '-';
        board.positions[nx][ny] = onBoard;
        x = nx;
        y = ny;
    };

    public team getColor() {
        return color;
    }

    public char getOnBoard() {
        return onBoard;
    }

    public void die() {

    };
}
