package model;

import javafx.util.Pair;

import java.util.ArrayList;

public abstract class Piece {
    public enum team{WHITE, BLACK};
    public static team oponnent(team t) {
        if (t == team.WHITE) return team.BLACK;
        else return team.WHITE;
    }

    public team color;
    public char onBoard;
    public Board board;
    public int x, y; // position on board
    public boolean moved = false;

    public ArrayList<Pair<Integer, Integer>> reachablePositions = new ArrayList<>();
    public ArrayList<Pair<Integer, Integer>> takeablePositions = new ArrayList<>();

    public Piece(int x, int y, team col, Board board) {
        this.x = x;
        this.y = y;
        this.color = col;
        this.board = board;
    }

    public abstract void updatePositions();

    public void move(int nx, int ny) {
        board.move(x, y, nx, ny);
        x = nx;
        y = ny;
        moved = true;
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
